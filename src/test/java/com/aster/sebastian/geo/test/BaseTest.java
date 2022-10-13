package com.aster.sebastian.geo.test;

import com.alibaba.fastjson2.JSON;
import com.aster.sebastian.geo.dto.BaseDto;
import org.junit.Test;

public class BaseTest {


    @Test
    public void baseTest() {
        BaseDto baseDto = new BaseDto();
        baseDto.setId(123L);
        baseDto.setName("zhangS");
        System.out.println(JSON.toJSONString(baseDto));
    }


}
