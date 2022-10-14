package com.aster.sebastian.geo.mybatis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.LineString;

/**
 * @author astercasc
 */
@MappedTypes(LineString.class)
public class LineStringHandler extends GeoTypeHandler<LineString> {
}
