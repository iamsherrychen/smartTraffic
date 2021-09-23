package com.wistron.swpc.wismarttrafficlight.controller;

import com.wistron.swpc.wismarttrafficlight.service.AreaService;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.RoadSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AreaController {

    @Autowired
    private AreaService areaService;

    /**
     * 订阅获取旅行时间 topic 时主动去 opendata 获取数据并返回
     * @return
     */
    @SubscribeMapping("/topic/area_statistic")
    public TrafficResponse getAreaStatistic() {
        AreaStatisticVO areaStatistic = areaService.getAreaStatistic();
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
    @SubscribeMapping("/topic/speed_from_opendata")
    public TrafficResponse getRoadLineAndSpeedStatus() {
        RoadSpeedVO[] roadSpeedVO = {areaService.getRoadLineAndSpeedStatus("Dayuan", false), areaService.getRoadLineAndSpeedStatus("Dazu", false), areaService.getRoadLineAndSpeedStatus("Dazu_zzer", false)};
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
    @SubscribeMapping("/topic/time_statistic")
    public TrafficResponse getTimeStatistic() {
        return TrafficResponse.ok().setResult(areaService.getTimeStatistic());
    }

    /**
     * 訂閱停等趋势圖，從 mongodb 獲取今日已經紀錄的 小時停等趋势
     * @return
     */
    @SubscribeMapping("/topic/car_delay")
    public TrafficResponse getCarDelay() {
        return TrafficResponse.ok().setResult(areaService.getCarDelay());
    }



}
