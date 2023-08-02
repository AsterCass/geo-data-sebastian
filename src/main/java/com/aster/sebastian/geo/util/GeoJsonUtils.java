package com.aster.sebastian.geo.util;

import com.alibaba.fastjson2.JSON;
import com.aster.sebastian.geo.bo.GeoJson;
import com.aster.sebastian.geo.bo.GeoJsonProperty;
import com.aster.sebastian.geo.constant.GeoJsonConstant;
import com.google.common.geometry.S2Cell;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2Point;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author astercasc
 */
public class GeoJsonUtils {


    public static String getGeoJsonFromCellList(List<S2CellId> cellList) {
        String json = null;
        if (null != cellList && 0 != cellList.size()) {
            GeoJson geoJson = new GeoJson();
            geoJson.setType(GeoJsonConstant.FC_STR);

            GeoJson feature = new GeoJson();
            feature.setType(GeoJsonConstant.F_STR);
            feature.setProperties(new GeoJsonProperty());

            GeoJson geometry = new GeoJson();
            geometry.setType(GeoJsonConstant.MP_STR);
            List<List<List<List<BigDecimal>>>> coordinates = new ArrayList<>();
            for (S2CellId s2Cell : cellList) {
                List<List<List<BigDecimal>>> pointList = new ArrayList<>();
                List<List<BigDecimal>> vertex = new ArrayList<>();
                for (int ver = 0; ver < 4; ++ver) {
                    List<BigDecimal> verPoint = new ArrayList<>();
                    S2Cell cell = new S2Cell(s2Cell);
                    S2Point point = cell.getVertex(ver);
                    org.postgis.Point gisPoint = PointUtils.s2PointToGisPointEarth(point);
                    verPoint.add(BigDecimal.valueOf(gisPoint.getX()));
                    verPoint.add(BigDecimal.valueOf(gisPoint.getY()));
                    vertex.add(verPoint);
                }
                if (!vertex.isEmpty()) {
                    vertex.add(vertex.get(0));
                }
                pointList.add(vertex);

                coordinates.add(pointList);
            }
            geometry.setCoordinates(coordinates);
            feature.setGeometry(geometry);
            geoJson.setFeatures(Collections.singletonList(feature));
            json = JSON.toJSONString(geoJson);
        }
        return json;
    }

    public static String getGeoJsonFromCellIdList(List<Long> cellIdList) {
        String json = null;
        if (null != cellIdList && 0 != cellIdList.size()) {
            json = getGeoJsonFromCellList(cellIdList.stream().map(S2CellId::new)
                    .collect(Collectors.toList()));
        }
        return json;
    }

}
