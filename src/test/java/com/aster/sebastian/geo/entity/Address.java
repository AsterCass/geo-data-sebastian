package com.aster.sebastian.geo.entity;

import com.aster.sebastian.geo.mybatis.PointHandler;
import lombok.Data;
import org.postgis.Point;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.Id;

@Data
public class Address {

    @Id
    private long id;

    private String name;

    @ColumnType(typeHandler = PointHandler.class)
    private Point geom;

}
