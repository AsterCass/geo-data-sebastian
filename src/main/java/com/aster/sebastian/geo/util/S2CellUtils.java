package com.aster.sebastian.geo.util;

import com.aster.sebastian.geo.constant.GoogleCellConstant;
import com.google.common.geometry.S2Cap;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2RegionCoverer;
import org.postgis.Point;
import org.postgis.Polygon;

import java.util.ArrayList;
import java.util.Collections;
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

    public static List<Long> getCellIdListByPolygonSimple(Polygon polygon, int maxLevel,
                                                          int minLevel) {
        return getCellIdListByPolygon(polygon, maxLevel, minLevel, RECOMMEND_MAX_CELL_NUM);
    }


    public static List<Long> getCellIdListByPolygon(Polygon polygon, int maxLevel,
                                                    int minLevel, int maxCells) {
        return PolygonUtils.getCellIdListByPolygon(polygon, maxLevel, minLevel, maxCells);
    }

    /**
     * simple radius around 7km(10 level) - 10m (20level)
     * default max cell num equal 20
     * <a href="https://s2geometry.io/resources/s2cell_statistics">else around reference</a>
     */
    public static List<Long> getCellIdListByCircleSimple(Point point, double radius) {
        return getCellIdListByCircle(point, radius,
                RECOMMEND_MIN_LEVEL, RECOMMEND_MAX_LEVEL, RECOMMEND_MAX_CELL_NUM);
    }

    /**
     * simple radius around 7km(10 level) - 10m (20level)
     * default max cell num equal 20
     * <a href="https://s2geometry.io/resources/s2cell_statistics">else around reference</a>
     */
    public static List<S2CellId> getCellListByCircleSimple(Point point, double radius) {
        return getCellListByCircle(point, radius,
                RECOMMEND_MIN_LEVEL, RECOMMEND_MAX_LEVEL, RECOMMEND_MAX_CELL_NUM);
    }

    /**
     * simple radius around 7km(10 level) - 10m (20level)
     * <a href="https://s2geometry.io/resources/s2cell_statistics">else around reference</a>
     */
    public static List<Long> getCellIdListByCircleSimple(Point point, double radius, Integer maxCellNum) {
        return getCellIdListByCircle(point, radius,
                RECOMMEND_MIN_LEVEL, RECOMMEND_MAX_LEVEL, maxCellNum);
    }

    /**
     * simple radius around 7km(10 level) - 10m (20level)
     * <a href="https://s2geometry.io/resources/s2cell_statistics">else around reference</a>
     */
    public static List<S2CellId> getCellListByCircleSimple(Point point, double radius, Integer maxCellNum) {
        return getCellListByCircle(point, radius,
                RECOMMEND_MIN_LEVEL, RECOMMEND_MAX_LEVEL, maxCellNum);
    }

    /**
     * <a href="https://s2geometry.io/resources/s2cell_statistics">level reference</a>
     */
    public static List<Long> getCellIdListByCircle(Point point, double radius,
                                                   int minLevel, int maxLevel, int maxCells) {
        return getCellListByCircle(point, radius, minLevel, maxLevel, maxCells)
                .stream().map(S2CellId::id).collect(Collectors.toList());
    }

    /**
     * <a href="https://s2geometry.io/resources/s2cell_statistics">level reference</a>
     */
    public static List<S2CellId> getCellListByCircle(Point point, double radius,
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

        return cellIdList;
    }

    /**
     * 获取指定级别的全部子cell
     */
    public static List<Long> getChildId(S2CellId root, int targetLevel) {
        if (root.level() < targetLevel) {
            long interval = (root.childEnd().id() - root.childBegin().id()) /
                    GoogleCellConstant.CHILD_CELL_NUM;
            List<Long> list = new ArrayList<>();
            for (int count = 0; count < GoogleCellConstant.CHILD_CELL_NUM; count++) {
                long id = root.childBegin().id() + interval * count;
                S2CellId cellId = new S2CellId(id);
                List<Long> childrenCellId = getChildId(cellId, targetLevel);
                list.addAll(childrenCellId);
            }
            return list;
        } else if (root.level() == targetLevel) {
            return Collections.singletonList(root.id());
        } else {
            return Collections.emptyList();
        }
    }


}
