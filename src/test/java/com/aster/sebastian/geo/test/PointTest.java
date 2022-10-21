package com.aster.sebastian.geo.test;

import org.junit.Test;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.MultiPoint;

public class PointTest {

    @Test
    public void collinear() {

        Coordinate co1 = new Coordinate(-1, -1);
        Coordinate co2 = new Coordinate(0, 0);
        Coordinate co3 = new Coordinate(-2, -2);
        Coordinate co4 = new Coordinate(1, 1);


        LineIntersector lineIntersector = new RobustLineIntersector();
        lineIntersector.computeIntersection(co1, co2, co3, co4);
        int x = lineIntersector.getIntersectionNum();


        System.out.println();

        int a = Orientation.index(co1, co2, co3);

        System.out.println();
    }


}
