package com.aster.sebastian.geo.mybatis;

import org.apache.ibatis.type.MappedTypes;
import org.postgis.Point;

/**
 * @author astercasc
 */
@MappedTypes(Point.class)
public class PointHandler extends GeoTypeHandler<Point> {

}
