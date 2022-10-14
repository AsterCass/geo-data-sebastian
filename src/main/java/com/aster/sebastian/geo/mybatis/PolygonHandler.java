package com.aster.sebastian.geo.mybatis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.Polygon;


/**
 * @author astercasc
 */
@MappedTypes(Polygon.class)
public class PolygonHandler extends GeoTypeHandler<Polygon> {
}
