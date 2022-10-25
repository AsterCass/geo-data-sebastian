# 空间数据处理/存储/计算 geo-data-sebastian

## 功能清单
### 已完成
1. 支持mybatis直接使用gis中的数据类型进行处理不需要单独转换
   （tk.mybatis再加注解也可以使用给默认的方法调用sql）
2. gis, s2, jts 三种图像算法数据结构互转
3. 根据不规则点集合获取凹/凸面多边形
4. 判断点是否在区域内
5. 通过点计算其在google-s2算法下的cellId, 用于检索指定范围内所有点, 减少数据库查询压力
6. 计算两个点之间的地球距离
7. 判断多边形是否为真多边形(可能是点或者线)
8. 通过点构建多边形
9. 计算多边形的中心点
10. 获取多边形的在google-s2算法下的cellIdList
11. 判断多边形是否为线/点

### 进行中

## 快速开始

默认使用post-gis作为数据持久化, 使用mybatis和tk.mybatis查询插入数据

### mybatis 识别gis类

```java
/**
 * @author astercasc
 */
@Data
public class Address {

    @Id
    private long id;

    private String name;

    /**
     * 其他类型, 比如Polygon使用, 方法相同
     */
    @ColumnType(typeHandler = PointHandler.class)
    private Point geom;

}
```

### 插入后自动把主键写入插入对象中

```java
/**
 * @author astercasc
 */
@org.apache.ibatis.annotations.Mapper
public interface BlockMapper extends Mapper<Block> {

    /**
     * 参考 <a href="https://blog.csdn.net/shangcunshanfu/article/details/110847744">tk-mybatis + PostgreSQL返回主键自增</a>
     * @param block block
     * @return change col
     */
    @Override
    @InsertProvider(type = BaseInsertProvider.class, method = "dynamicSQL")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertSelective(Block block);

}
```

