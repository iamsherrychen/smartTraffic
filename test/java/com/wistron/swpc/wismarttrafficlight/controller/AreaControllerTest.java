package com.wistron.swpc.wismarttrafficlight.controller;

import com.wistron.swpc.wismarttrafficlight.service.AreaService;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class AreaControllerTest {

    @Autowired
    AreaController areaController;

    @MockBean
    AreaService areaService;


    @Test
    void getAreaStatistic() {

        Mockito.when(areaService.getAreaStatistic()).thenReturn(null);
        TrafficResponse response = areaController.getAreaStatistic();
        Assert.assertTrue(!response.getSuccess());

        AreaStatisticVO areaStatisticVO = new AreaStatisticVO();
        Mockito.when(areaService.getAreaStatistic()).thenReturn(areaStatisticVO);

        response = areaController.getAreaStatistic();
        Assert.assertTrue(response.getSuccess());
        Assert.assertEquals(areaStatisticVO, response.getData());
    }

    @Test
    void getTimeStatistic() {
        List<TimeStatisticVO<List<StatisticDataVO>>> list = new ArrayList<>();
        Mockito.when(areaService.getTimeStatistic()).thenReturn(list);
        TrafficResponse response = areaController.getTimeStatistic();

        Assert.assertTrue(response.getSuccess());
        Assert.assertEquals(list, response.getData());
    }
}