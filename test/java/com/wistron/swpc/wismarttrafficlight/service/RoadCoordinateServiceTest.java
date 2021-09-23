package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.vo.RoadPointVO;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class RoadCoordinateServiceTest {

    @SpyBean
    RoadCoordinateService roadCoordinateService;

    // @Test
    // void processRoutePath() {
    //     // 直接使用真实数据进行测试
    //     List<RoadPointVO> roadPointList = roadCoordinateService.processRoutePath("H33700401", "H33700402", true);
    //     Assert.assertFalse(CollectionUtils.isEmpty(roadPointList));
    // }
}