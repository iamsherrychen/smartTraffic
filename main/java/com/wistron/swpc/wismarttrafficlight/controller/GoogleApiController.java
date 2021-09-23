package com.wistron.swpc.wismarttrafficlight.controller;

import com.wistron.swpc.wismarttrafficlight.service.AreaService;
import com.wistron.swpc.wismarttrafficlight.service.GoolgeService;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.RoadSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
public class GoogleApiController {

    @Autowired
    private GoolgeService googleService;

    @Autowired
    private AreaService areaService;

    /**
     * 订阅获取旅行时间 topic 时主动去 opendata 获取数据并返回
     * @return
     */
    @SubscribeMapping("/topic/area_statistic/google")
    public TrafficResponse getAreaStatistic() {
        AreaStatisticVO areaStatistic = googleService.getAreaStatistic(false);
        if (null == areaStatistic) {
            return TrafficResponse.error();
        } else {
            return TrafficResponse.ok().setResult(areaStatistic);
        }
    }

    /**
     * 抓取路線圖並根據旅行時間回傳路況
     * @return
     */
    @SubscribeMapping("/topic/speed_from_opendata/google")
    public TrafficResponse getRoadLineAndSpeedStatus() {
        RoadSpeedVO[] roadSpeedVO = {areaService.getRoadLineAndSpeedStatus("Dayuan", true), areaService.getRoadLineAndSpeedStatus("Dazu", true), areaService.getRoadLineAndSpeedStatus("Dazu_zzer", true)};
        if (null == roadSpeedVO) {
            return TrafficResponse.error();
        } else {
            return TrafficResponse.ok().setResult(roadSpeedVO);
        }
    }

    /**
     * 订阅流量趋势图，从 mongodb 获取今日已经记录的 小时流量趋势
     * @return
     */
    @SubscribeMapping("/topic/time_statistic/google")
    public TrafficResponse getTimeStatistic() {
        return TrafficResponse.ok().setResult(googleService.getTimeStatistic());
    }

    @GetMapping(value = "/api/test/queryGoogleData/true",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse getqueryGoogleDataBySchedule() {
        return TrafficResponse.ok().setResult(googleService.getAreaStatistic(true));
    }

    @GetMapping(value = "/api/test/queryGoogleData/false",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse getqueryGoogleDataFromDataBase() {
        return TrafficResponse.ok().setResult(googleService.getAreaStatistic(false));
    }
}
