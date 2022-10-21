package com.aster.sebastian.geo.test;

import com.alibaba.fastjson2.JSON;
import com.aster.sebastian.geo.dto.BaseDto;
import com.aster.sebastian.geo.entity.Address;
import com.aster.sebastian.geo.mapper.AddressMapper;
import com.aster.sebastian.geo.mybatis.PointHandler;
import com.aster.sebastian.geo.util.ConcaveHullUtils;
import com.aster.sebastian.geo.util.PointUtils;
import com.aster.sebastian.geo.utils.MybatisHelper;
import com.google.common.geometry.S2CellUnion;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polygon;
import com.google.common.geometry.S2Region;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.algorithm.hull.ConcaveHull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.PrecisionModel;
import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.postgis.Point;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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


    /**
     * <a href="https://cloud.tencent.com/developer/article/2121902">参考</a>
     */
    @Test
    public void turfTest() {



        S2LatLng s11 = S2LatLng.fromDegrees(30.401051098438547, 120.0104819409752);
        S2LatLng s22 = S2LatLng.fromDegrees(30.132994731062112, 119.87040625738145);
        S2LatLng s33 = S2LatLng.fromDegrees(30.064083648538357, 120.19038306402207);
        S2LatLng s44 = S2LatLng.fromDegrees(30.243389716036948, 120.15055762456895);
        S2LatLng s55 = S2LatLng.fromDegrees(30.264742187753154, 120.2192221753502);
        S2LatLng s66 = S2LatLng.fromDegrees(30.31098994792401, 120.47328101324082);


        S2Loop loop = new S2Loop(new ArrayList<>(Arrays.asList(
                s11.toPoint(), s22.toPoint(), s33.toPoint(), s44.toPoint(), s55.toPoint(), s66.toPoint()
        )));


        S2CellUnion s2CellIds = new S2CellUnion();


        boolean f = loop.isValid();
        double y = loop.getArea();

        S2LatLngRect x = loop.getRectBound();

        org.locationtech.jts.geom.Point point21 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.92353339350231, 30.3802347446321), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point222 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.95649237787731, 30.309125591149492), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point23 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.08283515131481, 30.341131103380867), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point24 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.04653396988083, 30.27896717560416), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point25 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.9572700538652, 30.222025185285094), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point26 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.13991775894333, 30.13298730574284), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point27 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.15090408706833, 30.242195914437744), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point28 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.8858589210527, 30.26117634914401), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point29 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.0286811866777, 30.393936617093495), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point30 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.03554764175583, 30.464984097167374), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point31 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.15639725113083, 30.40459703755449), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point32 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.24016800308395, 30.354838455840728), new PrecisionModel(), 4326);

        org.locationtech.jts.geom.Point[] pointsx = {point21, point222, point23, point24, point25, point26, point27,
                point28, point29, point30, point31, point32};

        //凸面
        org.locationtech.jts.geom.Geometry geometry = new MultiPoint(pointsx,
                new GeometryFactory(new PrecisionModel(), 4326));
        org.locationtech.jts.geom.Geometry geometry1 = geometry.convexHull();

        Coordinate[] coordinates = {point21.getCoordinate(), point222.getCoordinate(), point23.getCoordinate(),
                point24.getCoordinate(), point25.getCoordinate(), point26.getCoordinate(),
                point27.getCoordinate(), point28.getCoordinate(), point29.getCoordinate(),
                point30.getCoordinate(), point31.getCoordinate(), point32.getCoordinate()};
        ConvexHull convexHull = new ConvexHull(coordinates, new GeometryFactory(new PrecisionModel(), 4326));
        org.locationtech.jts.geom.Geometry geometry2 = convexHull.getConvexHull();


        ConcaveHull concaveHull = new ConcaveHull(new MultiPoint(pointsx, new GeometryFactory(new PrecisionModel(), 4326)));
        concaveHull.setHolesAllowed(false);
        org.locationtech.jts.geom.Geometry geometry3 = concaveHull.getHull();
        String stl = geometry3.toString();


        boolean isIn = PointUtils.pointInRegionByJts(point23, geometry3);


        System.out.println(111);
    }


    @Test
    public void jtsConcaveTest() {

        org.locationtech.jts.geom.Point point1 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.92353339350231, 30.3802347446321), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point2 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.95649237787731, 30.309125591149492), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point3 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.08283515131481, 30.341131103380867), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point4 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.04653396988083, 30.27896717560416), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point5 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.9572700538652, 30.222025185285094), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point6 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.13991775894333, 30.13298730574284), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point7 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.15090408706833, 30.242195914437744), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point8 = new org.locationtech.jts.geom.Point
                (new Coordinate(119.8858589210527, 30.26117634914401), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point9 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.0286811866777, 30.393936617093495), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point10 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.03554764175583, 30.464984097167374), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point11 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.15639725113083, 30.40459703755449), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point12 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.24016800308395, 30.354838455840728), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point13 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.14055569697429, 30.33424634221984), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point14 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.14862378169109, 30.334542661523585), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point15 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.15274365473796, 30.327875260297272), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point point16 = new org.locationtech.jts.geom.Point
                (new Coordinate(120.14158566523601, 30.328023429700664), new PrecisionModel(), 4326);


        ArrayList<org.locationtech.jts.geom.Point> points = new ArrayList<>(
                Arrays.asList(point1, point2, point3, point4, point5, point6, point7,
                        point8, point9, point10, point11, point12, point13, point14, point15, point16));


        org.locationtech.jts.geom.Geometry geometry = ConcaveHullUtils
                .getJtsPolygonEarthByPoint(points, false, 0.9);

        org.locationtech.jts.geom.Point pointIn = new org.locationtech.jts.geom.Point
                (new Coordinate(120.14510569119948, 30.3312283585627), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point pointNotIn = new org.locationtech.jts.geom.Point
                (new Coordinate(120.85810025263835, 30.504777594565585), new PrecisionModel(), 4326);

        boolean isIn1 = PointUtils.pointInRegionByJts(pointIn, geometry);
        boolean isIn2 = PointUtils.pointInRegionByJts(pointNotIn, geometry);

        S2Point s2PointIn = PointUtils.jtsPointToS2PointEarth(pointIn);
        S2Point s2PointNotIn = PointUtils.jtsPointToS2PointEarth(pointNotIn);


        Coordinate[] coordinates = geometry.getCoordinates();
        List<org.locationtech.jts.geom.Point> boundList = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            org.locationtech.jts.geom.Point point = new org.locationtech.jts.geom.Point(
                    coordinate, new PrecisionModel(), 4326);
            boundList.add(point);
        }



        S2Region s2Region = new S2Polygon(new S2Loop(PointUtils.jtsPointToS2PointList(boundList)));
        S2Polygon s2Polygon = (S2Polygon) s2Region;


        boolean isIn3 = PointUtils.pointInRegionByS2(s2PointIn, s2Region);
        boolean isIn4 = PointUtils.pointInRegionByS2(s2PointNotIn, s2Region);


        System.out.println(geometry);
        System.out.println(s2Polygon);


        System.out.println("1111");

    }


    @Test
    public void pointConvertTest() {
        org.locationtech.jts.geom.Point pointIn = new org.locationtech.jts.geom.Point
                (new Coordinate(120.14510569119948, 30.3312283585627), new PrecisionModel(), 4326);
        org.locationtech.jts.geom.Point pointNotIn = new org.locationtech.jts.geom.Point
                (new Coordinate(120.85810025263835, 30.504777594565585), new PrecisionModel(), 4326);

        S2Point s2PointIn = PointUtils.jtsPointToS2PointEarth(pointIn);
        S2Point s2PointNotIn = PointUtils.jtsPointToS2PointEarth(pointNotIn);

        org.locationtech.jts.geom.Point pointInx = PointUtils.s2PointToJtsPoint(s2PointIn);
        org.locationtech.jts.geom.Point pointNotInx = PointUtils.s2PointToJtsPoint(s2PointNotIn);

        Point point = PointUtils.s2PointToGisPointEarth(s2PointIn);
        S2Point point1 = PointUtils.gisPointToS2Point(point);

        System.out.println("11111");

    }

    @Test
    public void twoPointDistant() {

        S2LatLng s11 = S2LatLng.fromDegrees(30.285786080528084, 120.08493741084148);
        S2LatLng s22 = S2LatLng.fromDegrees(30.304758088734506, 120.23634274531413);
        double a = s11.getEarthDistance(s22);

        Point point1 = new Point(120.08493741084148, 30.285786080528084);
        point1.setSrid(4326);
        Point point2 = new Point(120.23634274531413, 30.304758088734506);
        point2.setSrid(4326);

        double x = point1.distance(point2);


        org.postgis.Point pointx = new org.postgis.Point(120.08493741084148, 30.285786080528084);
        pointx.setSrid(4326);
        org.postgis.Point pointy = new org.postgis.Point(120.23634274531413, 30.304758088734506);
        pointy.setSrid(4326);

        double y = PointUtils.getEarthDistant(pointx, pointy);


        System.out.println(111111);


    }


}

