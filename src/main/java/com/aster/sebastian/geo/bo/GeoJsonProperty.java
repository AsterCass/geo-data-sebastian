package com.aster.sebastian.geo.bo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class GeoJsonProperty {

    @JSONField(name = "fill-opacity")
    private double opacity = 0.2;

    @JSONField(name = "stroke-width")
    private int width = 1;
}
