package com.wistron.swpc.wismarttrafficlight.controller;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wistron.swpc.wismarttrafficlight.service.HistoryRecordService;
import com.wistron.swpc.wismarttrafficlight.util.DateUtil;
import com.wistron.swpc.wismarttrafficlight.vo.RecordDownloadVO;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class HistoryRecordControllerTest {

    // @Autowired
    // HistoryRecordController historyRecordController;

    // @Autowired
    // MockMvc mockMvc;

    // @MockBean
    // HistoryRecordService historyRecordService;

    // @Test
    // void search() throws Exception {
    //     List<TimeStatisticVO<List<StatisticDataVO>>> list = new ArrayList<>();
    //     String dateStr = DateUtil.getLocalDateString();
    //     Mockito.when(historyRecordService.search(dateStr)).thenReturn(list);

    //     String url = "/api/history/time_statistic?date="+dateStr;
    //     MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url);
    //     MvcResult result = mockMvc.perform(request)
    //             .andDo(MockMvcResultHandlers.print())
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andReturn();

    //     JsonMapper jsonMapper = new JsonMapper();
    //     TrafficResponse response = jsonMapper.readValue(result.getResponse().getContentAsString(), TrafficResponse.class);

    //     Assert.assertTrue(response.getSuccess());
    //     Assert.assertEquals(list, response.getData());
    // }

    // @Test
    // void download() throws Exception {
    //     RecordDownloadVO recordDownloadVO = new RecordDownloadVO();

    //     ClassPathResource classPathResource = new ClassPathResource("rsyslog.zip");
    //     recordDownloadVO.setZipFile(classPathResource.getFile());
    //     recordDownloadVO.setHasArchive(true);
    //     Mockito.when(historyRecordService.download("2020-05-26")).thenReturn(recordDownloadVO);

    //     String url = "/api/history/download?date=2020-05-26";
    //     MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(url);
    //     MvcResult result = mockMvc.perform(request)
    //             .andDo(MockMvcResultHandlers.print())
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andReturn();

    //     Assert.assertTrue(result.getResponse().containsHeader("Content-Disposition"));
    // }
}