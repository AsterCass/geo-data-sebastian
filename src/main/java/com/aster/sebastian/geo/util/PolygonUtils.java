package com.aster.sebastian.geo.util;

import com.aster.sebastian.geo.constant.GeoConstant;
import com.aster.sebastian.geo.exception.SebastianParamException;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polygon;
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
import java.util.List;

/**
 * @author astercasc
 */
public class PolygonUtils {

    private static final int POINT_NUM = 1;

    private static final int LINE_NUM = 2;

    public static Polygon gisPolygonBuilderByGisPoints(List<Point> points) {
        if (null == points) {
            throw new RuntimeException("points is null");
        }
        LinearRing[] linearRings = {new LinearRing(points.toArray(new Point[0]))};
        return new Polygon(linearRings);
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


}
