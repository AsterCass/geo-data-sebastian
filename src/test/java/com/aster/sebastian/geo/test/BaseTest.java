package com.aster.sebastian.geo.test;

import com.alibaba.fastjson2.JSON;
import com.aster.sebastian.geo.dto.BaseDto;
import com.aster.sebastian.geo.entity.Address;
import com.aster.sebastian.geo.mapper.AddressMapper;
import com.aster.sebastian.geo.mybatis.PointHandler;
import com.aster.sebastian.geo.utils.MybatisHelper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.postgis.Point;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

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


        SqlSession sqlSession = MybatisHelper.getSqlSession();
        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);

        List<Address> list = addressMapper.selectAll();

        Address address = list.get(0);

        PGgeometry pGgeometry = new PGgeometry(address.getGeom());
        Geometry geometry = pGgeometry.getGeometry();

        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void tkQueryTest() throws IOException, SQLException {


        SqlSession sqlSession = MybatisHelper.getSqlSession();
        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);
        MapperHelper mapperHelper = new MapperHelper();
        Configuration configuration = sqlSession.getConfiguration();
        configuration.getTypeHandlerRegistry().register(PointHandler.class);
        mapperHelper.processConfiguration(configuration);


        System.out.println("111");


        List<Address> list = addressMapper.selectByExample(new Example(Address.class));

        sqlSession.commit();
        sqlSession.close();
    }


    @Test
    public void insertTest() throws IOException, SQLException {


        SqlSession sqlSession = MybatisHelper.getSqlSession();
        AddressMapper addressMapper = sqlSession.getMapper(AddressMapper.class);
        MapperHelper mapperHelper = new MapperHelper();
        Configuration configuration = sqlSession.getConfiguration();
        configuration.getTypeHandlerRegistry().register(PointHandler.class);
        mapperHelper.processConfiguration(configuration);


        Address address = new Address();
        address.setName("laos");


        Point point = new Point(120.1300585269928, 30.2921636834525);
        point.setSrid(4326);
        address.setGeom((point));


        addressMapper.insert(address);
        addressMapper.insertOne(address.getGeom(), address.getName());


        List<Address> list = addressMapper.selectByExample(new Example(Address.class));
        List<Address> list2 = addressMapper.selectAll();
        System.out.println("111");

        sqlSession.commit();
        sqlSession.close();

    }

}
