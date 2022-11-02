package com.aster.sebastian.geo.util;

import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2RegionCoverer;
import org.postgis.Point;
import org.postgis.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐信息参考 <a href="https://s2geometry.io/resources/s2cell_statistics">S2 Cell Statistics</a>
 *
 * @author astercasc
 */
public class S2CellUtils {

    private static final int RECOMMEND_MAX_CELL_NUM = 20;

    private static final int RECOMMEND_MAX_LEVEL = 20;

    private static final int RECOMMEND_MIN_LEVEL = 10;

    private static final double K_EARTH_CIRCUMFERENCE_METERS = 1000 * 40075.017;

    public static double earthMetersToRadians(double meters) {
        return (2 * Math.PI) * (meters / K_EARTH_CIRCUMFERENCE_METERS);
    }

    public static List<String> getCellIdListByPolygonSimple(Polygon polygon, int maxLevel,
                                                            int minLevel) {
        return getCellIdListByPolygon(polygon, maxLevel, minLevel, RECOMMEND_MAX_CELL_NUM);
    }


    public static List<String> getCellIdListByPolygon(Polygon polygon, int maxLevel,
                                                      int minLevel, int maxCells) {
        return PolygonUtils.getCellIdListByPolygon(polygon, maxLevel, minLevel, maxCells);
    }

    public static List<String> getCellIdListByCircleSimple(Point point, double radius) {
        return getCellIdListByCircle(point, radius,
                RECOMMEND_MIN_LEVEL, RECOMMEND_MAX_LEVEL, RECOMMEND_MAX_CELL_NUM);
    }

    public static List<String> getCellIdListByCircle(Point point, double radius,
                                                     int minLevel, int maxLevel, int maxCells) {

        S2Point thisPoint = PointUtils.gisPointToS2PointEarth(point);
        double radiusRadians = earthMetersToRadians(radius);
        S2Cap region = S2Cap.fromAxisHeight(thisPoint, (radiusRadians * radiusRadians) / 2.0);

        ArrayList<S2CellId> cellIdList = new ArrayList<>();

        S2RegionCoverer s2RegionCoverer = new S2RegionCoverer();
        s2RegionCoverer.setMaxLevel(maxLevel);
        s2RegionCoverer.setMaxCells(maxCells);
        s2RegionCoverer.setMinLevel(minLevel);

        s2RegionCoverer.getCovering(region, cellIdList);

        return cellIdList.stream().map(S2CellId::toToken).collect(Collectors.toList());
    }


}
