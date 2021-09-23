package com.wistron.swpc.wismarttrafficlight.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wistron.swpc.wismarttrafficlight.entity.Device;
import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import com.wistron.swpc.wismarttrafficlight.service.DeviceService;
import com.wistron.swpc.wismarttrafficlight.service.IntersectionService;
import com.wistron.swpc.wismarttrafficlight.vo.ControlStrategyVO;
import com.wistron.swpc.wismarttrafficlight.vo.StorageSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubIntersectionFlowVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class IntersectionControllerTest {

    @Autowired
    IntersectionController intersectionController;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    IntersectionService intersectionService;

    @MockBean
    DeviceService deviceService;

    // @Test
    // void setControlStrategy() throws Exception {
    //     Mockito.when(intersectionService.setControlStrategy(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
    //             .thenReturn(true);

    //     ControlStrategyVO controlStrategyVO = new ControlStrategyVO();
    //     controlStrategyVO.setControlStrategy("0x10");
    //     String url = "/api/intersections/control_strategy";
    //     MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(url)
    //             .header("Authorization", "Basic YWRtaW46OGM2OTc2ZTViNTQxMDQxNWJkZTkwOGJkNGRlZTE1ZGZiMTY3YTljODczZmM0YmI4YTgxZjZmMmFiNDQ4YTkxOA==")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(new JsonMapper().writeValueAsString(controlStrategyVO));
    //     MvcResult result = mockMvc.perform(request)
    //             .andDo(MockMvcResultHandlers.print())
    //             .andExpect(MockMvcResultMatchers.status().is(202))
    //             .andReturn();

    //     Mockito.verify(intersectionService).setControlStrategy(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
    // }

    // @Test
    // void getAllIntersection() throws Exception {
    //     List<Intersection> list = Collections.emptyList();
    //     Mockito.when(intersectionService.getAllIntersection()).thenReturn(list);

    //     String url = "/api/intersections";
    //     MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url);
    //     MvcResult result = mockMvc.perform(request)
    //             .andDo(MockMvcResultHandlers.print())
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andReturn();

    //     TrafficResponse response = new JsonMapper().readValue(result.getResponse().getContentAsString(), TrafficResponse.class);

    //     Assert.assertTrue(response.getSuccess());
    // }

    // @Test
    // void getIntersectionById() throws Exception {
    //     Intersection intersection = new Intersection();
    //     Mockito.when(intersectionService.getIntersectionById("test_id")).thenReturn(intersection);

    //     String url = "/api/intersections/test_id";
    //     MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url);
    //     MvcResult result = mockMvc.perform(request)
    //             .andDo(MockMvcResultHandlers.print())
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andReturn();

    //     TrafficResponse response = new JsonMapper().readValue(result.getResponse().getContentAsString(), TrafficResponse.class);

    //     Assert.assertTrue(response.getSuccess());
    // }

    // @Test
    // void getIntersectionDevice() throws Exception {
    //     List<Device> list = Collections.emptyList();
    //     Mockito.when(deviceService.getDeviceByIntersectionId("test_id")).thenReturn(list);

    //     String url = "/api/intersections/test_id/devices";
    //     MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url);
    //     MvcResult result = mockMvc.perform(request)
    //             .andDo(MockMvcResultHandlers.print())
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andReturn();

    //     TrafficResponse response = new JsonMapper().readValue(result.getResponse().getContentAsString(), TrafficResponse.class);

    //     Assert.assertTrue(response.getSuccess());
    // }

    @Test
    void getStorageSpaceSpeedById() {
        StorageSpeedVO storageSpeedVO = new StorageSpeedVO();
        Mockito.when(intersectionService.getStorageSpaceSpeedById("test_id")).thenReturn(storageSpeedVO);

        TrafficResponse response = intersectionController.getStorageSpaceSpeedById("test_id");

        Assert.assertTrue(response.getSuccess());
        Assert.assertEquals(storageSpeedVO, response.getData());
    }

    @Test
    void getAllStorageSpaceSpeed() {
        Mockito.doNothing().when(intersectionService).getAllStorageSpaceSpeed();

        intersectionController.getAllStorageSpaceSpeed();
    }

    @Test
    void getTrafficFlow() {
        List<SubIntersectionFlowVO> list = Collections.emptyList();
        Mockito.when(intersectionService.getTrafficFlow("test_id", 1)).thenReturn(list);

        TrafficResponse response = intersectionController.getTrafficFlow("test_id", 1);

        Assert.assertTrue(response.getSuccess());
        Assert.assertEquals(list, response.getData());
    }
}