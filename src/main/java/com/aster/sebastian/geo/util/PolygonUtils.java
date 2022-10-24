package com.aster.sebastian.geo.util;

import com.aster.sebastian.geo.constant.GeoConstant;
import com.aster.sebastian.geo.exception.SebastianParamException;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polygon;
import com.google.common.geometry.S2RegionCoverer;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.postgis.LinearRing;
import org.postgis.Point;
import org.postgis.Polygon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author astercasc
 */
public class PolygonUtils {

    private static final int POINT_NUM = 1;

    private static final int LINE_NUM = 2;

    /**
     * is or not a strict polygon
     */
    public static boolean isGisPolygon(Polygon polygon) {
        return null != polygon && 0 != polygon.numPoints() &&
                !isPoint(polygon) && !isLine(polygon);
    }

    public static Polygon gisPolygonBuilderByGisPoints(List<Point> points) {
        if (null == points) {
            throw new RuntimeException("points is null");
        }
        LinearRing[] linearRings = {new LinearRing(points.toArray(new Point[0]))};
        Polygon polygon = new Polygon(linearRings);
        polygon.setSrid(points.get(0).getSrid());
        return polygon;
    }

    public static Point getGisPolygonCenterPoint(Polygon polygon) {
        if (null == polygon || 0 == polygon.numPoints()) {
            return null;
        }
        if (POINT_NUM == polygon.numPoints()) {
            return polygon.getPoint(0);
        }
        if (LINE_NUM == polygon.numPoints()) {
            Point point1 = polygon.getPoint(0);
            Point point2 = polygon.getPoint(1);
            Coordinate[] coordinates = {
                    new Coordinate(point1.getX(), point1.getY()),
                    new Coordinate(point2.getX(), point2.getY())};
            CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
            LineString lineString = new LineString(coordinateSequence,
                    new GeometryFactory(new PrecisionModel(), polygon.getSrid()));
            return PointUtils.jtsPointToGisPoint(lineString.getCentroid());
        }
        S2Polygon s2Polygon = gisPolygonToS2Polygon(polygon);
        return PointUtils.s2PointToGisPointEarth(s2Polygon.getCentroid());
    }

    public static S2Polygon gisPolygonToS2Polygon(Polygon polygon) {
        List<S2Point> points = new ArrayList<>();
        for (int count = 0; count < polygon.numPoints(); ++count) {
            Point point = polygon.getPoint(count);
            points.add(PointUtils.gisPointToS2PointEarth(point));
        }
        return new S2Polygon(new S2Loop(points));
    }

    public static boolean gisPointInGisPolygon(Point point, Polygon polygon) {
        if (point.getSrid() != polygon.getSrid()) {
            throw new RuntimeException("point's srid not equal polygon's");
        }
        S2Point s2Point = PointUtils.gisPointToS2PointEarth(point);
        S2Polygon s2Polygon = gisPolygonToS2Polygon(polygon);
        return s2Polygon.contains(s2Point);
    }

    public static Polygon jtsPolygonToGisPolygonEarth(org.locationtech.jts.geom.Polygon polygon) {
        if (!polygon.isValid()) {
            throw new SebastianParamException("polygon is not valid");
        }
        try {
            Polygon gisPol = new Polygon(polygon.toString());
            gisPol.setSrid(GeoConstant.DEFAULT_SRID);
            return gisPol;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SebastianParamException("convert polygon jts to gis fail");
        }
    }

    public static List<String> getCellIdListByPolygon(Polygon polygon, int maxLevel,
                                                      int minLevel, int maxCells) {
        if (null == polygon || 0 == polygon.numPoints()) {
            return null;
        }

        if (isPoint(polygon)) {
            return new ArrayList<>(Collections.singletonList(
                    PointUtils.getCellIdByGisPoint(polygon.getPoint(0))));
        }
        if (isLine(polygon)) {
            List<String> cellIdToken = new ArrayList<>();
            List<Point> points = getGisPointListByPolygon(polygon);
            if (null != points) {
                for (Point s2Point : points) {
                    cellIdToken.add(PointUtils.getCellIdByGisPoint(s2Point));
                }
            }
            return cellIdToken;
        }

        S2Polygon s2Polygon = gisPolygonToS2Polygon(polygon);

        ArrayList<S2CellId> test = new ArrayList<>();

        S2RegionCoverer s2RegionCoverer = new S2RegionCoverer();
        s2RegionCoverer.setMaxLevel(maxLevel);
        s2RegionCoverer.setMinLevel(minLevel);
        s2RegionCoverer.setMaxCells(maxCells);

        s2RegionCoverer.getCovering(s2Polygon, test);

        return test.stream().map(S2CellId::toToken).collect(Collectors.toList());
    }

    public static boolean isPoint(Polygon polygon) {
        if (null == polygon) {
            return false;
        }
        if (POINT_NUM == polygon.numPoints()) {
            return true;
        }

        boolean allPointEql = true;
        for (int count = 1; count < polygon.numPoints(); ++count) {
            if (!polygon.getPoint(count).equals(polygon.getPoint(count - 1))) {
                allPointEql = false;
                break;
            }
        }
        return allPointEql;
    }

    public static boolean isLine(Polygon polygon) {
        if (null == polygon || polygon.numPoints() < LINE_NUM) {
            return false;
        }
        if (LINE_NUM == polygon.numPoints()) {
            return true;
        }
        boolean collinear = true;
        for (int count = LINE_NUM; count < polygon.numPoints(); ++count) {
            int type = Orientation.index(
                    PointUtils.gisPointToJtsPoint(polygon.getPoint(count - LINE_NUM)).getCoordinate(),
                    PointUtils.gisPointToJtsPoint(polygon.getPoint(count - POINT_NUM)).getCoordinate(),
                    PointUtils.gisPointToJtsPoint(polygon.getPoint(count)).getCoordinate());
            if (Orientation.COLLINEAR != type) {
                collinear = false;
                break;
            }
        }
        return collinear;
    }

    public static List<Point> getGisPointListByPolygon(Polygon polygon) {
        if (isLine(polygon)) {
            List<Point> points = new ArrayList<>();
            for (int count = 0; count < polygon.numPoints(); ++count) {
                points.add(polygon.getPoint(count));
            }
            return points;
        }
        return null;
    }


}
