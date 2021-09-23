package com.wistron.swpc.wismarttrafficlight.schedule;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.wistron.swpc.wismarttrafficlight.helper.WiLicenseHelper;
import com.wistron.swpc.wismarttrafficlight.service.AreaService;
import com.wistron.swpc.wismarttrafficlight.service.CsvDownloaderService;
import com.wistron.swpc.wismarttrafficlight.service.GoolgeService;
import com.wistron.swpc.wismarttrafficlight.service.IntersectionService;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.RoadSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApplicationSchedulerTask {

    @Autowired
    private AreaService areaService;

    @Autowired
    private GoolgeService googleservice;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private IntersectionService intersectionService;

    @Autowired
    private CsvDownloaderService csvDownloaderService;

    /**
     * 每天晚上 12 点周期性检查证书是否过期
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    public void checkLicense() {
        WiLicenseHelper.checkLicence();

        // 更新地图路径数据
        if (WiLicenseHelper.licenseStatus) {
            intersectionService.getAllStorageSpaceSpeedTask();
        }
    }

    /**
     * 每 120 秒去 openData 获取一次旅行时间数据并推送给前端
     */
    @Scheduled(fixedRate = 120000)
    public void travelTimeTask() {
        // 证书过期，任务取消
        if (!WiLicenseHelper.licenseStatus) {
            return;
        }

        AreaStatisticVO areaStatistic = areaService.getAreaStatistic();
        TrafficResponse response;
        if (null == areaStatistic) {
            response = TrafficResponse.error();
        } else {
            response = TrafficResponse.ok().setResult(areaStatistic);
        }

        simpMessagingTemplate.convertAndSend("/topic/area_statistic", response);

        RoadSpeedVO[] roadSpeedVO = {areaService.getRoadLineAndSpeedStatus("Dayuan", false), areaService.getRoadLineAndSpeedStatus("Dazu", false), areaService.getRoadLineAndSpeedStatus("Dazu_zzer", false)};
        TrafficResponse roadSpeedResponse;
        if (null == roadSpeedVO) {
            roadSpeedResponse = TrafficResponse.error();
        } else {
            roadSpeedResponse = TrafficResponse.ok().setResult(roadSpeedVO);
        }

        simpMessagingTemplate.convertAndSend("/topic/speed_from_opendata", roadSpeedResponse);
    }

    //     /**
    //  * 每 240 秒去 google api 获取一次旅行时间数据并推送给前端
    //  */
    // @Scheduled(fixedRate = 240000)
    // public void googleTravelTimeTask() {
    //     // 证书过期，任务取消
    //     if (!WiLicenseHelper.licenseStatus) {
    //         return;
    //     }

    //     AreaStatisticVO areaStatistic = googleservice.getAreaStatistic(true);
    //     TrafficResponse response;
    //     if (null == areaStatistic) {
    //         response = TrafficResponse.error();
    //     } else {
    //         googleservice.saveTrafficTrend();
    //         response = TrafficResponse.ok().setResult(areaStatistic);
    //     }

    //     simpMessagingTemplate.convertAndSend("/topic/area_statistic/google", response);

    //     RoadSpeedVO[] roadSpeedVO = {areaService.getRoadLineAndSpeedStatus("Dayuan", true), areaService.getRoadLineAndSpeedStatus("Dazu", true), areaService.getRoadLineAndSpeedStatus("Dazu_zzer", true)};
    //     TrafficResponse roadSpeedResponse;
    //     if (null == roadSpeedVO) {
    //         roadSpeedResponse = TrafficResponse.error();
    //     } else {
    //         roadSpeedResponse = TrafficResponse.ok().setResult(roadSpeedVO);
    //     }

    //     simpMessagingTemplate.convertAndSend("/topic/speed_from_opendata/google", roadSpeedResponse);
    // }

    /**
     * 每天晚上 12 點輸出交通趨勢流量csv檔
     * 
     * @throws IOException
     */
    @Scheduled(cron = "0 5 0 * * ? ")
    public void createCsv() throws IOException {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        csvDownloaderService.downloadFile(dateFormat.format(calendar.getTime()));
    }
}

