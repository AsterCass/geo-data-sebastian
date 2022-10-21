package com.aster.sebastian.geo.util;

import com.aster.sebastian.geo.constant.GeoConstant;
import com.aster.sebastian.geo.exception.SebastianParamException;
import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
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
    public static S2Point jtsPointToS2PointEarth(Point point) {
        return S2LatLng.fromDegrees(point.getY(), point.getX()).toPoint();
    }

    /**
     * jts point list to s2 point list
     */
    public static List<S2Point> jtsPointToS2PointList(List<Point> points) {
        List<S2Point> pointList = new ArrayList<>();
        for (Point point : points) {
            pointList.add(jtsPointToS2PointEarth(point));
        }
        return pointList;
    }

    /**
     * gis point builder
     */
    public static org.postgis.Point gisPointEarthBuilder(double lat, double lng) {
        org.postgis.Point point = new org.postgis.Point(lng, lat);
        point.setSrid(GeoConstant.DEFAULT_SRID);
        return point;
    }

    /**
     * gis point to s2 point
     */
    public static S2Point gisPointToS2PointEarth(org.postgis.Point point) {
        return S2LatLng.fromDegrees(point.getY(), point.getX()).toPoint();
    }

    /**
     * gis point to jts point
     */
    public static Point gisPointToJtsPoint(org.postgis.Point point) {
        Coordinate[] coordinates = {new Coordinate(point.getX(), point.getY())};
        CoordinateSequence coordinateSequence = new CoordinateArraySequence(coordinates);
        return new Point(coordinateSequence, new GeometryFactory(new PrecisionModel(), point.srid));
    }


    /**
     * jts point to gis point
     */
    public static org.postgis.Point jtsPointToGisPoint(Point point) {
        org.postgis.Point gisPoint = new org.postgis.Point(point.getX(), point.getY());
        gisPoint.setSrid(point.getSRID());
        return gisPoint;
    }

    /**
     * gis point to jts point list
     */
    public static List<Point> gisPointToJtsPointList(List<org.postgis.Point> points) {
        List<Point> pointList = new ArrayList<>();
        if (null != points && 0 != points.size()) {
            for (org.postgis.Point point : points) {
                pointList.add(gisPointToJtsPoint(point));
            }
        }
        return pointList;
    }

    /**
     * gis point to s2 point
     */
    public static S2Point gisPointToS2Point(org.postgis.Point point) {
        return S2LatLng.fromDegrees(point.getY(), point.getX()).toPoint();
    }

    /**
     * s2 point to gis point
     */
    public static org.postgis.Point s2PointToGisPointEarth(S2Point s2Point) {
        S2LatLng s2LatLng = new S2LatLng(s2Point);
        org.postgis.Point point = new org.postgis.Point(s2LatLng.lngDegrees(), s2LatLng.latDegrees());
        point.setSrid(GeoConstant.DEFAULT_SRID);
        return point;
    }

    public static String getCellIdByGisPoint(org.postgis.Point point) {
        if (GeoConstant.DEFAULT_SRID != point.getSrid() ||
                GeoConstant.DEFAULT_SRID != point.getSrid()) {
            throw new SebastianParamException("point or other is not in earth (srid != 4326)");
        }

        S2Point s2Poi = gisPointToS2Point(point);
        S2CellId cellId = S2CellId.fromLatLng(new S2LatLng(s2Poi));
        return cellId.toToken();
    }


    /**
     * two point distant
     */
    public static double getEarthDistant(org.postgis.Point point,
                                         org.postgis.Point otherPoint) {
        if (GeoConstant.DEFAULT_SRID != point.getSrid() ||
                GeoConstant.DEFAULT_SRID != point.getSrid()) {
            throw new SebastianParamException("point or other is not in earth (srid != 4326)");
        }
        S2LatLng pointS2 = S2LatLng.fromDegrees(point.getY(), point.getX());
        S2LatLng otherPointS2 = S2LatLng.fromDegrees(otherPoint.getY(), otherPoint.getX());
        return pointS2.getEarthDistance(otherPointS2);
    }

}
