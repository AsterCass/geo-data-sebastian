package com.aster.sebastian.geo.util;

import com.aster.sebastian.geo.constant.GeoConstant;
import com.aster.sebastian.geo.exception.SebastianParamException;
import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Region;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author astercasc
 */
public class PointUtils {

    /**
     * 判断点是否在区域内
     */
    public static boolean pointInRegionByS2(S2Point s2Point, S2Region s2Region) {
        return s2Region.contains(new S2Cell(s2Point));
    }

    /**
     * 判断点是否在区域内
     */
    public static boolean pointInRegionByJts(Point point, Geometry geometry) {
        return geometry.contains(point);
    }

    /**
     * s2 point to jts point
     */
    public static Point s2PointToJtsPoint(S2Point s2Point) {
        S2LatLng s2LatLng = new S2LatLng(s2Point);
        if (!s2LatLng.isValid()) {
            throw new SebastianParamException("s2 point to jts point only support lat lng point");
        }
        Coordinate[] coordinates = {new Coordinate(s2LatLng.lngDegrees(), s2LatLng.latDegrees())};
        CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        return new Point(coordinateSequence, new GeometryFactory(new PrecisionModel(), GeoConstant.DEFAULT_SRID));
    }

    /**
     * s2 point list to jts point list
     */
    public static List<Point> s2PointToJtsPointList(List<S2Point> s2PointList) {
        List<Point> pointList = new ArrayList<>();
        for (S2Point s2Point : s2PointList) {
            pointList.add(s2PointToJtsPoint(s2Point));
        }
        return pointList;
    }

    /**
     * jts point to s2 point
     */
    public static S2Point jtsPointToS2Point(Point point) {
        return S2LatLng.fromDegrees(point.getY(), point.getX()).toPoint();
    }

    /**
     * jts point list to s2 point list
     */
    public static List<S2Point> jtsPointToS2PointList(List<Point> points) {
        List<S2Point> pointList = new ArrayList<>();
        for (Point point : points) {
            pointList.add(jtsPointToS2Point(point));
        }
        return pointList;
    }

    /**
     * gis point builder
     */
    public static org.postgis.Point GisPointBuilder(double lat, double lng) {
        org.postgis.Point point = new org.postgis.Point(lng, lat);
        point.setSrid(GeoConstant.DEFAULT_SRID);
        return point;
    }

}
