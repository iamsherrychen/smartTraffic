package com.wistron.swpc.wismarttrafficlight.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wistron.swpc.wismarttrafficlight.dto.TrafficBoxMsgDTO;
import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import com.wistron.swpc.wismarttrafficlight.entity.SubIntersection;
import com.wistron.swpc.wismarttrafficlight.entity.TrafficTrend;
import com.wistron.swpc.wismarttrafficlight.mapper.IntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.service.AreaService;
import com.wistron.swpc.wismarttrafficlight.service.RoadCoordinateService;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.StorageSpeedVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class TrafficFlowDataHelperTest {

    @MockBean
    RoadCoordinateService roadCoordinateService;

    @Autowired
    TrafficFlowDataHelper trafficFlowDataHelper;

    @MockBean
    AreaService areaService;

    @SpyBean
    MongoTemplate mongoTemplate;

    @MockBean
    StringRedisTemplate stringRedisTemplate;

    @MockBean
    IntersectionMapper intersectionDao;

    @Test
    void processTrafficTrend() throws JsonProcessingException {
        String ipcMsgStr = "{\"MsgType\":\"RESPONSE\",\"Cmd\":\"GET_Intersection_FlowV2\",\"MsgSEQ\":\"1582536517\"," +
                "\"MsgTime\":\"1582536517\",\"IntersectionID\":\"H33700401\",\"Duration\":\"60\"," +
                "\"Msg\":[{\"sub_intersection_id\":\"1\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"6\",\"flow_out_bigcar\":\"16\"," +
                "\"flow_out_car\":\"16\",\"flow_out_motcar\":\"16\"},{\"connected_sub_intersection_id\":\"3\"," +
                "\"flow_out_bigcar\":\"13\",\"flow_out_car\":\"13\",\"flow_out_motcar\":\"13\"}]}," +
                "{\"sub_intersection_id\":\"6\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"3\",\"flow_out_bigcar\":\"63\"," +
                "\"flow_out_car\":\"63\",\"flow_out_motcar\":\"63\"},{\"connected_sub_intersection_id\":\"1\"," +
                "\"flow_out_bigcar\":\"61\",\"flow_out_car\":\"61\",\"flow_out_motcar\":\"61\"}]}," +
                "{\"sub_intersection_id\":\"3\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"1\",\"flow_out_bigcar\":\"31\"," +
                "\"flow_out_car\":\"31\",\"flow_out_motcar\":\"31\"},{\"connected_sub_intersection_id\":\"6\"," +
                "\"flow_out_bigcar\":\"36\",\"flow_out_car\":\"36\",\"flow_out_motcar\":\"36\"}]}," +
                "{\"sub_intersection_id\":\"8\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"1\",\"flow_out_bigcar\":\"81\"," +
                "\"flow_out_car\":\"81\",\"flow_out_motcar\":\"81\"},{\"connected_sub_intersection_id\":\"3\"," +
                "\"flow_out_bigcar\":\"31\",\"flow_out_car\":\"31\",\"flow_out_motcar\":\"31\"}," +
                "{\"connected_sub_intersection_id\":\"6\",\"flow_out_bigcar\":\"86\",\"flow_out_car\":\"86\"," +
                "\"flow_out_motcar\":\"86\"}]}]}";

        TrafficBoxMsgDTO msg;
        ObjectMapper objectMapper = new ObjectMapper();
        msg = objectMapper.readValue(ipcMsgStr, TrafficBoxMsgDTO.class);


        AreaStatisticVO areaStatistic = new AreaStatisticVO();
        areaStatistic.setAvgCarSpeedEast(100d);
        areaStatistic.setAvgCarSpeedWest(100d);
        Mockito.when(areaService.getAreaStatistic()).thenReturn(areaStatistic);

        List<TrafficTrend> trafficTrendList = new ArrayList<>();
        TrafficTrend trafficTrend = new TrafficTrend();
        trafficTrend.setIpcMsg(ipcMsgStr);
        trafficTrend.setHour(12);
        trafficTrend.setPcu(200d);
        trafficTrend.setSpeedEast(100d);
        trafficTrend.setSpeedWest(100d);
        trafficTrend.setIntersectionId("H33700401");
        trafficTrendList.add(trafficTrend);

        Mockito.doReturn(trafficTrendList).when(mongoTemplate).find(Mockito.any(Query.class), Mockito.eq(TrafficTrend.class));
        Mockito.doReturn(trafficTrend).when(mongoTemplate).save(Mockito.any(TrafficTrend.class));

        List<Intersection> intersectionList = new ArrayList<>();
        Intersection intersection = new Intersection();
        intersection.setId("H33700401");
        intersection.setName("test");
        intersectionList.add(intersection);
        Mockito.when(intersectionDao.selectAll()).thenReturn(intersectionList);

        trafficFlowDataHelper.processTrafficTrend(msg, "2020-05-25");
    }

    @Test
    void processStorageSpeedData() {
        String ipcMsgStr = "{\"MsgType\":\"RESPONSE\",\"Cmd\":\"GET_Intersection_FlowV2\",\"MsgSEQ\":\"1582536517\"," +
                "\"MsgTime\":\"1582536517\",\"IntersectionID\":\"H33700401\",\"Duration\":\"60\"," +
                "\"Msg\":[{\"sub_intersection_id\":\"1\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"6\",\"flow_out_bigcar\":\"16\"," +
                "\"flow_out_car\":\"16\",\"flow_out_motcar\":\"16\"},{\"connected_sub_intersection_id\":\"3\"," +
                "\"flow_out_bigcar\":\"13\",\"flow_out_car\":\"13\",\"flow_out_motcar\":\"13\"}]}," +
                "{\"sub_intersection_id\":\"6\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"3\",\"flow_out_bigcar\":\"63\"," +
                "\"flow_out_car\":\"63\",\"flow_out_motcar\":\"63\"},{\"connected_sub_intersection_id\":\"1\"," +
                "\"flow_out_bigcar\":\"61\",\"flow_out_car\":\"61\",\"flow_out_motcar\":\"61\"}]}," +
                "{\"sub_intersection_id\":\"3\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"1\",\"flow_out_bigcar\":\"31\"," +
                "\"flow_out_car\":\"31\",\"flow_out_motcar\":\"31\"},{\"connected_sub_intersection_id\":\"6\"," +
                "\"flow_out_bigcar\":\"36\",\"flow_out_car\":\"36\",\"flow_out_motcar\":\"36\"}]}," +
                "{\"sub_intersection_id\":\"8\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"1\",\"flow_out_bigcar\":\"81\"," +
                "\"flow_out_car\":\"81\",\"flow_out_motcar\":\"81\"},{\"connected_sub_intersection_id\":\"3\"," +
                "\"flow_out_bigcar\":\"31\",\"flow_out_car\":\"31\",\"flow_out_motcar\":\"31\"}," +
                "{\"connected_sub_intersection_id\":\"6\",\"flow_out_bigcar\":\"86\",\"flow_out_car\":\"86\"," +
                "\"flow_out_motcar\":\"86\"}]}]}";

        Set<String> flowData = new HashSet<>();
        flowData.add(ipcMsgStr);
        ZSetOperations zSetOperations = Mockito.mock(ZSetOperations.class);
        Mockito.when(stringRedisTemplate.opsForZSet()).thenReturn(zSetOperations);
        Mockito.when(stringRedisTemplate.opsForZSet().rangeByScore(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(flowData);

        List<SubIntersection> subIntersectionList = new ArrayList<>();
        SubIntersection subIntersection = new SubIntersection();
        subIntersection.setId("1");
        subIntersectionList.add(subIntersection);

        StorageSpeedVO response = trafficFlowDataHelper.processStorageSpeedData("H33700401", subIntersectionList);

        Assert.assertTrue(response.getSubIntersections().size() > 0);
    }

    @Test
    void processTrafficFlow() {
        String ipcMsgStr = "{\"MsgType\":\"RESPONSE\",\"Cmd\":\"GET_Intersection_FlowV2\",\"MsgSEQ\":\"1582536517\"," +
                "\"MsgTime\":\"1582536517\",\"IntersectionID\":\"H33700401\",\"Duration\":\"60\"," +
                "\"Msg\":[{\"sub_intersection_id\":\"1\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"6\",\"flow_out_bigcar\":\"16\"," +
                "\"flow_out_car\":\"16\",\"flow_out_motcar\":\"16\"},{\"connected_sub_intersection_id\":\"3\"," +
                "\"flow_out_bigcar\":\"13\",\"flow_out_car\":\"13\",\"flow_out_motcar\":\"13\"}]}," +
                "{\"sub_intersection_id\":\"6\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"3\",\"flow_out_bigcar\":\"63\"," +
                "\"flow_out_car\":\"63\",\"flow_out_motcar\":\"63\"},{\"connected_sub_intersection_id\":\"1\"," +
                "\"flow_out_bigcar\":\"61\",\"flow_out_car\":\"61\",\"flow_out_motcar\":\"61\"}]}," +
                "{\"sub_intersection_id\":\"3\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"1\",\"flow_out_bigcar\":\"31\"," +
                "\"flow_out_car\":\"31\",\"flow_out_motcar\":\"31\"},{\"connected_sub_intersection_id\":\"6\"," +
                "\"flow_out_bigcar\":\"36\",\"flow_out_car\":\"36\",\"flow_out_motcar\":\"36\"}]}," +
                "{\"sub_intersection_id\":\"8\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
                "\"sub_data\":[{\"connected_sub_intersection_id\":\"1\",\"flow_out_bigcar\":\"81\"," +
                "\"flow_out_car\":\"81\",\"flow_out_motcar\":\"81\"},{\"connected_sub_intersection_id\":\"3\"," +
                "\"flow_out_bigcar\":\"31\",\"flow_out_car\":\"31\",\"flow_out_motcar\":\"31\"}," +
                "{\"connected_sub_intersection_id\":\"6\",\"flow_out_bigcar\":\"86\",\"flow_out_car\":\"86\"," +
                "\"flow_out_motcar\":\"86\"}]}]}";

        Set<String> flowData = new HashSet<>();
        flowData.add(ipcMsgStr);
        ZSetOperations zSetOperations = Mockito.mock(ZSetOperations.class);
        Mockito.when(stringRedisTemplate.opsForZSet()).thenReturn(zSetOperations);
        Mockito.when(stringRedisTemplate.opsForZSet().rangeByScore(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(flowData);
        Mockito.when(stringRedisTemplate.opsForZSet().removeRangeByScore(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble()))
                .thenReturn(1L);

        Map<String, SubIntersection> subIntersectionMap = new HashMap<>();
        SubIntersection subIntersection = new SubIntersection();
        subIntersection.setId("1");
        trafficFlowDataHelper.processTrafficFlow("H33700401", 1, subIntersectionMap);
    }


}