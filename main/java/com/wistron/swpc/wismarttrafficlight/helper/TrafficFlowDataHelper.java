package com.wistron.swpc.wismarttrafficlight.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wistron.swpc.wismarttrafficlight.dto.SubIntersectionFlowDTO;
import com.wistron.swpc.wismarttrafficlight.dto.SubIntersectionFlowItemDTO;
import com.wistron.swpc.wismarttrafficlight.dto.TrafficBoxMsgDTO;
import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import com.wistron.swpc.wismarttrafficlight.entity.SubIntersection;
import com.wistron.swpc.wismarttrafficlight.entity.TrafficTrend;
import com.wistron.swpc.wismarttrafficlight.mapper.IntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.SubIntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.SubPhaseMapper;
import com.wistron.swpc.wismarttrafficlight.service.AreaService;
import com.wistron.swpc.wismarttrafficlight.service.RoadCoordinateService;
import com.wistron.swpc.wismarttrafficlight.util.DateUtil;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.StorageSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubIntersectionFlowItemVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubIntersectionFlowVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubIntersectionVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TrafficFlowDataHelper {

    private final Logger logger = LoggerFactory.getLogger(TrafficFlowDataHelper.class);

    private static final double BIG_CAR_WEIGHT = 2.0;
    private static final double CAR_WEIGHT = 1.0;
    private static final double MOT_CAR_WEIGHT = 0.5;

    private static final String RED = "red";

    private static final String GREEN = "green";

    @Value("${ids.space_status_limit}")
    private int SPACE_STATUS_LIMIT;

    @Value("${ids.speed_status_limit}")
    private int SPEED_STATUS_LIMIT;

    public static final String TRAFFIC_FLOW_REDIS_PREFIX = "intersection_flow:";

    /**
     * 转向两最大的统计时间点 单位分钟
     */
    public static final int INTERSECTION_FLOW_MAX_TIME = 60;

    private JsonMapper jsonMapper = new JsonMapper();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SubPhaseMapper subPhaseDao;

    @Autowired
    private SubIntersectionMapper subIntersectionDao;

    @Autowired
    private IntersectionMapper intersectionDao;

    @Autowired
    private AreaService areaService;

    @Autowired
    private RoadCoordinateService roadCoordinateService;

    /**
     * 处理流量趋势图
     * @param ipcMsg
     * @param searchDateStr
     * @return
     */
    public List<TimeStatisticVO<List<StatisticDataVO>>> processTrafficTrend(TrafficBoxMsgDTO ipcMsg, String searchDateStr) {

        if (null != ipcMsg) {
            saveTrafficTrend(ipcMsg);
        }

        List<TimeStatisticVO<List<StatisticDataVO>>> result = new ArrayList<>();

        Date searchDate;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (null == searchDateStr) {
                searchDateStr = dateFormat.format(Calendar.getInstance().getTime());
            }
            searchDate = dateFormat.parse(searchDateStr);
        } catch (ParseException e) {
            logger.error("[traffic] date format erro, {}", searchDateStr);
            return result;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(searchDate);

        Date startDate = calendar.getTime();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endDate = calendar.getTime();

        Query query = new Query();
        Criteria criteria = Criteria.where("create_at").gte(startDate).lte(endDate);
        query.addCriteria(criteria);
        query.fields().include("intersection_id").include("speed_east").include("speed_west").include("travel_time_east").include("travel_time_west").include("travel_time_dazu_zzer_forward").include("travel_time_dazu_zzer_reverse").include("speed_dazu_zzer_forward").include("speed_dazu_zzer_reverse").include("travel_time_dazu_nqr_forward").include("travel_time_dazu_nqr_reverse").include("speed_dazu_nqr_forward").include("speed_dazu_nqr_reverse").include("travel_time_dazu_nqr_turn_zzer_forward").include("travel_time_dazu_nqr_turn_zzer_reverse").include("speed_dazu_nqr_turn_zzer_forward").include("speed_dazu_nqr_turn_zzer_reverse").include("pcu")
                .include("hour").include("car_delay");
        List<TrafficTrend> queryData = mongoTemplate.find(query, TrafficTrend.class);

        Map<String, String> intersectionIdNameMap = intersectionDao.selectAll().stream()
                .collect(Collectors.toMap(Intersection::getId, Intersection::getName, (key1, key2) -> key1));

        List<TrafficTrend> hourTrafficTread;
        TimeStatisticVO<List<StatisticDataVO>> pointData;
        List<StatisticDataVO> pointIntersectionData;
        List<TrafficTrend> intersectionTrafficData;
        StatisticDataVO intersectionItem;

        //大園
        double speedEast = 0;
        double speedWest = 0;
        int speedEastCount = 0;
        int speedWestCount = 0;
        double travelTimeEast = 0;
        double travelTimeWest = 0;
        int travelTimeEastCount = 0;
        int travelTimeWestCount = 0;

        //大竹
        double speedDazuZzerForward = 0;
        double speedDazuZzerReverse = 0;
        double speedDazuNqrForward = 0;
        double speedDazuNqrReverse = 0;
        double speedDazuNqrTurnZzerForward = 0;
        double speedDazuNqrTurnZzerReverse = 0;
        int speedDazuZzerForwardCount = 0;
        int speedDazuZzerReverseCount = 0;
        int speedDazuNqrForwardCount = 0;
        int speedDazuNqrReverseCount = 0;
        int speedDazuNqrTurnZzerForwardCount = 0;
        int speedDazuNqrTurnZzerReverseCount = 0;
        double travelTimeDazuZzerForward = 0;
        double travelTimeDazuZzerReverse = 0;
        double travelTimeDazuNqrForward = 0;
        double travelTimeDazuNqrReverse = 0;
        double travelTimeDazuNqrTurnZzerForward = 0;
        double travelTimeDazuNqrTurnZzerReverse = 0;
        int travelTimeDazuZzerForwardCount = 0;
        int travelTimeDazuZzerReverseCount = 0;
        int travelTimeDazuNqrForwardCount = 0;
        int travelTimeDazuNqrReverseCount = 0;
        int travelTimeDazuNqrTurnZzerForwardCount = 0;
        int travelTimeDazuNqrTurnZzerReverseCount = 0;

        double pcu = 0;
        double carDelay = 0;
        for (int i = 0; i < 24; i++) {

            //大園
            speedEast = 0;
            speedWest = 0;
            speedEastCount = 0;
            speedWestCount = 0;
            travelTimeEast = 0;
            travelTimeWest = 0;
            travelTimeEastCount = 0;
            travelTimeWestCount = 0;

            //大竹
            speedDazuZzerForward = 0;
            speedDazuZzerReverse = 0;
            speedDazuNqrForward = 0;
            speedDazuNqrReverse = 0;
            speedDazuNqrTurnZzerForward = 0;
            speedDazuNqrTurnZzerReverse = 0;
            speedDazuZzerForwardCount = 0;
            speedDazuZzerReverseCount = 0;
            speedDazuNqrForwardCount = 0;
            speedDazuNqrReverseCount = 0;
            speedDazuNqrTurnZzerForwardCount = 0;
            speedDazuNqrTurnZzerReverseCount = 0;
            travelTimeDazuZzerForward = 0;
            travelTimeDazuZzerReverse = 0;
            travelTimeDazuNqrForward = 0;
            travelTimeDazuNqrReverse = 0;
            travelTimeDazuNqrTurnZzerForward = 0;
            travelTimeDazuNqrTurnZzerReverse = 0;
            travelTimeDazuZzerForwardCount = 0;
            travelTimeDazuZzerReverseCount = 0;
            travelTimeDazuNqrForwardCount = 0;
            travelTimeDazuNqrReverseCount = 0;
            travelTimeDazuNqrTurnZzerForwardCount = 0;
            travelTimeDazuNqrTurnZzerReverseCount = 0;

            pointData = new TimeStatisticVO<>();
            pointData.setTimePoint(String.valueOf(i));

            pointIntersectionData = new ArrayList<>();
            pointData.setStatisticData(pointIntersectionData);

            int hour = i;
            hourTrafficTread = queryData.stream().filter(item -> item.getHour() == hour).collect(Collectors.toList());

            for (Map.Entry<String, String> idName : intersectionIdNameMap.entrySet()) {
                intersectionItem = new StatisticDataVO();
                intersectionItem.setIntersectionId(idName.getKey());
                intersectionItem.setIntersectionName(idName.getValue());

                intersectionTrafficData = hourTrafficTread.stream().filter(item -> idName.getKey().equals(item.getIntersectionId())).collect(Collectors.toList());

                pcu = intersectionTrafficData.stream().filter(item -> null != item.getPcu())
                        .mapToDouble(TrafficTrend::getPcu).sum();
                intersectionItem.setPcu(pcu);

                carDelay = intersectionTrafficData.stream().filter(item -> null != item.getCarDelay())
                        .mapToDouble(TrafficTrend::getCarDelay).sum() / intersectionTrafficData.size();;
                intersectionItem.setCarDelay(carDelay);

                OptionalDouble speedEastTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedEast())
                        .mapToDouble(TrafficTrend::getSpeedEast).average();
                if (speedEastTemp.isPresent() && speedEastTemp.getAsDouble() > 0) {
                    speedEastCount++;
                    speedEast += speedEastTemp.getAsDouble();
                }

                OptionalDouble speedWestTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedWest())
                        .mapToDouble(TrafficTrend::getSpeedWest).average();
                if (speedWestTemp.isPresent() && speedWestTemp.getAsDouble() > 0) {
                    speedWestCount++;
                    speedWest += speedWestTemp.getAsDouble();
                }

                OptionalDouble travelTimeEastTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeEast())
                        .mapToDouble(TrafficTrend::getTravelTimeEast).average();
                if (travelTimeEastTemp.isPresent()&& travelTimeEastTemp.getAsDouble() > 0) {
                    travelTimeEastCount++;
                    travelTimeEast += travelTimeEastTemp.getAsDouble();
                }

                OptionalDouble travelTimeWestTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeWest())
                        .mapToDouble(TrafficTrend::getTravelTimeWest).average();
                if (travelTimeWestTemp.isPresent()&& travelTimeWestTemp.getAsDouble() > 0) {
                    travelTimeWestCount++;
                    travelTimeWest += travelTimeWestTemp.getAsDouble();
                }

                //大竹案 - 大竹中正東路直行
                OptionalDouble speedDazuZzerForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuZzerForward())
                        .mapToDouble(TrafficTrend::getSpeedDazuZzerForward).average();
                if (speedDazuZzerForwardTemp.isPresent()&& speedDazuZzerForwardTemp.getAsDouble() > 0) {
                    speedDazuZzerForwardCount++;
                    speedDazuZzerForward += speedDazuZzerForwardTemp.getAsDouble();
                }

                OptionalDouble speedDazuZzerReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuZzerReverse())
                        .mapToDouble(TrafficTrend::getSpeedDazuZzerReverse).average();
                if (speedDazuZzerReverseTemp.isPresent()&& speedDazuZzerReverseTemp.getAsDouble() > 0) {
                    speedDazuZzerReverseCount++;
                    speedDazuZzerReverse += speedDazuZzerReverseTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuZzerForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuZzerForward())
                        .mapToDouble(TrafficTrend::getTravelTimeDazuZzerForward).average();
                if (travelTimeDazuZzerForwardTemp.isPresent()&& travelTimeDazuZzerForwardTemp.getAsDouble() > 0) {
                    travelTimeDazuZzerForwardCount++;
                    travelTimeDazuZzerForward += travelTimeDazuZzerForwardTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuZzerReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuZzerReverse())
                        .mapToDouble(TrafficTrend::getTravelTimeDazuZzerReverse).average();
                if (travelTimeDazuZzerReverseTemp.isPresent()&& travelTimeDazuZzerReverseTemp.getAsDouble() > 0) {
                    travelTimeDazuZzerReverseCount++;
                    travelTimeDazuZzerReverse += travelTimeDazuZzerReverseTemp.getAsDouble();
                }

                //大竹案 - 台31線上下匝道
                OptionalDouble speedDazuNqrForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuNqrForward())
                        .mapToDouble(TrafficTrend::getSpeedDazuNqrForward).average();
                if (speedDazuNqrForwardTemp.isPresent()&& speedDazuNqrForwardTemp.getAsDouble() > 0) {
                    speedDazuNqrForwardCount++;
                    speedDazuNqrForward += speedDazuNqrForwardTemp.getAsDouble();
                }

                OptionalDouble speedDazuNqrReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuNqrReverse())
                        .mapToDouble(TrafficTrend::getSpeedDazuNqrReverse).average();
                if (speedDazuNqrReverseTemp.isPresent()&& speedDazuNqrReverseTemp.getAsDouble() > 0) {
                    speedDazuNqrReverseCount++;
                    speedDazuNqrReverse += speedDazuNqrReverseTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuNqrForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuNqrForward())
                        .mapToDouble(TrafficTrend::getTravelTimeDazuNqrForward).average();
                if (travelTimeDazuNqrForwardTemp.isPresent()&& travelTimeDazuNqrForwardTemp.getAsDouble() > 0) {
                    travelTimeDazuNqrForwardCount++;
                    travelTimeDazuNqrForward += travelTimeDazuNqrForwardTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuNqrReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuNqrReverse())
                        .mapToDouble(TrafficTrend::getTravelTimeDazuNqrReverse).average();
                if (travelTimeDazuNqrReverseTemp.isPresent()&& travelTimeDazuNqrReverseTemp.getAsDouble() > 0) {
                    travelTimeDazuNqrReverseCount++;
                    travelTimeDazuNqrReverse += travelTimeDazuNqrReverseTemp.getAsDouble();
                }

                //大竹案 - 台31線與中正東路正反向轉彎
                OptionalDouble speedDazuNqrTurnZzerForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuNqrTurnZzerForward())
                        .mapToDouble(TrafficTrend::getSpeedDazuNqrTurnZzerForward).average();
                if (speedDazuNqrTurnZzerForwardTemp.isPresent()&& speedDazuNqrTurnZzerForwardTemp.getAsDouble() > 0) {
                    speedDazuNqrTurnZzerForwardCount++;
                    speedDazuNqrTurnZzerForward += speedDazuNqrTurnZzerForwardTemp.getAsDouble();
                }

                OptionalDouble speedDazuNqrTurnZzerReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuNqrTurnZzerReverse())
                        .mapToDouble(TrafficTrend::getSpeedDazuNqrTurnZzerReverse).average();
                if (speedDazuNqrTurnZzerReverseTemp.isPresent()&& speedDazuNqrTurnZzerReverseTemp.getAsDouble() > 0) {
                    speedDazuNqrTurnZzerReverseCount++;
                    speedDazuNqrTurnZzerReverse += speedDazuNqrTurnZzerReverseTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuNqrTurnZzerForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuNqrTurnZzerForward())
                        .mapToDouble(TrafficTrend::getTravelTimeDazuNqrTurnZzerForward).average();
                if (travelTimeDazuNqrTurnZzerForwardTemp.isPresent()&& travelTimeDazuNqrTurnZzerForwardTemp.getAsDouble() > 0) {
                    travelTimeDazuNqrTurnZzerForwardCount++;
                    travelTimeDazuNqrTurnZzerForward += travelTimeDazuNqrTurnZzerForwardTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuNqrTurnZzerReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuNqrTurnZzerReverse())
                        .mapToDouble(TrafficTrend::getTravelTimeDazuNqrTurnZzerReverse).average();
                if (travelTimeDazuNqrTurnZzerReverseTemp.isPresent()&& travelTimeDazuNqrTurnZzerReverseTemp.getAsDouble() > 0) {
                    travelTimeDazuNqrTurnZzerReverseCount++;
                    travelTimeDazuNqrTurnZzerReverse += travelTimeDazuNqrTurnZzerReverseTemp.getAsDouble();
                }


                pointIntersectionData.add(intersectionItem);
            }

            //大園
            double speedEastAvg = speedEastCount == 0 ? 0 : speedEast / speedEastCount;
            speedEastAvg = Math.round(speedEastAvg*10)/10.0;
            pointData.setSpeedEast(speedEastAvg);

            double speedWestAvg = speedWestCount == 0 ? 0 : speedWest / speedWestCount;
            speedWestAvg = Math.round(speedWestAvg*10)/10.0;
            pointData.setSpeedWest(speedWestAvg);

            travelTimeEast = travelTimeEastCount == 0 ? 0 : travelTimeEast / travelTimeEastCount;
            int travelTimeEastAvg = (int) Math.round(travelTimeEast);
            pointData.setTravelTimeEast(travelTimeEastAvg);

            travelTimeWest = travelTimeWestCount == 0 ? 0 : travelTimeWest / travelTimeWestCount;
            int travelTimeWestAvg = (int) Math.round(travelTimeWest);
            pointData.setTravelTimeWest(travelTimeWestAvg);

            //大竹案 - 大竹中正東路直行
            double speedDazuZzerForwardAvg = speedDazuZzerForwardCount == 0 ? 0 : speedDazuZzerForward / speedDazuZzerForwardCount;
            speedDazuZzerForwardAvg = Math.round(speedDazuZzerForwardAvg*10)/10.0;
            pointData.setSpeedDazuZzerForward(speedDazuZzerForwardAvg);

            double speedDazuZzerReverseAvg = speedDazuZzerReverseCount == 0 ? 0 : speedDazuZzerReverse / speedDazuZzerReverseCount;
            speedDazuZzerReverseAvg = Math.round(speedDazuZzerReverseAvg*10)/10.0;
            pointData.setSpeedDazuZzerReverse(speedDazuZzerReverseAvg);

            travelTimeDazuZzerForward = travelTimeDazuZzerForwardCount == 0 ? 0 : travelTimeDazuZzerForward / travelTimeDazuZzerForwardCount;
            int travelTimeDazuZzerForwardAvg = (int) Math.round(travelTimeDazuZzerForward);
            pointData.setTravelTimeDazuZzerForward(travelTimeDazuZzerForwardAvg);

            travelTimeDazuZzerReverse = travelTimeDazuZzerReverseCount == 0 ? 0 : travelTimeDazuZzerReverse / travelTimeDazuZzerReverseCount;
            int travelTimeDazuZzerReverseAvg = (int) Math.round(travelTimeDazuZzerReverse);
            pointData.setTravelTimeDazuZzerReverse(travelTimeDazuZzerReverseAvg);

            //大竹案 - 台31線上下匝道
            double speedDazuNqrForwardAvg = speedDazuNqrForwardCount == 0 ? 0 : speedDazuNqrForward / speedDazuNqrForwardCount;
            speedDazuNqrForwardAvg = Math.round(speedDazuNqrForwardAvg*10)/10.0;
            pointData.setSpeedDazuNqrForward(speedDazuNqrForwardAvg);

            double speedDazuNqrReverseAvg = speedDazuNqrReverseCount == 0 ? 0 : speedDazuNqrReverse / speedDazuNqrReverseCount;
            speedDazuNqrReverseAvg = Math.round(speedDazuNqrReverseAvg*10)/10.0;
            pointData.setSpeedDazuNqrReverse(speedDazuNqrReverseAvg);

            travelTimeDazuNqrForward = travelTimeDazuNqrForwardCount == 0 ? 0 : travelTimeDazuNqrForward / travelTimeDazuNqrForwardCount;
            int travelTimeDazuNqrForwardAvg = (int) Math.round(travelTimeDazuNqrForward);
            pointData.setTravelTimeDazuNqrForward(travelTimeDazuNqrForwardAvg);

            travelTimeDazuNqrReverse = travelTimeDazuNqrReverseCount == 0 ? 0 : travelTimeDazuNqrReverse / travelTimeDazuNqrReverseCount;
            int travelTimeDazuNqrReverseAvg = (int) Math.round(travelTimeDazuNqrReverse);
            pointData.setTravelTimeDazuNqrReverse(travelTimeDazuNqrReverseAvg);

            //大竹案 - 台31線與中正東路正反向轉彎
            double speedDazuNqrTurnZzerForwardAvg = speedDazuNqrTurnZzerForwardCount == 0 ? 0 : speedDazuNqrTurnZzerForward / speedDazuNqrTurnZzerForwardCount;
            speedDazuNqrTurnZzerForwardAvg = Math.round(speedDazuNqrTurnZzerForwardAvg*10)/10.0;
            pointData.setSpeedDazuNqrTurnZzerForward(speedDazuNqrTurnZzerForwardAvg);

            double speedDazuNqrTurnZzerReverseAvg = speedDazuNqrTurnZzerReverseCount == 0 ? 0 : speedDazuNqrTurnZzerReverse / speedDazuNqrTurnZzerReverseCount;
            speedDazuNqrTurnZzerReverseAvg = Math.round(speedDazuNqrTurnZzerReverseAvg*10)/10.0;
            pointData.setSpeedDazuNqrTurnZzerReverse(speedDazuNqrTurnZzerReverseAvg);

            travelTimeDazuNqrTurnZzerForward = travelTimeDazuNqrTurnZzerForwardCount == 0 ? 0 : travelTimeDazuNqrTurnZzerForward / travelTimeDazuNqrTurnZzerForwardCount;
            int travelTimeDazuNqrTurnZzerForwardAvg = (int) Math.round(travelTimeDazuNqrTurnZzerForward);
            pointData.setTravelTimeDazuNqrTurnZzerForward(travelTimeDazuNqrTurnZzerForwardAvg);

            travelTimeDazuNqrTurnZzerReverse = travelTimeDazuNqrTurnZzerReverseCount == 0 ? 0 : travelTimeDazuNqrTurnZzerReverse / travelTimeDazuNqrTurnZzerReverseCount;
            int travelTimeDazuNqrTurnZzerReverseAvg = (int) Math.round(travelTimeDazuNqrTurnZzerReverse);
            pointData.setTravelTimeDazuNqrTurnZzerReverse(travelTimeDazuNqrTurnZzerReverseAvg);

            result.add(pointData);
        }

        return result;
    }

    private void saveTrafficTrend(TrafficBoxMsgDTO ipcMsg) {
        List<SubIntersectionFlowDTO> msgData;
        try {
            msgData = jsonMapper.readValue(
                    jsonMapper.writeValueAsString(ipcMsg.getMsg()),
                    new TypeReference<List<SubIntersectionFlowDTO>>() {
                    });
            TrafficTrend trafficTrend = new TrafficTrend();
            trafficTrend.setIntersectionId(ipcMsg.getIntersectionId());

            double pcu = 0d;
            for (SubIntersectionFlowDTO flowItem : msgData) {
                for (SubIntersectionFlowItemDTO subDataItem : flowItem.getSubData()) {
                    pcu += subDataItem.getFlowOutBigCar() * BIG_CAR_WEIGHT + subDataItem.getFlowOutCar() * CAR_WEIGHT
                            + subDataItem.getFlowOutMotCar() * MOT_CAR_WEIGHT;
                }
            }

            //這邊的cardelay已淘汰
            double carDelay = 0d;
            for (SubIntersectionFlowDTO flowItem : msgData) {
                carDelay += (flowItem.getBigCarDelay() * BIG_CAR_WEIGHT + flowItem.getCarDelay() * CAR_WEIGHT + flowItem.getMotorDelay() * MOT_CAR_WEIGHT > 5000 ? 0 : flowItem.getBigCarDelay() * BIG_CAR_WEIGHT + flowItem.getCarDelay() * CAR_WEIGHT + flowItem.getMotorDelay() * MOT_CAR_WEIGHT);
            }

            AreaStatisticVO areaStatistic = areaService.getAreaStatistic();
            if (null != areaStatistic) {
                //大園
                trafficTrend.setSpeedEast(areaStatistic.getAvgCarSpeedEast());
                trafficTrend.setSpeedWest(areaStatistic.getAvgCarSpeedWest());
                trafficTrend.setTravelTimeEast(areaStatistic.getTravelTimeEast());
                trafficTrend.setTravelTimeWest(areaStatistic.getTravelTimeWest());

                //大竹案 - 大竹中正東路直行
                trafficTrend.setSpeedDazuZzerForward(areaStatistic.getAvgCarSpeedDazuZzerForward());
                trafficTrend.setSpeedDazuZzerReverse(areaStatistic.getAvgCarSpeedDazuZzerReverse());
                trafficTrend.setTravelTimeDazuZzerForward(areaStatistic.getTravelTimeDazuZzerForward());
                trafficTrend.setTravelTimeDazuZzerReverse(areaStatistic.getTravelTimeDazuZzerReverse());

                //大竹案 - 台31線上下匝道
                trafficTrend.setSpeedDazuNqrForward(areaStatistic.getAvgCarSpeedDazuNqrForward());
                trafficTrend.setSpeedDazuNqrReverse(areaStatistic.getAvgCarSpeedDazuNqrReverse());
                trafficTrend.setTravelTimeDazuNqrForward(areaStatistic.getTravelTimeDazuNqrForward());
                trafficTrend.setTravelTimeDazuNqrReverse(areaStatistic.getTravelTimeDazuNqrReverse());
                //大竹案 - 台31線與中正東路正反向轉彎
                trafficTrend.setSpeedDazuNqrTurnZzerForward(areaStatistic.getAvgCarSpeedDazuNqrTurnZzerForward());
                trafficTrend.setSpeedDazuNqrTurnZzerReverse(areaStatistic.getAvgCarSpeedDazuNqrTurnZzerReverse());
                trafficTrend.setTravelTimeDazuNqrTurnZzerForward(areaStatistic.getTravelTimeDazuNqrTurnZzerForward());
                trafficTrend.setTravelTimeDazuNqrTurnZzerReverse(areaStatistic.getTravelTimeDazuNqrTurnZzerReverse());

            }

            trafficTrend.setPcu(pcu);
            trafficTrend.setCarDelay(carDelay);

            Calendar calendar = Calendar.getInstance();
            trafficTrend.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            trafficTrend.setCreateAt(calendar.getTime());
            trafficTrend.setIpcMsg(jsonMapper.writeValueAsString(ipcMsg));

            mongoTemplate.save(trafficTrend);

        } catch (JsonProcessingException e) {
            logger.error("[traffic] ipc msg format error: ", e);
        }
    }

    /**
     * 处理车速状态，存车空间状态
     * @param intersectionId
     * @param subIntersectionList
     * @return
     */
    public StorageSpeedVO processStorageSpeedData(String intersectionId, List<SubIntersection> subIntersectionList) {

        long currentSeconds = DateUtil.getUtcMillis() / 1000;

        String redisKey = TrafficFlowDataHelper.TRAFFIC_FLOW_REDIS_PREFIX + intersectionId;
        Set<String> flowData = stringRedisTemplate.opsForZSet()
                .rangeByScore(redisKey, currentSeconds - 60, currentSeconds);

        Map<String, SubIntersectionFlowDTO> msgMap = new HashMap<>();

        if (!CollectionUtils.isEmpty(flowData)) {
            List<String> flowDataList = new ArrayList<>(flowData);
            String latestData = flowDataList.get(flowDataList.size() -1);

            TrafficBoxMsgDTO trafficBoxMsgDTO;
            List<SubIntersectionFlowDTO> msgData;
            try {
                trafficBoxMsgDTO = jsonMapper.readValue(latestData, TrafficBoxMsgDTO.class);
                msgData = jsonMapper.readValue(
                        jsonMapper.writeValueAsString(trafficBoxMsgDTO.getMsg()),
                        new TypeReference<List<SubIntersectionFlowDTO>>() {
                        });
                msgMap = msgData.stream()
                        .collect(Collectors.toMap(SubIntersectionFlowDTO::getSubIntersectionId, item -> item, (key1, key2) -> key1));
            } catch (JsonProcessingException e) {
                logger.error("[traffic] ipc msg format error: ", e);
            }
        }

        StorageSpeedVO storageSpeedVO = new StorageSpeedVO();
        List<SubIntersectionVO> subIntersectionVOList = new ArrayList<>();
        storageSpeedVO.setSubIntersections(subIntersectionVOList);

        SubIntersectionFlowDTO flowItem;
        String spaceStatus = GREEN;
        String speedStatus = GREEN;
        for (SubIntersection subIntersection : subIntersectionList) {
            flowItem = msgMap.get(subIntersection.getId());
            if (null != flowItem) {
                spaceStatus = flowItem.getCarStorageStatus() > SPACE_STATUS_LIMIT ? RED : GREEN;
                speedStatus = flowItem.getAvgSpeed() > SPEED_STATUS_LIMIT ? GREEN : RED;
            }
            SubIntersectionVO subIntersectionVO = new SubIntersectionVO();
            subIntersectionVO.setName(subIntersection.getName());
            subIntersectionVO.setSubIntersectionDirection(subIntersection.getDirection());
            subIntersectionVO.setSubIntersectionId(subIntersection.getId());
            subIntersectionVO.setFlowInStorageSpace(GREEN);
            subIntersectionVO.setFlowOutStorageSpace(spaceStatus);
            subIntersectionVO.setFlowInSpeed(GREEN);
            subIntersectionVO.setFlowOutSpeed(speedStatus);
            subIntersectionVO.setConnectTo(subIntersection.getConnectTo());
            if (!StringUtils.isEmpty(subIntersectionVO.getConnectTo())) {
                subIntersectionVO.setConnectedRoadPoint(
                        roadCoordinateService.processRoutePath(intersectionId, subIntersectionVO.getConnectTo(), false));
            }
            subIntersectionVOList.add(subIntersectionVO);
        }

        return storageSpeedVO;
    }


    /**
     * 处理路口转向流量
     * @param intersectionId
     * @param min
     * @param subIntersectionIdMap
     * @return
     */
    public List<SubIntersectionFlowVO> processTrafficFlow(String intersectionId,
                                                          int min, Map<String, SubIntersection> subIntersectionIdMap) {
        long currentSeconds = DateUtil.getUtcMillis() / 1000;

        // 1. get intersection_flow data from redis
        String redisKey = TRAFFIC_FLOW_REDIS_PREFIX + intersectionId;
        Set<String> flowData = stringRedisTemplate.opsForZSet()
                .rangeByScore(redisKey, currentSeconds - min * 60, currentSeconds);

        if (CollectionUtils.isEmpty(flowData)) {
            return Collections.emptyList();
        }

        // 2.1 transform flow data
        List<SubIntersectionFlowDTO> allFlow = new ArrayList<>();
        TrafficBoxMsgDTO trafficBoxMsgDTO;
        List<SubIntersectionFlowDTO> msgData;
        for (String flowItem : flowData) {
            try {
                trafficBoxMsgDTO = jsonMapper.readValue(flowItem, TrafficBoxMsgDTO.class);
                msgData = jsonMapper.readValue(
                        jsonMapper.writeValueAsString(trafficBoxMsgDTO.getMsg()),
                        new TypeReference<List<SubIntersectionFlowDTO>>() {
                        });
                allFlow.addAll(msgData);
            } catch (Exception ex) {
                logger.error("[traffic] ipc msg format error: ", ex);
            }
        }

        // 2.2 calculate pcu
        List<SubIntersectionFlowVO> subIntersectionFlow = calculatePcu(allFlow, subIntersectionIdMap);

        // 2.3 外层 缺少的路口进行补零
        subIntersectionFlow = processSubIntersection(subIntersectionFlow, subIntersectionIdMap);

        // 2.4 内层 subData, 计算总pcu, 转向率, T 型路口 connectedId 缺少补零
        subIntersectionFlow = processSubData(subIntersectionFlow, subIntersectionIdMap);

        // 2.5 T 型路口外层数据额外处理，外层subIntersectionId 为 1, 2, 3, 4, 缺少则补充
        subIntersectionFlow = processIntersectionOfT(subIntersectionFlow);

        // 2.6 移除过期数据
        long expireTime = currentSeconds - INTERSECTION_FLOW_MAX_TIME * 60;
        stringRedisTemplate.opsForZSet().removeRangeByScore(redisKey, 0, expireTime);

        return subIntersectionFlow;
    }

    /**
     * T 型路口补全 subIntersectionId : 1, 2, 3, 4
     * @param subIntersectionFlow
     * @return
     */
    private List<SubIntersectionFlowVO> processIntersectionOfT(List<SubIntersectionFlowVO> subIntersectionFlow) {
        Set<String> currentSubIntersectionId = subIntersectionFlow.stream()
                .map(SubIntersectionFlowVO::getSubIntersectionId).collect(Collectors.toSet());
        Set<String> allId = Stream.of("1", "2", "3", "4").collect(Collectors.toSet());
        allId.removeAll(currentSubIntersectionId);

        SubIntersectionFlowVO flowItem;
        for (String subIntersectionId : allId) {
            flowItem = new SubIntersectionFlowVO();
            flowItem.setSubIntersectionName("");
            flowItem.setSubIntersectionDirection("");
            flowItem.setSubIntersectionId(subIntersectionId);
            flowItem.setFlowInTotal(0);
            flowItem.setFlowOutTotal(0);
            flowItem.setSubData(Collections.EMPTY_LIST);
            subIntersectionFlow.add(flowItem);
        }

        return subIntersectionFlow;
    }

    /**
     * 计算 pcu
     * @param allFlow
     * @param subIntersectionIdMap
     * @return
     */
    private List<SubIntersectionFlowVO> calculatePcu(List<SubIntersectionFlowDTO> allFlow,
                                                     Map<String, SubIntersection> subIntersectionIdMap) {

        List<SubIntersectionFlowVO> subIntersectionFlow = new ArrayList<>();

        Map<String, List<SubIntersectionFlowDTO>> intersectionFlow =  allFlow.stream()
                .collect(Collectors.groupingBy(SubIntersectionFlowDTO::getSubIntersectionId));

        SubIntersection subIntersection;
        for (Map.Entry<String, List<SubIntersectionFlowDTO>> entry : intersectionFlow.entrySet()) {
            SubIntersectionFlowVO subIntersectionFlowVO = new SubIntersectionFlowVO();
            subIntersectionFlowVO.setSubIntersectionId(entry.getKey());
            subIntersectionFlowVO.setHasVehicleDelector(true); //有轉向資料，表示有VD裝置

            subIntersection = subIntersectionIdMap.get(entry.getKey());
            if (null != subIntersection) {
                subIntersectionFlowVO.setSubIntersectionName(subIntersection.getName());
                subIntersectionFlowVO.setSubIntersectionDirection(subIntersection.getDirection());
            }

            List<SubIntersectionFlowItemVO> subDataVOList = new ArrayList<>();
            subIntersectionFlowVO.setSubData(subDataVOList);
            subIntersectionFlow.add(subIntersectionFlowVO);

            List<SubIntersectionFlowItemDTO> subData = new ArrayList<>();
            entry.getValue().forEach(subItem -> subData.addAll(subItem.getSubData()));

            Map<String, List<SubIntersectionFlowItemDTO>> connectedIdMap = subData.stream()
                    .collect(Collectors.groupingBy(SubIntersectionFlowItemDTO::getConnectedSubId));

            for (Map.Entry<String, List<SubIntersectionFlowItemDTO>> connectedEntry : connectedIdMap.entrySet()) {
                SubIntersectionFlowItemVO itemVO = new SubIntersectionFlowItemVO();
                itemVO.setConnectedSubId(connectedEntry.getKey());

                subIntersection = subIntersectionIdMap.get(connectedEntry.getKey());
                if (null != subIntersection) {
                    itemVO.setConnectedSubName(subIntersection.getName());
                    itemVO.setConnectedSubDirection(subIntersection.getDirection());
                }

                connectedEntry.getValue().forEach(itemDTO -> {
                    itemVO.setFlowOutBigCar(itemVO.getFlowOutBigCar() + itemDTO.getFlowOutBigCar() * BIG_CAR_WEIGHT);
                    itemVO.setFlowOutCar(itemVO.getFlowOutCar() + itemDTO.getFlowOutCar() * CAR_WEIGHT);
                    itemVO.setFlowOutMotCar(itemVO.getFlowOutMotCar() + itemDTO.getFlowOutMotCar() * MOT_CAR_WEIGHT);
                });
                subDataVOList.add(itemVO);
            }
        }

        return subIntersectionFlow;
    }

    /**
     * 处理 子路口 缺少的情况，缺少补零
     * @param subIntersectionFlow
     * @param subIntersectionIdMap
     * @return
     */
    private List<SubIntersectionFlowVO> processSubIntersection(List<SubIntersectionFlowVO> subIntersectionFlow,
                                                                   Map<String, SubIntersection> subIntersectionIdMap) {
        Set<String> allSubIdSet;
        Set<String> currSubIdSet;

        if (subIntersectionFlow.size() < subIntersectionIdMap.keySet().size()) {
            allSubIdSet = new HashSet<>(subIntersectionIdMap.keySet());
            currSubIdSet = subIntersectionFlow.stream().map(SubIntersectionFlowVO::getSubIntersectionId).collect(Collectors.toSet());
            allSubIdSet.removeAll(currSubIdSet);
            SubIntersection subIntersection;
            for (String subId : allSubIdSet) {
                SubIntersectionFlowVO vo = new SubIntersectionFlowVO();
                vo.setSubIntersectionId(subId);
                vo.setHasVehicleDelector(false);//無轉向資料，表示無VD裝置
                subIntersection = subIntersectionIdMap.get(subId);
                if (null != subIntersection) {
                    vo.setSubIntersectionName(subIntersection.getName());
                    vo.setSubIntersectionDirection(subIntersection.getDirection());
                }
                vo.setSubData(new ArrayList<>());
                for (String connectedId : subIntersectionIdMap.keySet()) {
                    if (subId.equals(connectedId)) {
                        continue;
                    }
                    SubIntersectionFlowItemVO itemVo = new SubIntersectionFlowItemVO();
                    itemVo.setConnectedSubId(connectedId);
                    subIntersection = subIntersectionIdMap.get(connectedId);
                    if (null != subIntersection) {
                        itemVo.setConnectedSubName(subIntersection.getName());
                        itemVo.setConnectedSubDirection(subIntersection.getDirection());
                    }
                    vo.getSubData().add(itemVo);
                }
                subIntersectionFlow.add(vo);
            }
        }

        return subIntersectionFlow;
    }

    /**
     * 内层 subData, 计算总pcu, 转向率, T 型路口 connectedId 缺少补零
     * @param subIntersectionFlow
     * @param subIntersectionIdMap
     * @return
     */
    private List<SubIntersectionFlowVO> processSubData(List<SubIntersectionFlowVO> subIntersectionFlow,
                                                      Map<String, SubIntersection> subIntersectionIdMap) {

        double flowOutTotal;
        double flowOutAvgOp;
        double flowInTotal;
        double flowInCount;

        for (SubIntersectionFlowVO item : subIntersectionFlow) {
            // 1. 补全 subData
            Set<String> allSubId;

            Set<String> currentConnectedId = item.getSubData().stream()
                    .map(SubIntersectionFlowItemVO::getConnectedSubId)
                    .collect(Collectors.toSet());

            // T 型路口, 缺少栏位补零，最少是 3
            if (subIntersectionIdMap.keySet().size() - 1 < 3) {
                allSubId = Stream.of("1", "2", "3", "4").collect(Collectors.toSet());
            } else {  // 其他类型路口，subData 缺少补零
                allSubId = new HashSet<>(subIntersectionIdMap.keySet());
            }

            allSubId.remove(item.getSubIntersectionId());
            allSubId.removeAll(currentConnectedId);

            // 缺少的 connected_id 补零
            SubIntersection subIntersection;
            for (String id : allSubId) {
                SubIntersectionFlowItemVO vo = new SubIntersectionFlowItemVO();
                vo.setConnectedSubId(id);
                subIntersection = subIntersectionIdMap.get(id);
                if (null != subIntersection) {
                    vo.setConnectedSubName(subIntersection.getName());
                    vo.setConnectedSubDirection(subIntersection.getDirection());
                }
                item.getSubData().add(vo);
            }
        }

        for (SubIntersectionFlowVO item : subIntersectionFlow) {

            // 2. 计算 flowOutTotal
            flowOutTotal = item.getSubData().stream().mapToDouble(subItem -> subItem.getFlowOutBigCar() + subItem.getFlowOutCar()
                    + subItem.getFlowOutMotCar()).sum();
            item.setFlowOutTotal(flowOutTotal);

            // 3. 计算 flowInCount, flowOutAvgOp
            for (SubIntersectionFlowItemVO subItem : item.getSubData()) {

                if (flowOutTotal == 0) {
                    flowOutAvgOp = 0;
                } else {
                    flowOutAvgOp = (subItem.getFlowOutBigCar() + subItem.getFlowOutCar()
                            + subItem.getFlowOutMotCar()) / flowOutTotal;
                }

                subItem.setFlowOutAvgOp(
                        new BigDecimal(flowOutAvgOp).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue());

                flowInCount = subIntersectionFlow.stream().filter(flowItem -> subItem.getConnectedSubId().equals(flowItem.getSubIntersectionId()))
                        .mapToDouble(
                                flowItem -> flowItem.getSubData().stream()
                                        .filter(subDataItem -> item.getSubIntersectionId().equals(subDataItem.getConnectedSubId()))
                                        .mapToDouble(subDataItem -> subDataItem.getFlowOutBigCar() + subDataItem.getFlowOutCar() + subDataItem.getFlowOutMotCar()).sum()
                        ).sum();

                subItem.setFlowInCount(flowInCount);
            }

            // 4. 计算 flowInTotal
            flowInTotal = item.getSubData().stream().mapToDouble(SubIntersectionFlowItemVO::getFlowInCount).sum();
            item.setFlowInTotal(flowInTotal);
        }

        return subIntersectionFlow;
    }

}
