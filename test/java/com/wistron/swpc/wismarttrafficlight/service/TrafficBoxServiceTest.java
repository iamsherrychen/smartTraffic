package com.wistron.swpc.wismarttrafficlight.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wistron.swpc.wismarttrafficlight.dto.TrafficBoxMsgDTO;
import com.wistron.swpc.wismarttrafficlight.entity.SubPhase;
import com.wistron.swpc.wismarttrafficlight.entity.TimePeriod;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.mapper.SubPhaseMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.TimePeriodMapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class TrafficBoxServiceTest {

    @MockBean
    IntersectionService intersectionService;

    @Autowired
    TrafficBoxService trafficBoxService;

    @SpyBean
    SimpMessagingTemplate messagingTemplate;

    @MockBean
    TrafficFlowDataHelper trafficFlowDataHelper;

    @MockBean
    SubPhaseMapper subPhaseDao;
    
    @MockBean
    TimePeriodMapper timePeriodDao;

    @Test
    void getControlStrategy() {
        trafficBoxService.getControlStrategy();
    }

    @Test
    void setControlStrategy() {
        trafficBoxService.setControlStrategy("test", "0x00");
    }

    @Test
    void publishControlStrategy() throws JsonProcessingException {
        String ipcMsgStr = "{\"MsgType\":\"RESPONSE\",\"Cmd\":\"GET_CONTROL_STRATEGY\",\"MsgSEQ\":\"1582536517\"," +
                "\"MsgTime\":\"1582536517\",\"Msg\":[{\"IntersectionID\":\"H33700401\",\"ControlStrategy\":\"0x10\"," +
                "\"Curr_SubPhaseID\":\"1\",\"Curr_Step\":\"1\",\"Curr_EffectTime\":\"2\"," +
                "\"ControlStrategy_From_Center\":\"0\",\"Status_Code\":\"1\"," +
                "\"SubPhaseID_1_EffectTime_Total\":\"20\",\"SubPhaseID_2_EffectTime_Total\":\"20\"," +
                "\"SubPhaseID_3_EffectTime_Total\":\"20\",\"SubPhaseID_4_EffectTime_Total\":\"20\"," +
                "\"SubPhaseID_5_EffectTime_Total\":\"20\",\"SubPhaseID_6_EffectTime_Total\":\"0\"," +
                "\"SubPhaseID_7_EffectTime_Total\":\"0\",\"SubPhaseID_8_EffectTime_Total\":\"0\"}]}";
        TrafficBoxMsgDTO msg;
        ObjectMapper objectMapper = new ObjectMapper();
        msg = objectMapper.readValue(ipcMsgStr, TrafficBoxMsgDTO.class);

        List<SubPhase> subPhaseList = new ArrayList<>();
        SubPhase subPhase = new SubPhase();
        subPhase.setId("1");
        subPhaseList.add(subPhase);

        List<TimePeriod> timePeriods = new ArrayList<>();
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setTimingUuid("test");
        timePeriod.setFromHourTime("0000");
        timePeriod.setToHourTime("2359");
        timePeriods.add(timePeriod);
        Mockito.when(timePeriodDao.selectTimePeriodByIntersectionId("H33700401")).thenReturn(timePeriods);
        Mockito.when(subPhaseDao.selectByTimingUuid("test")).thenReturn(subPhaseList);

        trafficBoxService.publishControlStrategy(msg.getMsg());
    }

    // @Test
    // void publishIntersectionFlow() throws JsonProcessingException {
    //     String ipcMsgStr = "{\"MsgType\":\"RESPONSE\",\"Cmd\":\"GET_Intersection_FlowV2\",\"MsgSEQ\":\"1582536517\"," +
    //             "\"MsgTime\":\"1582536517\",\"IntersectionID\":\"H33700401\",\"Duration\":\"60\"," +
    //             "\"Msg\":[{\"sub_intersection_id\":\"1\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
    //             "\"sub_data\":[{\"connected_sub_intersection_id\":\"6\",\"flow_out_bigcar\":\"16\"," +
    //             "\"flow_out_car\":\"16\",\"flow_out_motcar\":\"16\"},{\"connected_sub_intersection_id\":\"3\"," +
    //             "\"flow_out_bigcar\":\"13\",\"flow_out_car\":\"13\",\"flow_out_motcar\":\"13\"}]}," +
    //             "{\"sub_intersection_id\":\"6\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
    //             "\"sub_data\":[{\"connected_sub_intersection_id\":\"3\",\"flow_out_bigcar\":\"63\"," +
    //             "\"flow_out_car\":\"63\",\"flow_out_motcar\":\"63\"},{\"connected_sub_intersection_id\":\"1\"," +
    //             "\"flow_out_bigcar\":\"61\",\"flow_out_car\":\"61\",\"flow_out_motcar\":\"61\"}]}," +
    //             "{\"sub_intersection_id\":\"3\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
    //             "\"sub_data\":[{\"connected_sub_intersection_id\":\"1\",\"flow_out_bigcar\":\"31\"," +
    //             "\"flow_out_car\":\"31\",\"flow_out_motcar\":\"31\"},{\"connected_sub_intersection_id\":\"6\"," +
    //             "\"flow_out_bigcar\":\"36\",\"flow_out_car\":\"36\",\"flow_out_motcar\":\"36\"}]}," +
    //             "{\"sub_intersection_id\":\"8\",\"average_speed\":\"40\",\"quflow_arrive\":\"80\"," +
    //             "\"sub_data\":[{\"connected_sub_intersection_id\":\"1\",\"flow_out_bigcar\":\"81\"," +
    //             "\"flow_out_car\":\"81\",\"flow_out_motcar\":\"81\"},{\"connected_sub_intersection_id\":\"3\"," +
    //             "\"flow_out_bigcar\":\"31\",\"flow_out_car\":\"31\",\"flow_out_motcar\":\"31\"}," +
    //             "{\"connected_sub_intersection_id\":\"6\",\"flow_out_bigcar\":\"86\",\"flow_out_car\":\"86\"," +
    //             "\"flow_out_motcar\":\"86\"}]}]}";

    //     TrafficBoxMsgDTO msg;
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     msg = objectMapper.readValue(ipcMsgStr, TrafficBoxMsgDTO.class);

    //     trafficBoxService.publishIntersectionFlow(msg);
    // }

}