package com.aster.sebastian.geo.test;

import com.aster.sebastian.geo.util.ConcaveHullUtils;
import org.junit.Test;
import org.locationtech.jts.algorithm.MinimumBoundingCircle;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.locationtech.jts.simplify.TopologyPreservingSimplifier;

import java.util.ArrayList;
import java.util.Arrays;

public class JtsTest {


    @Test
    public void pointSimplifierTest() {

        Point point1 = new Point(new Coordinate(1.0, 2.0), new PrecisionModel(), 0);
        Point point2 = new Point(new Coordinate(2.0, 1.0), new PrecisionModel(), 0);
        Point point3 = new Point(new Coordinate(3.0, 1.0), new PrecisionModel(), 0);
        Point point4 = new Point(new Coordinate(1.0, -1.0), new PrecisionModel(), 0);
        Point point5 = new Point(new Coordinate(1.0, 0.0), new PrecisionModel(), 0);
        Point point6 = new Point(new Coordinate(1.0, 3.0), new PrecisionModel(), 0);
        Point point7 = new Point(new Coordinate(-1.0, 1.0), new PrecisionModel(), 0);
        Point point8 = new Point(new Coordinate(-1.0, -1.0), new PrecisionModel(), 0);
        Point point9 = new Point(new Coordinate(100.0, 1.0), new PrecisionModel(), 0);
        Point[] points = {point1, point2, point3, point4, point4, point5, point6, point7, point8, point9};

        Geometry geometry = ConcaveHullUtils.getJtsPolygonEarthByPoint(
                new ArrayList<>(Arrays.asList(points)), false, 0.8);

        Geometry newGeo1 = DouglasPeuckerSimplifier.simplify(geometry, 1);

        Geometry newGeo2 = TopologyPreservingSimplifier.simplify(geometry, 1);

        System.out.println("1111");

    }

    @Test
    public void testMinimumBoundingCircle() {

        Point point1 = new Point(new Coordinate(1.0, 2.0), new PrecisionModel(), 0);
        Point point2 = new Point(new Coordinate(2.0, 1.0), new PrecisionModel(), 0);
        Point point3 = new Point(new Coordinate(3.0, 1.0), new PrecisionModel(), 0);
        Point point4 = new Point(new Coordinate(1.0, -1.0), new PrecisionModel(), 0);
        Point point5 = new Point(new Coordinate(1.0, 0.0), new PrecisionModel(), 0);
        Point point6 = new Point(new Coordinate(1.0, 3.0), new PrecisionModel(), 0);
        Point point7 = new Point(new Coordinate(-1.0, 1.0), new PrecisionModel(), 0);
        Point point8 = new Point(new Coordinate(-1.0, -1.0), new PrecisionModel(), 0);
        Point point9 = new Point(new Coordinate(1000.0, 1.0), new PrecisionModel(), 0);
        Point[] points = {point1, point2, point3, point4, point4, point5, point6, point7, point8, point9};

        Geometry geometry = new MultiPoint(points, new PrecisionModel(), 0);

        MinimumBoundingCircle mini = new MinimumBoundingCircle(geometry);
        Coordinate geC = mini.getCentre();

        System.out.println("11111");
    }

}
