package com.aster.sebastian.geo.util;

import com.aster.sebastian.geo.constant.GeoConstant;
import com.aster.sebastian.geo.exception.SebastianParamException;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polygon;
import org.postgis.Point;
import org.postgis.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * @author astercasc
 */
public class PolygonUtils {

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
