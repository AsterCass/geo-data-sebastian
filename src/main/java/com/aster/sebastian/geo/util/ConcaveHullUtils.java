package com.aster.sebastian.geo.util;

import com.aster.sebastian.geo.constant.GeoConstant;
import com.aster.sebastian.geo.exception.SebastianParamException;
import org.locationtech.jts.algorithm.Orientation;
import org.locationtech.jts.algorithm.hull.ConcaveHull;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.List;

/**
 * @author astercasc
 */
public class ConcaveHullUtils {

    /**
     * 根据不规则点集合获取凹面多边形
     * <a href="https://gis.stackexchange.com/questions/158693/how-to-calculate-the-form-or-shape-factor-of-a-polygon">how to get</a>
     * <a href="https://postgis.net/docs/ST_ConcaveHull.html">post-gis</a>
     */
    public static Polygon getJtsPolygonEarthByPoint(List<Point> points,
                                                    boolean holesAllowed, double ratio) {
        ConcaveHull concaveHull = new ConcaveHull(new MultiPoint(points.toArray(new Point[0]),
                new GeometryFactory(new PrecisionModel(), GeoConstant.DEFAULT_SRID)));
        concaveHull.setHolesAllowed(holesAllowed);
        concaveHull.setMaximumEdgeLengthRatio(ratio);
        Geometry geo = concaveHull.getHull();
        if (geo instanceof Polygon) {
            Polygon polygon = (Polygon) geo;
            return Orientation.isCCW(polygon.getCoordinates()) ? polygon : polygon.reverse();
        } else {
            throw new SebastianParamException("points param error");
        }
    }

    public static org.postgis.Polygon getGisPolygonEarthByPoint(List<org.postgis.Point> points,
                                                                boolean holesAllowed, double ratio,
                                                                boolean compatiblePointAndLine) {
        List<Point> jtsPointList = PointUtils.gisPointToJtsPointList(points);
        if (compatiblePointAndLine) {
            try {
                Polygon jtsPol = getJtsPolygonEarthByPoint(jtsPointList, holesAllowed, ratio);
                return PolygonUtils.jtsPolygonToGisPolygonEarth(jtsPol);
            } catch (SebastianParamException exception) {
                return PolygonUtils.gisPolygonBuilderByGisPoints(points);
            }
        } else {
            Polygon jtsPol = getJtsPolygonEarthByPoint(jtsPointList, holesAllowed, ratio);
            return PolygonUtils.jtsPolygonToGisPolygonEarth(jtsPol);
        }
    }


}
