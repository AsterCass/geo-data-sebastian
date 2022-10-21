package com.aster.sebastian.geo.util;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2Polyline;
import com.google.common.geometry.S2RegionCoverer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author astercasc
 */
public class LineUtils {


    public static List<String> getCellIdListByS2Line(S2Polyline s2Polyline, int maxLevel,
                                                     int minLevel, int maxCells) {
        ArrayList<S2CellId> test = new ArrayList<>();
        S2RegionCoverer s2RegionCoverer = new S2RegionCoverer();
        s2RegionCoverer.setMinLevel(minLevel);
        s2RegionCoverer.setMaxLevel(maxLevel);
        s2RegionCoverer.setMaxCells(maxCells);
        s2RegionCoverer.getCovering(s2Polyline, test);
        return test.stream().map(S2CellId::toToken).collect(Collectors.toList());
    }


}
