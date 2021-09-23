package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.exception.ValidateException;
import com.wistron.swpc.wismarttrafficlight.vo.AreaStatisticVO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.wistron.swpc.wismarttrafficlight.entity.TrafficTrendGoogle;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import java.util.concurrent.TimeUnit;

@Service
public class GoolgeService {

    private static final double BIG_CAR_WEIGHT = 2.0;
    private static final double CAR_WEIGHT = 1.0;
    private static final double MOT_CAR_WEIGHT = 0.5;

    private static Logger logger = LoggerFactory.getLogger(AreaService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${license.google_api}")
    private String license;

    private ObjectMapper objectMapper = new ObjectMapper();

    private JsonMapper jsonMapper = new JsonMapper();

    private static final String TRAVEL_TIME_KEY = "travel_time_google";

    public AreaStatisticVO getAreaStatistic(Boolean schedule) {

        String travelTimeStr = stringRedisTemplate.opsForValue().get(TRAVEL_TIME_KEY);

        if (!StringUtils.isEmpty(travelTimeStr)) {
            try {
                return jsonMapper.readValue(travelTimeStr, AreaStatisticVO.class);
            } catch (JsonProcessingException e) {
                logger.error("[traffic] parse travelTimeStr error", e);
            }
        }

        AreaStatisticVO areaStatisticVO = new AreaStatisticVO();

        if(schedule) {
            int[] matrixData = queryOpenDataFromGoogle();
    
            //大園
            double eastSpeed = matrixData[0] <= 0 ? 0.0 : Double.valueOf(matrixData[8]) / 1000 * 3600 / matrixData[0];
            double westSpeed = matrixData[1] <= 0 ? 0.0 : Double.valueOf(matrixData[9]) / 1000 * 3600 / matrixData[1];
    
            //大竹
            double dazuZzerForwardSpeed = matrixData[2] <= 0 ? 0.0 : Double.valueOf(matrixData[10]) / 1000 * 3600 / matrixData[2]; //向南86->88 **10471
            double dazuZzerReverseSpeed = matrixData[3] <= 0 ? 0.0 : Double.valueOf(matrixData[11]) / 1000 * 3600 / matrixData[3]; //向北284->285 **
            double dazuNqrForwardSpeed = matrixData[4] <= 0 ? 0.0 : Double.valueOf(matrixData[12]) / 1000 * 3600 / matrixData[4]; //台31線上匝道286->87 **EA14814_EA14811
            double dazuNqrReverseSpeed = matrixData[5] <= 0 ? 0.0 : Double.valueOf(matrixData[13]) / 1000 * 3600 / matrixData[5]; //台31線下匝道288->84 + 85->287 **E729350_E245155 + E245156_EA14850
            double dazuNqrTurnZzerForwardSpeed = matrixData[6] <= 0 ? 0.0 : Double.valueOf(matrixData[14]) / 1000 * 3600 / matrixData[6]; //台31線左轉中正東路288->88 **E729350_EA04745
            double dazuNqrTurnZzerReverseSpeed = matrixData[7] <= 0 ? 0.0 : Double.valueOf(matrixData[15]) / 1000 * 3600 / matrixData[7]; //中正東路北向右轉往匝道87->284 **EA05100_EA14811
    
            //大園
            areaStatisticVO.setAvgCarSpeedEast(new BigDecimal(eastSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            areaStatisticVO.setAvgCarSpeedWest(new BigDecimal(westSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
    
            //大竹
            areaStatisticVO.setAvgCarSpeedDazuZzerForward(new BigDecimal(dazuZzerForwardSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            areaStatisticVO.setAvgCarSpeedDazuZzerReverse(new BigDecimal(dazuZzerReverseSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            areaStatisticVO.setAvgCarSpeedDazuNqrForward(new BigDecimal(dazuNqrForwardSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            areaStatisticVO.setAvgCarSpeedDazuNqrReverse(new BigDecimal(dazuNqrReverseSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            areaStatisticVO.setAvgCarSpeedDazuNqrTurnZzerForward(new BigDecimal(dazuNqrTurnZzerForwardSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
            areaStatisticVO.setAvgCarSpeedDazuNqrTurnZzerReverse(new BigDecimal(dazuNqrTurnZzerReverseSpeed).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
    
            //大園
            areaStatisticVO.setTravelTimeEast(matrixData[0]);
            areaStatisticVO.setTravelTimeWest(matrixData[1]);
    
            //大竹
            areaStatisticVO.setTravelTimeDazuZzerForward(matrixData[2]);
            areaStatisticVO.setTravelTimeDazuZzerReverse(matrixData[3]);
    
            areaStatisticVO.setTravelTimeDazuNqrForward(matrixData[4]);
            areaStatisticVO.setTravelTimeDazuNqrReverse(matrixData[5]);
            areaStatisticVO.setTravelTimeDazuNqrTurnZzerForward(matrixData[6]);
            areaStatisticVO.setTravelTimeDazuNqrTurnZzerReverse(matrixData[7]);
    
            try {
                stringRedisTemplate.opsForValue().set(TRAVEL_TIME_KEY, jsonMapper.writeValueAsString(areaStatisticVO), 2, TimeUnit.MINUTES);
            } catch (JsonProcessingException e) {
                logger.error("[traffic] cache travelTime error", e);
            }
        }

        else {
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date searchDate;
            Calendar calendar = Calendar.getInstance();

            try {
                searchDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
                calendar.setTime(searchDate);
                calendar.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().getTime().getHours());
                calendar.set(Calendar.MINUTE, Calendar.getInstance().getTime().getMinutes() - 4);
                calendar.set(Calendar.SECOND, Calendar.getInstance().getTime().getSeconds());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
      
            Date now = calendar.getTime();
    
            Query query = new Query();
            Criteria criteria = Criteria.where("create_at").gte(now);
            query.addCriteria(criteria);
            query.with(Sort.by("_id"));
            query.fields().include("speed_east").include("speed_west").include("travel_time_east").include("travel_time_west").include("travel_time_dazu_zzer_forward").include("travel_time_dazu_zzer_reverse").include("speed_dazu_zzer_forward").include("speed_dazu_zzer_reverse").include("travel_time_dazu_nqr_forward").include("travel_time_dazu_nqr_reverse").include("speed_dazu_nqr_forward").include("speed_dazu_nqr_reverse").include("travel_time_dazu_nqr_turn_zzer_forward").include("travel_time_dazu_nqr_turn_zzer_reverse").include("speed_dazu_nqr_turn_zzer_forward").include("speed_dazu_nqr_turn_zzer_reverse");
            List<TrafficTrendGoogle> queryData = mongoTemplate.find(query, TrafficTrendGoogle.class);
            TrafficTrendGoogle lastResult = queryData.get(queryData.size()-1);
    
            areaStatisticVO.setTravelTimeEast(lastResult.getTravelTimeEast());
            areaStatisticVO.setTravelTimeWest(lastResult.getTravelTimeWest());
            areaStatisticVO.setTravelTimeDazuNqrForward(lastResult.getTravelTimeDazuNqrForward());
            areaStatisticVO.setTravelTimeDazuNqrReverse(lastResult.getTravelTimeDazuNqrReverse());
            areaStatisticVO.setTravelTimeDazuZzerForward(lastResult.getTravelTimeDazuZzerForward());
            areaStatisticVO.setTravelTimeDazuZzerReverse(lastResult.getTravelTimeDazuZzerReverse());
            areaStatisticVO.setTravelTimeDazuNqrTurnZzerReverse(lastResult.getTravelTimeDazuNqrTurnZzerForward());
            areaStatisticVO.setTravelTimeDazuNqrTurnZzerReverse(lastResult.getTravelTimeDazuNqrTurnZzerReverse());

            //大園
            areaStatisticVO.setAvgCarSpeedEast(lastResult.getSpeedEast());
            areaStatisticVO.setAvgCarSpeedWest(lastResult.getSpeedWest());
    
            //大竹
            areaStatisticVO.setAvgCarSpeedDazuZzerForward(lastResult.getSpeedDazuZzerForward());
            areaStatisticVO.setAvgCarSpeedDazuZzerReverse(lastResult.getSpeedDazuZzerReverse());
            areaStatisticVO.setAvgCarSpeedDazuNqrForward(lastResult.getSpeedDazuNqrForward());
            areaStatisticVO.setAvgCarSpeedDazuNqrReverse(lastResult.getSpeedDazuNqrReverse());
            areaStatisticVO.setAvgCarSpeedDazuNqrTurnZzerForward(lastResult.getSpeedDazuNqrTurnZzerForward());
            areaStatisticVO.setAvgCarSpeedDazuNqrTurnZzerReverse(lastResult.getSpeedDazuNqrTurnZzerReverse());
        }   

        return areaStatisticVO;
    }
    

    public int[] queryOpenDataFromGoogle() {
        
        //东向旅行时间         
        String queryUrl0 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + "25.061513+121.203636"
        + "&destinations=" + "25.059216+121.211344" + "&departure_time=now&key=" + license;

        //西向旅行时间
        String queryUrl1 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + "25.059407+121.211279"
        + "&destinations=" + "25.061605+121.203673" + "&departure_time=now&key=" + license;

        //中正東路南向直行 
        String queryUrl2 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + "25.029731+121.230555"
        + "&destinations=" + "25.027446+121.230306" + "&departure_time=now&key=" + license;

        //中正東路北向直行
        String queryUrl3 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + "25.027203+121.230419"
        + "&destinations=" + "25.030091+121.230823" + "&departure_time=now&key=" + license;

        //台31線上匝道
        String queryUrl4 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + "25.023570+121.225600"
        + "&destinations=" + "25.028711+121.230823" + "&departure_time=now&key=" + license;

        //台31線下匝道​
        String queryUrl5 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + "25.031033+121.232869"
        + "&destinations=" + "25.024044+121.225726" + "&departure_time=now&key=" + license;

        //台31線左轉中正東路​
        String queryUrl6 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + "25.031033+121.232869"
        + "&destinations=" + "25.027446+121.230306" + "&departure_time=now&key=" + license;

        //中正東路北向右轉往匝道
        String queryUrl7 = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + "25.027203+121.230419"
        + "&destinations=" + "25.028711+121.230823" + "&departure_time=now&key=" + license;

        ResponseEntity<String> response0 = restTemplate.getForEntity(queryUrl0, String.class);
        ResponseEntity<String> response1 = restTemplate.getForEntity(queryUrl1, String.class);
        ResponseEntity<String> response2 = restTemplate.getForEntity(queryUrl2, String.class);
        ResponseEntity<String> response3 = restTemplate.getForEntity(queryUrl3, String.class);
        ResponseEntity<String> response4 = restTemplate.getForEntity(queryUrl4, String.class);
        ResponseEntity<String> response5 = restTemplate.getForEntity(queryUrl5, String.class);
        ResponseEntity<String> response6 = restTemplate.getForEntity(queryUrl6, String.class);
        ResponseEntity<String> response7 = restTemplate.getForEntity(queryUrl7, String.class);

        try {
            JsonNode rootNode0 = objectMapper.readTree(response0.getBody());
            JsonNode rootNode1 = objectMapper.readTree(response1.getBody());
            JsonNode rootNode2 = objectMapper.readTree(response2.getBody());
            JsonNode rootNode3 = objectMapper.readTree(response3.getBody());
            JsonNode rootNode4 = objectMapper.readTree(response4.getBody());
            JsonNode rootNode5 = objectMapper.readTree(response5.getBody());
            JsonNode rootNode6 = objectMapper.readTree(response6.getBody());
            JsonNode rootNode7 = objectMapper.readTree(response7.getBody());


            int duration0 = Integer.parseInt(rootNode0.get("rows").get(0).get("elements").get(0).get("duration_in_traffic").get("value").toString());
            int duration1 = Integer.parseInt(rootNode1.get("rows").get(0).get("elements").get(0).get("duration_in_traffic").get("value").toString());
            int duration2 = Integer.parseInt(rootNode2.get("rows").get(0).get("elements").get(0).get("duration_in_traffic").get("value").toString());
            int duration3 = Integer.parseInt(rootNode3.get("rows").get(0).get("elements").get(0).get("duration_in_traffic").get("value").toString());
            int duration4 = Integer.parseInt(rootNode4.get("rows").get(0).get("elements").get(0).get("duration_in_traffic").get("value").toString());
            int duration5 = Integer.parseInt(rootNode5.get("rows").get(0).get("elements").get(0).get("duration_in_traffic").get("value").toString());
            int duration6 = Integer.parseInt(rootNode6.get("rows").get(0).get("elements").get(0).get("duration_in_traffic").get("value").toString());
            int duration7 = Integer.parseInt(rootNode7.get("rows").get(0).get("elements").get(0).get("duration_in_traffic").get("value").toString());

            int distance0 = Integer.parseInt(rootNode0.get("rows").get(0).get("elements").get(0).get("distance").get("value").toString());
            int distance1 = Integer.parseInt(rootNode1.get("rows").get(0).get("elements").get(0).get("distance").get("value").toString());
            int distance2 = Integer.parseInt(rootNode2.get("rows").get(0).get("elements").get(0).get("distance").get("value").toString());
            int distance3 = Integer.parseInt(rootNode3.get("rows").get(0).get("elements").get(0).get("distance").get("value").toString());
            int distance4 = Integer.parseInt(rootNode4.get("rows").get(0).get("elements").get(0).get("distance").get("value").toString());
            int distance5 = Integer.parseInt(rootNode5.get("rows").get(0).get("elements").get(0).get("distance").get("value").toString());
            int distance6 = Integer.parseInt(rootNode6.get("rows").get(0).get("elements").get(0).get("distance").get("value").toString());
            int distance7 = Integer.parseInt(rootNode7.get("rows").get(0).get("elements").get(0).get("distance").get("value").toString());

            int[] result = {duration0,duration1,duration2,duration3,duration4,duration5,duration6,duration7,distance0,distance1,distance2,distance3,distance4,distance5,distance6,distance7};

            return result;
        } catch (Exception e) {
            throw new ValidateException(e.toString());
        }
    }

        /**
     * 处理流量趋势图
     * @param ipcMsg
     * @param searchDateStr
     * @return
     */
    public List<TimeStatisticVO> processTrafficTrend(String searchDateStr) {

        List<TimeStatisticVO> result = new ArrayList<>();

        Date searchDate;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (null == searchDateStr) {
                searchDateStr = dateFormat.format(Calendar.getInstance().getTime());
            }
            searchDate = dateFormat.parse(searchDateStr);
        } catch (ParseException e) {
            logger.error("[traffic] date format error, {}", searchDateStr);
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
        query.fields().include("speed_east").include("speed_west").include("travel_time_east").include("travel_time_west").include("travel_time_dazu_zzer_forward").include("travel_time_dazu_zzer_reverse").include("speed_dazu_zzer_forward").include("speed_dazu_zzer_reverse").include("travel_time_dazu_nqr_forward").include("travel_time_dazu_nqr_reverse").include("speed_dazu_nqr_forward").include("speed_dazu_nqr_reverse").include("travel_time_dazu_nqr_turn_zzer_forward").include("travel_time_dazu_nqr_turn_zzer_reverse").include("speed_dazu_nqr_turn_zzer_forward").include("speed_dazu_nqr_turn_zzer_reverse").include("pcu")
                .include("hour").include("car_delay");
        List<TrafficTrendGoogle> queryData = mongoTemplate.find(query, TrafficTrendGoogle.class);

        List<TrafficTrendGoogle> hourTrafficTread;
        TimeStatisticVO pointData;
        List<TrafficTrendGoogle> intersectionTrafficData;

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

            int hour = i;
            hourTrafficTread = queryData.stream().filter(item -> item.getHour() == hour).collect(Collectors.toList());


                intersectionTrafficData = hourTrafficTread.stream().collect(Collectors.toList());

                OptionalDouble speedEastTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedEast())
                        .mapToDouble(TrafficTrendGoogle::getSpeedEast).average();
                if (speedEastTemp.isPresent() && speedEastTemp.getAsDouble() > 0) {
                    speedEastCount++;
                    speedEast += speedEastTemp.getAsDouble();
                }

                OptionalDouble speedWestTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedWest())
                        .mapToDouble(TrafficTrendGoogle::getSpeedWest).average();
                if (speedWestTemp.isPresent() && speedWestTemp.getAsDouble() > 0) {
                    speedWestCount++;
                    speedWest += speedWestTemp.getAsDouble();
                }

                OptionalDouble travelTimeEastTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeEast())
                        .mapToDouble(TrafficTrendGoogle::getTravelTimeEast).average();
                if (travelTimeEastTemp.isPresent()&& travelTimeEastTemp.getAsDouble() > 0) {
                    travelTimeEastCount++;
                    travelTimeEast += travelTimeEastTemp.getAsDouble();
                }

                OptionalDouble travelTimeWestTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeWest())
                        .mapToDouble(TrafficTrendGoogle::getTravelTimeWest).average();
                if (travelTimeWestTemp.isPresent()&& travelTimeWestTemp.getAsDouble() > 0) {
                    travelTimeWestCount++;
                    travelTimeWest += travelTimeWestTemp.getAsDouble();
                }

                //大竹案 - 大竹中正東路直行
                OptionalDouble speedDazuZzerForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuZzerForward())
                        .mapToDouble(TrafficTrendGoogle::getSpeedDazuZzerForward).average();
                if (speedDazuZzerForwardTemp.isPresent()&& speedDazuZzerForwardTemp.getAsDouble() > 0) {
                    speedDazuZzerForwardCount++;
                    speedDazuZzerForward += speedDazuZzerForwardTemp.getAsDouble();
                }

                OptionalDouble speedDazuZzerReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuZzerReverse())
                        .mapToDouble(TrafficTrendGoogle::getSpeedDazuZzerReverse).average();
                if (speedDazuZzerReverseTemp.isPresent()&& speedDazuZzerReverseTemp.getAsDouble() > 0) {
                    speedDazuZzerReverseCount++;
                    speedDazuZzerReverse += speedDazuZzerReverseTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuZzerForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuZzerForward())
                        .mapToDouble(TrafficTrendGoogle::getTravelTimeDazuZzerForward).average();
                if (travelTimeDazuZzerForwardTemp.isPresent()&& travelTimeDazuZzerForwardTemp.getAsDouble() > 0) {
                    travelTimeDazuZzerForwardCount++;
                    travelTimeDazuZzerForward += travelTimeDazuZzerForwardTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuZzerReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuZzerReverse())
                        .mapToDouble(TrafficTrendGoogle::getTravelTimeDazuZzerReverse).average();
                if (travelTimeDazuZzerReverseTemp.isPresent()&& travelTimeDazuZzerReverseTemp.getAsDouble() > 0) {
                    travelTimeDazuZzerReverseCount++;
                    travelTimeDazuZzerReverse += travelTimeDazuZzerReverseTemp.getAsDouble();
                }

                //大竹案 - 台31線上下匝道
                OptionalDouble speedDazuNqrForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuNqrForward())
                        .mapToDouble(TrafficTrendGoogle::getSpeedDazuNqrForward).average();
                if (speedDazuNqrForwardTemp.isPresent()&& speedDazuNqrForwardTemp.getAsDouble() > 0) {
                    speedDazuNqrForwardCount++;
                    speedDazuNqrForward += speedDazuNqrForwardTemp.getAsDouble();
                }

                OptionalDouble speedDazuNqrReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuNqrReverse())
                        .mapToDouble(TrafficTrendGoogle::getSpeedDazuNqrReverse).average();
                if (speedDazuNqrReverseTemp.isPresent()&& speedDazuNqrReverseTemp.getAsDouble() > 0) {
                    speedDazuNqrReverseCount++;
                    speedDazuNqrReverse += speedDazuNqrReverseTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuNqrForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuNqrForward())
                        .mapToDouble(TrafficTrendGoogle::getTravelTimeDazuNqrForward).average();
                if (travelTimeDazuNqrForwardTemp.isPresent()&& travelTimeDazuNqrForwardTemp.getAsDouble() > 0) {
                    travelTimeDazuNqrForwardCount++;
                    travelTimeDazuNqrForward += travelTimeDazuNqrForwardTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuNqrReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuNqrReverse())
                        .mapToDouble(TrafficTrendGoogle::getTravelTimeDazuNqrReverse).average();
                if (travelTimeDazuNqrReverseTemp.isPresent()&& travelTimeDazuNqrReverseTemp.getAsDouble() > 0) {
                    travelTimeDazuNqrReverseCount++;
                    travelTimeDazuNqrReverse += travelTimeDazuNqrReverseTemp.getAsDouble();
                }

                //大竹案 - 台31線與中正東路正反向轉彎
                OptionalDouble speedDazuNqrTurnZzerForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuNqrTurnZzerForward())
                        .mapToDouble(TrafficTrendGoogle::getSpeedDazuNqrTurnZzerForward).average();
                if (speedDazuNqrTurnZzerForwardTemp.isPresent()&& speedDazuNqrTurnZzerForwardTemp.getAsDouble() > 0) {
                    speedDazuNqrTurnZzerForwardCount++;
                    speedDazuNqrTurnZzerForward += speedDazuNqrTurnZzerForwardTemp.getAsDouble();
                }

                OptionalDouble speedDazuNqrTurnZzerReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getSpeedDazuNqrTurnZzerReverse())
                        .mapToDouble(TrafficTrendGoogle::getSpeedDazuNqrTurnZzerReverse).average();
                if (speedDazuNqrTurnZzerReverseTemp.isPresent()&& speedDazuNqrTurnZzerReverseTemp.getAsDouble() > 0) {
                    speedDazuNqrTurnZzerReverseCount++;
                    speedDazuNqrTurnZzerReverse += speedDazuNqrTurnZzerReverseTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuNqrTurnZzerForwardTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuNqrTurnZzerForward())
                        .mapToDouble(TrafficTrendGoogle::getTravelTimeDazuNqrTurnZzerForward).average();
                if (travelTimeDazuNqrTurnZzerForwardTemp.isPresent()&& travelTimeDazuNqrTurnZzerForwardTemp.getAsDouble() > 0) {
                    travelTimeDazuNqrTurnZzerForwardCount++;
                    travelTimeDazuNqrTurnZzerForward += travelTimeDazuNqrTurnZzerForwardTemp.getAsDouble();
                }

                OptionalDouble travelTimeDazuNqrTurnZzerReverseTemp = intersectionTrafficData.stream().filter(item -> null != item.getTravelTimeDazuNqrTurnZzerReverse())
                        .mapToDouble(TrafficTrendGoogle::getTravelTimeDazuNqrTurnZzerReverse).average();
                if (travelTimeDazuNqrTurnZzerReverseTemp.isPresent()&& travelTimeDazuNqrTurnZzerReverseTemp.getAsDouble() > 0) {
                    travelTimeDazuNqrTurnZzerReverseCount++;
                    travelTimeDazuNqrTurnZzerReverse += travelTimeDazuNqrTurnZzerReverseTemp.getAsDouble();
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

    public void saveTrafficTrend() {
        TrafficTrendGoogle trafficTrend = new TrafficTrendGoogle();

        AreaStatisticVO areaStatistic = getAreaStatistic(true);
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

        Calendar calendar = Calendar.getInstance();
        trafficTrend.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        trafficTrend.setCreateAt(calendar.getTime());
        trafficTrend.setPcu(0.0);
        trafficTrend.setCarDelay(0.0);

        mongoTemplate.save(trafficTrend);
    }

    /**
     * 获取当天的 流量趋势图统计
     * @return
     */
    public List<TimeStatisticVO> getTimeStatistic() {
        return processTrafficTrend(null);
    }
}
