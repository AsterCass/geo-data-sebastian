package com.aster.sebastian.geo.util;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author astercasc
 */
public class PropertyUtil {


    public static <T> T propertyToObject(Properties prop, Class<T> clazz) {
        try {
            Class<?> classType = Class.forName(clazz.getName());
            Object propertyInstance = classType.getDeclaredConstructor().newInstance();

            if (null != prop && 0 != prop.size()) {
                Field[] declaredFields = classType.getDeclaredFields();
                for (Field property : declaredFields) {
                    String name = property.getName();
                    String type = property.getGenericType().toString();
                    property.setAccessible(true);
                    String firstUpperName = firstUpperCase(name);

                    //忽略为null的属性
                    if (null == prop.get(name)) {
                        continue;
                    }

                    if ("class java.lang.String".equals(type)) {
                        Method method = propertyInstance.getClass()
                                .getDeclaredMethod("set" + firstUpperName, String.class);
                        method.invoke(propertyInstance, (String) prop.get(name));
                    }
                    if ("class java.lang.Integer".equals(type)) {
                        Method method = propertyInstance.getClass()
                                .getDeclaredMethod("set" + firstUpperName, Integer.class);
                        //这里if判断是兼容mybatis-config和yml文件的配置，mybatis的配置value值只能输入String格式，下同
                        Integer intStr;
                        if (prop.get(name) instanceof String) {
                            intStr = JSON.parseObject((String) prop.get(name), Integer.class);
                        } else {
                            intStr = (Integer) prop.get(name);
                        }
                        method.invoke(propertyInstance, intStr);
                    }
                    if ("java.util.Map<java.lang.String, java.lang.String>".equals(type)) {
                        Method method = propertyInstance.getClass()
                                .getDeclaredMethod("set" + firstUpperName, Map.class);
                        String mapStr;
                        //兼容mybatis-config和yml文件的配置
                        if (prop.get(name) instanceof String) {
                            mapStr = prop.get(name).toString();
                        } else {
                            mapStr = JSON.toJSONString(prop.get(name));
                        }

                        @SuppressWarnings("rawtypes")
                        Map strMap = JSON.parseObject(mapStr, Map.class);
                        method.invoke(propertyInstance, strMap);
                    }

                    if ("java.util.List<java.lang.String>".equals(type)) {
                        Method method = propertyInstance.getClass()
                                .getDeclaredMethod("set" + firstUpperName, List.class);
                        String listStr;
                        if (prop.get(name) instanceof String) {
                            listStr = prop.get(name).toString();
                        } else {
                            listStr = JSON.toJSONString(prop.get(name));
                        }
                        List<String> strList = JSON.parseArray(listStr, String.class);
                        method.invoke(propertyInstance, strList);
                    }

                    if ("java.util.List<java.lang.Integer>".equals(type)) {
                        Method method = propertyInstance.getClass()
                                .getDeclaredMethod("set" + firstUpperName, List.class);
                        String listStr;
                        if (prop.get(name) instanceof String) {
                            listStr = prop.get(name).toString();
                        } else {
                            listStr = JSON.toJSONString(prop.get(name));
                        }
                        List<Integer> strList = JSON.parseArray(listStr, Integer.class);
                        method.invoke(propertyInstance, strList);
                    }

                }
            }
            @SuppressWarnings("unchecked")
            T t = (T) propertyInstance;
            return t;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String firstUpperCase(String str) {
        return str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
    }

}
