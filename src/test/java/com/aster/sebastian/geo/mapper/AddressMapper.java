package com.aster.sebastian.geo.mapper;

import com.aster.sebastian.geo.entity.AddressEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AddressMapper {

    @Select("select * from address")
    List<AddressEntity> selectAll();


}
