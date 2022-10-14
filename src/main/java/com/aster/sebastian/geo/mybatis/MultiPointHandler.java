package com.aster.sebastian.geo.mybatis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.MultiPoint;


/**
 * @author astercasc
 */
@MappedTypes(MultiPoint.class)
public class MultiPointHandler extends GeoTypeHandler<MultiPoint> {
}
