package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import com.wistron.swpc.wismarttrafficlight.entity.SubIntersection;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.mapper.AccountMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.IntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.SubIntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.vo.StorageSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubIntersectionFlowVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class IntersectionServiceTest {

    @Autowired
    IntersectionService intersectionService;

    @MockBean
    AccountMapper accountDao;

    @MockBean
    RsyslogService rsyslogService;

    @MockBean
    TrafficBoxService trafficBoxService;

    @MockBean
    IntersectionMapper intersectionDao;

    @MockBean
    SubIntersectionMapper subIntersectionDao;

    @MockBean
    TrafficFlowDataHelper trafficFlowDataHelper;

    @MockBean
    SimpMessagingTemplate messagingTemplate;

    @Test
    void setControlStrategy() {
        Mockito.when(accountDao.authAccount("test", "test")).thenReturn(1);

        boolean response = intersectionService.setControlStrategy("test", "0x00", "test", "test");

        Assert.assertTrue(response);
    }

    @Test
    void getIntersectionById() {
        Intersection intersection = new Intersection();
        Mockito.when(intersectionDao.selectById("H33700401")).thenReturn(intersection);

        Intersection response = intersectionService.getIntersectionById("H33700401");

        Assert.assertEquals(intersection, response);
    }

    @Test
    void getAllIntersection() {
        List<Intersection> list = Collections.emptyList();
        Mockito.when(intersectionDao.selectAll()).thenReturn(Collections.emptyList());

        List<Intersection> response = intersectionService.getAllIntersection();

        Assert.assertEquals(list, response);
    }

    @Test
    void getTrafficFlow() {
        List<SubIntersection> subIntersectionList = new ArrayList<>();
        SubIntersection subIntersection = new SubIntersection();
        subIntersection.setId("H33700401");
        subIntersectionList.add(subIntersection);

        Mockito.when(subIntersectionDao.selectByIntersectionId("H33700401")).thenReturn(subIntersectionList);

        Map<String, SubIntersection> subIntersectionMap = new HashMap<>();
        subIntersectionMap.put("H33700401", subIntersection);
        List<SubIntersectionFlowVO> list = Collections.emptyList();
        Mockito.when(trafficFlowDataHelper.processTrafficFlow("H33700401", 1, subIntersectionMap))
                .thenReturn(list);

        List<SubIntersectionFlowVO> response = intersectionService.getTrafficFlow("H33700401", 1);

        Assert.assertEquals(list, response);
    }

    @Test
    void getStorageSpaceSpeedById() {

        List<SubIntersection> list = Collections.emptyList();
        Mockito.when(subIntersectionDao.selectByIntersectionId("H33700401")).thenReturn(list);
        StorageSpeedVO storageSpeedVO = new StorageSpeedVO();
        Mockito.when(trafficFlowDataHelper.processStorageSpeedData("H33700401", list))
                .thenReturn(storageSpeedVO);

        StorageSpeedVO response = intersectionService.getStorageSpaceSpeedById("H33700401");

        Assert.assertEquals(storageSpeedVO, response);
    }

    @Test
    void getAllStorageSpaceSpeed() {
        List<Intersection> intersectionList = new ArrayList<>();
        Intersection intersection = new Intersection();
        intersection.setId("H33700401");
        intersectionList.add(intersection);

        List<SubIntersection> subIntersectionList = new ArrayList<>();
        SubIntersection subIntersection = new SubIntersection();
        subIntersection.setId("1");
        subIntersectionList.add(subIntersection);

        Mockito.when(intersectionDao.selectAll()).thenReturn(intersectionList);
        Mockito.when(subIntersectionDao.selectByIntersectionId(intersection.getId())).thenReturn(subIntersectionList);

        StorageSpeedVO storageSpeedVO = new StorageSpeedVO();
        Mockito.when(trafficFlowDataHelper.processStorageSpeedData(intersection.getId(), subIntersectionList))
                .thenReturn(storageSpeedVO);

        intersectionService.getAllStorageSpaceSpeed();
        intersectionService.getAllStorageSpaceSpeedTask();
    }

}