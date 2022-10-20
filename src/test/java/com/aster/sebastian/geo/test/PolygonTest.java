package com.aster.sebastian.geo.test;

import com.aster.sebastian.geo.util.ConcaveHullUtils;
import com.aster.sebastian.geo.util.PolygonUtils;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.ArrayList;
import java.util.Arrays;

public class PolygonTest {


    @Test
    public void inPolTest() {
        org.locationtech.jts.geom.Point point1 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.92353339350231, 30.3802347446321), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point2 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.95649237787731, 30.309125591149492), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point3 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.08283515131481, 30.341131103380867), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point4 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.04653396988083, 30.27896717560416), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point5 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.9572700538652, 30.222025185285094), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point6 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.13991775894333, 30.13298730574284), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point7 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.15090408706833, 30.242195914437744), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point8 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.8858589210527, 30.26117634914401), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point9 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.0286811866777, 30.393936617093495), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point10 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.03554764175583, 30.464984097167374), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point11 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.15639725113083, 30.40459703755449), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point12 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.24016800308395, 30.354838455840728), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point13 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.14055569697429, 30.33424634221984), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point14 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.14862378169109, 30.334542661523585), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point15 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.15274365473796, 30.327875260297272), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point16 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.14158566523601, 30.328023429700664), new PrecisionModel(), 4326);


        ArrayList<Point> points = new ArrayList<>(
                Arrays.asList(point1, point2, point3, point4, point5, point6, point7,
                        point8, point9, point10, point11, point12, point13, point14, point15, point16));

        Polygon polygon = ConcaveHullUtils.getPolygonEarthByPoint(points, false, 0.9);


        org.postgis.Polygon polygon1 = PolygonUtils.jtsPolygonToGisPolygonEarth(polygon);


        org.postgis.Point pointIn = new org.postgis.Point(120.14510569119948, 30.3312283585627);
        pointIn.setSrid(4326);
        org.postgis.Point pointNotIn = new org.postgis.Point(120.85810025263835, 30.504777594565585);
        pointNotIn.setSrid(4326);


        boolean isIn1 = PolygonUtils.gisPointInGisPolygon(pointIn, polygon1);
        boolean isIn2 = PolygonUtils.gisPointInGisPolygon(pointNotIn, polygon1);


        System.out.println("11111");


    }
}
