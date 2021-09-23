package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.entity.Device;
import com.wistron.swpc.wismarttrafficlight.mapper.DeviceMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class DeviceServiceTest {

    @MockBean
    IntersectionService intersectionService;

    @Autowired
    DeviceService deviceService;

    @MockBean
    DeviceMapper deviceDao;

    @Test
    void getDeviceByIntersectionId() {
        List<Device> list = Collections.emptyList();
        Mockito.when(deviceDao.selectByIntersectionId("H33700401")).thenReturn(list);

        List<Device> response = deviceService.getDeviceByIntersectionId("H33700401");
        Assert.assertEquals(list, response);
    }
}