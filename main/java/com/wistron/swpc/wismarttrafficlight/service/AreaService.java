package com.wistron.swpc.wismarttrafficlight.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficCarDelayDataHelper;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.RoadPointVO;
import com.wistron.swpc.wismarttrafficlight.vo.RoadSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AreaService {

    private static Logger logger = LoggerFactory.getLogger(AreaService.class);

    @Autowired
    private OpenDataService openDataService;

    @Autowired
    private TrafficFlowDataHelper trafficFlowDataHelper;

    @Autowired
    private TrafficCarDelayDataHelper trafficCarDelayDataHelper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RoadCoordinateService roadCoordinateService;

    @Autowired
    private GoolgeService goolgeService;

    private JsonMapper jsonMapper = new JsonMapper();

    private static final String TRAVEL_TIME_KEY = "travel_time";

    private static final String RED = "red";

    private static final String GREEN = "green";

    @Value("${ids.dayuan.start_intersection_id}")
    private String dayuanStartIntersectionId;

    @Value("${ids.dayuan.end_intersection_id}")
    private String dayuanEndIntersectionId;

    @Value("${ids.dazu.start_intersection_id}")
    private String dazuStartIntersectionId;

    @Value("${ids.dazu.end_intersection_id}")
    private String dazuEndIntersectionId;

    @Value("${ids.dazu.zzer.start_intersection_id}")
    private String dazuZzerStartIntersectionId;

    @Value("${ids.dazu.zzer.end_intersection_id}")
    private String dazuZzerEndIntersectionId;

    @Value("${ids.basic_speed}")
    private Double basicSpeed;


    /**
     * ??????????????????
     *
     * ??????????????? 0.8*3600/(??????????????????)
     * ??????????????? 1.1*3600/(??????????????????)
     * ???????????????(??????????????????+??????????????????)/2
     *
     * @return
     */
    public AreaStatisticVO getAreaStatistic() {

        String travelTimeStr = stringRedisTemplate.opsForValue().get(TRAVEL_TIME_KEY);

        // if (!StringUtils.isEmpty(travelTimeStr)) {
        //     try {
        //         return jsonMapper.readValue(travelTimeStr, AreaStatisticVO.class);
        //     } catch (JsonProcessingException e) {
        //         logger.error("[traffic] parse travelTimeStr error", e);
        //     }
        // }

        int[] travelTime = openDataService.getTravelTime();



        if (null == travelTime) {
            if (!StringUtils.isEmpty(travelTimeStr)) {
                try {
                    return jsonMapper.readValue(travelTimeStr, AreaStatisticVO.class);
                } catch (JsonProcessingException e) {
                    logger.error("[traffic] parse travelTimeStr error", e);
                }
            }
            // if getTravelTime error, return null
            return null;
        }

        AreaStatisticVO areaStatisticVO = new AreaStatisticVO();

        //??????
        double eastSpeed = travelTime[0] <= 0 ? 0.0 : 1.1 * 3600 / travelTime[0];
        double westSpeed = travelTime[1] <= 0 ? 0.0 : 0.8 * 3600 / travelTime[1];

        //??????
        double dazuZzerForwardSpeed = travelTime[2] <= 0 ? 0.0 : 0.19 * 3600 / travelTime[2]; //??????86->88 **10471
        double dazuZzerReverseSpeed = travelTime[3] <= 0 ? 0.0 : 0.33 * 3600 / travelTime[3]; //??????284->285 **
        double dazuNqrForwardSpeed = travelTime[4] <= 0 ? 0.0 : 0.756 * 3600 / travelTime[4]; //???31????????????286->87 **EA14814_EA14811
        double dazuNqrReverseSpeed = travelTime[5] <= 0 ? 0.0 : (0.369+0.599) * 3600 / travelTime[5]; //???31????????????288->84 + 85->287 **E729350_E245155 + E245156_EA14850
        double dazuNqrTurnZzerForwardSpeed = travelTime[6] <= 0 ? 0.0 : 0.45 * 3600 / travelTime[6]; //???31?????????????????????288->88 **E729350_EA04745
        double dazuNqrTurnZzerReverseSpeed = travelTime[7] <= 0 ? 0.0 : 0.19 * 3600 / travelTime[7]; //?????????????????????????????????87->284 **EA05100_EA14811

        //??????
        areaStatisticVO.setAvgCarSpeedEast(new BigDecimal(eastSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        areaStatisticVO.setAvgCarSpeedWest(new BigDecimal(westSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

        //??????
        areaStatisticVO.setAvgCarSpeedDazuZzerForward(new BigDecimal(dazuZzerForwardSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        areaStatisticVO.setAvgCarSpeedDazuZzerReverse(new BigDecimal(dazuZzerReverseSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        areaStatisticVO.setAvgCarSpeedDazuNqrForward(new BigDecimal(dazuNqrForwardSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        areaStatisticVO.setAvgCarSpeedDazuNqrReverse(new BigDecimal(dazuNqrReverseSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        areaStatisticVO.setAvgCarSpeedDazuNqrTurnZzerForward(new BigDecimal(dazuNqrTurnZzerForwardSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        areaStatisticVO.setAvgCarSpeedDazuNqrTurnZzerReverse(new BigDecimal(dazuNqrTurnZzerReverseSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());

        //??????
        areaStatisticVO.setTravelTimeEast(travelTime[0]);
        areaStatisticVO.setTravelTimeWest(travelTime[1]);

        //??????
        areaStatisticVO.setTravelTimeDazuZzerForward(travelTime[2]);
        areaStatisticVO.setTravelTimeDazuZzerReverse(travelTime[3]);

        areaStatisticVO.setTravelTimeDazuNqrForward(travelTime[4]);
        areaStatisticVO.setTravelTimeDazuNqrReverse(travelTime[5]);
        areaStatisticVO.setTravelTimeDazuNqrTurnZzerForward(travelTime[6]);
        areaStatisticVO.setTravelTimeDazuNqrTurnZzerReverse(travelTime[7]);

        try {
            stringRedisTemplate.opsForValue().set(TRAVEL_TIME_KEY, jsonMapper.writeValueAsString(areaStatisticVO), 2, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            logger.error("[traffic] cache travelTime error", e);
        }

        


        return areaStatisticVO;
    }

    /**
     * ???open API???????????????????????????????????????????????????
     * forward => ????????????????????????(??????????????? -> ????????????-????????????)
     * reverse => ????????????????????????(????????????-???????????? -> ???????????????)
     * startIntersectionId => ???????????????????????????(?????????)
     * endIntersectionId => ????????????????????????-????????????(?????????)
     */
    public RoadSpeedVO getRoadLineAndSpeedStatus(String projectName, Boolean google) {

        AreaStatisticVO areaStatisticVO = new AreaStatisticVO();
        if(google) {
            areaStatisticVO = goolgeService.getAreaStatistic(false);
        } else {
            areaStatisticVO = getAreaStatistic();
        }
        
        //????????????????????????
        double westSpeed = -1;
        double eastSpeed = -1;

        //?????????31???(?????????)????????????
        double dazuSpeedForward = -1;
        double dazuSpeedReverse = -1;

        //?????????????????????????????????
        double dazuZzerSpeedForward = -1;
        double dazuZzerSpeedReverse = -1;

        if(areaStatisticVO != null) {
            westSpeed = areaStatisticVO.getAvgCarSpeedWest();
            eastSpeed = areaStatisticVO.getAvgCarSpeedEast();
            dazuSpeedForward = areaStatisticVO.getAvgCarSpeedDazuNqrForward();
            dazuSpeedReverse = areaStatisticVO.getAvgCarSpeedDazuNqrReverse();
            dazuZzerSpeedForward = areaStatisticVO.getAvgCarSpeedDazuZzerForward();
            dazuZzerSpeedReverse = areaStatisticVO.getAvgCarSpeedDazuZzerReverse();
        }

        //??????????????????
        List<RoadPointVO> dayuanForwardRoadList = roadCoordinateService.processRoutePath(dayuanStartIntersectionId, dayuanEndIntersectionId, false);
        //??????????????????
        List<RoadPointVO> dayuanReverseRoadList = roadCoordinateService.processRoutePath(dayuanEndIntersectionId, dayuanStartIntersectionId, false);

        //??????(???31???)????????????
        List<RoadPointVO> dazuForwardRoadList = roadCoordinateService.processRoutePath(dazuStartIntersectionId, dazuEndIntersectionId, false);
        //??????(???31???)????????????
        List<RoadPointVO> dazuReverseRoadList = roadCoordinateService.processRoutePath(dazuEndIntersectionId, dazuStartIntersectionId, false);

        //??????(????????????)????????????
        List<RoadPointVO> dazuZzerForwardRoadList = roadCoordinateService.processRoutePath(dazuZzerStartIntersectionId, dazuZzerEndIntersectionId, false);
        //??????(????????????)????????????
        List<RoadPointVO> dazuZzerReverseRoadList = roadCoordinateService.processRoutePath(dazuZzerEndIntersectionId, dazuZzerStartIntersectionId, false);

        RoadSpeedVO vo = new RoadSpeedVO();

        switch (projectName) {
            case "Dayuan":
                vo.setIntersectionId(dayuanStartIntersectionId);
                vo.setConnectTo(dayuanEndIntersectionId);
                vo.setForwardRouteToConnected(dayuanForwardRoadList);
                vo.setReverseRouteToConnected(dayuanReverseRoadList);
                vo.setForwardSpeedStatus(GREEN);
                vo.setReverseSpeedStatus(GREEN);
        
                if(westSpeed > 0 && westSpeed < basicSpeed) {
                    vo.setForwardSpeedStatus(RED);
                }
        
                if(eastSpeed > 0 && eastSpeed < basicSpeed) {
                    vo.setReverseSpeedStatus(RED);
                }
                
                break;  

            case "Dazu":
                vo.setIntersectionId(dazuStartIntersectionId);
                vo.setConnectTo(dazuEndIntersectionId);
                vo.setForwardRouteToConnected(dazuForwardRoadList);
                vo.setReverseRouteToConnected(dazuReverseRoadList);
                vo.setForwardSpeedStatus(GREEN);
                vo.setReverseSpeedStatus(GREEN);
        
                if(dazuSpeedForward > 0 && dazuSpeedForward < basicSpeed) {
                    vo.setForwardSpeedStatus(RED);
                }
        
                if(dazuSpeedReverse > 0 && dazuSpeedReverse < basicSpeed) {
                    vo.setReverseSpeedStatus(RED);
                }
                
                break;

            case "Dazu_zzer":
                vo.setIntersectionId(dazuZzerStartIntersectionId);
                vo.setConnectTo(dazuZzerEndIntersectionId);
                vo.setForwardRouteToConnected(dazuZzerForwardRoadList);
                vo.setReverseRouteToConnected(dazuZzerReverseRoadList);
                vo.setForwardSpeedStatus(GREEN);
                vo.setReverseSpeedStatus(GREEN);
        
                if(dazuZzerSpeedForward > 0 && dazuZzerSpeedForward < basicSpeed) {
                    vo.setForwardSpeedStatus(RED);
                }
        
                if(dazuZzerSpeedReverse > 0 && dazuZzerSpeedReverse < basicSpeed) {
                    vo.setReverseSpeedStatus(RED);
                }
                
                break;
                
            default:
        }
        
        return vo;
    }

    /**
     * ??????????????? ?????????????????????
     * @return
     */
    public List<TimeStatisticVO<List<StatisticDataVO>>> getTimeStatistic() {
        return trafficFlowDataHelper.processTrafficTrend(null, null);
    }

    /**
     * ??????????????? ?????????????????????
     * @return
     */
    public List<TimeStatisticVO<List<StatisticDataVO>>> getCarDelay() {
        return trafficCarDelayDataHelper.processCarDelay(null, null);
    }

}
