package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class AreaServiceTest {

    @MockBean
    IntersectionService intersectionService;

    @MockBean
    OpenDataService openDataService;

    @MockBean
    TrafficFlowDataHelper trafficFlowDataHelper;

    @MockBean
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    AreaService areaService;

    @Test
    void getAreaStatistic() {
        int[] travelTime = {100, 50};
        Mockito.when(openDataService.getTravelTime()).thenReturn(travelTime);
        ValueOperations valueOperations = Mockito.mock(ValueOperations.class);
        Mockito.when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        Mockito.when(valueOperations.get(Mockito.any())).thenReturn(null);
        Mockito.doNothing().when(valueOperations).set(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyLong(), Mockito.any());

        AreaStatisticVO response = areaService.getAreaStatistic();

        Assert.assertEquals(39.6, response.getAvgCarSpeedEast(), 0.0);
        Assert.assertEquals(57.6, response.getAvgCarSpeedWest(), 0.0);
        Assert.assertEquals(100, (int) response.getTravelTimeEast());
        Assert.assertEquals(50, (double) response.getTravelTimeWest(), 0.0);
    }

    @Test
    void getTimeStatistic() {
        List<TimeStatisticVO<List<StatisticDataVO>>> list = Collections.emptyList();
        Mockito.when(trafficFlowDataHelper.processTrafficTrend(null, null))
                .thenReturn(list);

        List<TimeStatisticVO<List<StatisticDataVO>>> response = areaService.getTimeStatistic();

        Assert.assertEquals(list, response);
    }
}