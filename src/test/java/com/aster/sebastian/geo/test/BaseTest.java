package com.aster.sebastian.geo.test;

import com.alibaba.fastjson2.JSON;
import com.aster.sebastian.geo.dto.BaseDto;
import com.aster.sebastian.geo.entity.AddressEntity;
import com.aster.sebastian.geo.mapper.AddressMapper;
import com.aster.sebastian.geo.utils.MybatisHelper;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.postgis.Geometry;
import org.postgis.PGgeometry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BaseTest {


    @Test
    public void baseTest() {
        BaseDto baseDto = new BaseDto();
        baseDto.setId(123L);
        baseDto.setName("zhangS");
        System.out.println(JSON.toJSONString(baseDto));
    }

    @Test
    public void queryTest() throws IOException, SQLException {

//        PGgeometry
        SqlSession sqlSession = MybatisHelper.getSqlSession();

        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);

        List<AddressEntity> list = addressMapper.selectAll();

        AddressEntity address = list.get(0);

        PGgeometry pGgeometry = new PGgeometry(address.getGeom());
        Geometry geometry = pGgeometry.getGeometry();

        System.out.println(JSON.toJSONString(geometry));
        System.out.println(JSON.toJSONString(list));
        sqlSession.close();
    }


}
