package com.aster.sebastian.geo.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GeoJson {

    private String type;

    private List<GeoJson> features;

    private GeoJson geometry;

    private GeoJsonProperty properties;

    private List<List<List<List<BigDecimal>>>> coordinates;

}
