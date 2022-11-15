package com.aster.sebastian.geo.test;

import com.aster.sebastian.geo.util.PointUtils;
import com.aster.sebastian.geo.util.S2CellUtils;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import org.junit.Test;
import org.postgis.Point;

import java.util.List;

public class S2CellTest {


    @Test
    public void circleTest() {
        Point point = new Point(120.130832, 30.292338);
        point.setSrid(4326);

        List<Long> cellIdList = S2CellUtils.getCellIdListByCircleSimple(
                point, 1 * 1000, 1);

        StringBuilder cellIdString = new StringBuilder();
        for (Long cellId : cellIdList) {
            cellIdString.append(cellId).append(",");
        }
        System.out.println(cellIdString);


        System.out.println("1111");

    }

    @Test
    public void buildCell() {
        S2CellId tmp = new S2CellId(6253078258273184841L);
        String TmpS = tmp.toToken();

        //=======================================

        S2CellId s2CellId = S2CellId.fromToken("344b6244");
        long id = s2CellId.id();
        long idMax = s2CellId.rangeMax().id();
        long idMin = s2CellId.rangeMin().id();

        boolean test = s2CellId.contains(S2CellId.fromToken("344b6247"));
        List<Long> x = S2CellUtils.getChildId(s2CellId, s2CellId.level() + 2);


        //===================================================

        Point point = PointUtils.gisPointEarthBuilder(120.20938, 30.206007);
        S2CellId cellId = S2CellId.fromLatLng(S2LatLng.fromDegrees(120.20938, 30.206007));
        Long tk = cellId.id();
        Long thiI = PointUtils.getCellIdByGisPoint(point);


        System.out.println("1111");


    }


}
