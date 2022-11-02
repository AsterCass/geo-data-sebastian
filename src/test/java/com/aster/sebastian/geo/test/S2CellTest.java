package com.aster.sebastian.geo.test;

import com.aster.sebastian.geo.util.S2CellUtils;
import org.junit.Test;
import org.postgis.Point;

import java.util.List;

public class S2CellTest {


    @Test
    public void circleTest() {
        Point point = new Point(120.14004736097823, 30.24686226145723);
        point.setSrid(4326);

        List<String> cellIdList = S2CellUtils.getCellIdListByCircleSimple(
                point, 10 * 1000);

        StringBuilder cellIdString = new StringBuilder();
        for (String cellId : cellIdList) {
            cellIdString.append(cellId).append(",");
        }
        System.out.println(cellIdString);


        System.out.println("1111");

    }


}
