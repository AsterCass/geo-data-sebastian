package com.aster.sebastian.geo.mapper;

import com.aster.sebastian.geo.entity.Address;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.postgis.Point;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AddressMapper extends Mapper<Address> {

    @Select("select * from address")
    List<Address> selectAll();

    @Insert("insert into address (geom, name) values (#{point}, #{name})")
    void insertOne(@Param("point") Point point, @Param("name") String name);


}
