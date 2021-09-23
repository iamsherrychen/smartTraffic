package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.helper.TrafficCarDelayDataHelper;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.vo.RecordDownloadVO;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class HistoryRecordServiceTest {

    @MockBean
    IntersectionService intersectionService;

    @MockBean
    TrafficFlowDataHelper trafficFlowDataHelper;

    @MockBean
    TrafficCarDelayDataHelper trafficCarDelayDataHelper;

    @Autowired
    HistoryRecordService historyRecordService;

    @Test
    void download() {
        RecordDownloadVO response = historyRecordService.download("timing","2020-05-20");

        // clear test data
        if (!response.getHasArchive()) {
            response.getZipFile().delete();
        }
    }

    @Test
    void search() {
        List<TimeStatisticVO<List<StatisticDataVO>>> list = Collections.emptyList();
        Mockito.when(trafficFlowDataHelper.processTrafficTrend(null, "2020-05-20"))
                .thenReturn(list);

        List<TimeStatisticVO<List<StatisticDataVO>>> response = historyRecordService.search("2020-05-20");

        Assert.assertEquals(list, response);
    }

    void searchCarDelay() {
        List<TimeStatisticVO<List<StatisticDataVO>>> list = Collections.emptyList();
        Mockito.when(trafficCarDelayDataHelper.processCarDelay(null, "2020-05-20"))
                .thenReturn(list);

        List<TimeStatisticVO<List<StatisticDataVO>>> response = historyRecordService.searchCarDelay("2020-05-20");

        Assert.assertEquals(list, response);
    }
}