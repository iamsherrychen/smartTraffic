package com.wistron.swpc.wismarttrafficlight.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wistron.swpc.wismarttrafficlight.dto.SubIntersectionFlowDTO;
import com.wistron.swpc.wismarttrafficlight.dto.TrafficBoxMsgDTO;
import com.wistron.swpc.wismarttrafficlight.entity.CarDelay;
import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import com.wistron.swpc.wismarttrafficlight.mapper.IntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TrafficCarDelayDataHelper {

    private final Logger logger = LoggerFactory.getLogger(TrafficCarDelayDataHelper.class);

    private static final double BIG_CAR_WEIGHT = 2.0;
    private static final double CAR_WEIGHT = 1.0;
    private static final double MOT_CAR_WEIGHT = 0.5;

    public static final String CAR_DELAY_REDIS_PREFIX = "intersection_car_delay:";

    private JsonMapper jsonMapper = new JsonMapper();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IntersectionMapper intersectionDao;

    /**
     * 處理停等圖
     * @param ipcMsg
     * @param searchDateStr
     * @return
     */
    public List<TimeStatisticVO<List<StatisticDataVO>>> processCarDelay(TrafficBoxMsgDTO ipcMsg, String searchDateStr) {

        if (null != ipcMsg) {
            saveCarDelay(ipcMsg);
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
        query.fields().include("intersection_id").include("hour").include("car_delay");
        List<CarDelay> queryData = mongoTemplate.find(query, CarDelay.class);

        Map<String, String> intersectionIdNameMap = intersectionDao.selectAll().stream()
                .collect(Collectors.toMap(Intersection::getId, Intersection::getName, (key1, key2) -> key1));

        List<CarDelay> hourTrafficTread;
        TimeStatisticVO<List<StatisticDataVO>> pointData;
        List<StatisticDataVO> pointIntersectionData;
        List<CarDelay> intersectionTrafficData;
        StatisticDataVO intersectionItem;

        double carDelay = 0;
        for (int i = 0; i < 24; i++) {
            
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

                carDelay = intersectionTrafficData.stream().filter(item -> null != item.getCarDelay()).mapToDouble(CarDelay::getCarDelay).sum() / intersectionTrafficData.size() > 5000 ? 0 : intersectionTrafficData.stream().filter(item -> null != item.getCarDelay()).mapToDouble(CarDelay::getCarDelay).sum() / intersectionTrafficData.size();
                intersectionItem.setCarDelay(carDelay);

                pointIntersectionData.add(intersectionItem);
            }

            result.add(pointData);
        }

        return result;
    }

    private void saveCarDelay(TrafficBoxMsgDTO ipcMsg) {
        List<SubIntersectionFlowDTO> msgData;
        try {
            msgData = jsonMapper.readValue(
                    jsonMapper.writeValueAsString(ipcMsg.getMsg()),
                    new TypeReference<List<SubIntersectionFlowDTO>>() {
                    });
            CarDelay trafficTrend = new CarDelay();
            trafficTrend.setIntersectionId(ipcMsg.getIntersectionId());

            double carDelay = 0d;
            for (SubIntersectionFlowDTO flowItem : msgData) {
                carDelay += (flowItem.getBigCarDelay() * BIG_CAR_WEIGHT + flowItem.getCarDelay() * CAR_WEIGHT + flowItem.getMotorDelay() * MOT_CAR_WEIGHT);
            }

            trafficTrend.setCarDelay(carDelay);

            Calendar calendar = Calendar.getInstance();
            trafficTrend.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            trafficTrend.setCreateAt(calendar.getTime());
            trafficTrend.setIpcMsg(jsonMapper.writeValueAsString(ipcMsg));

            mongoTemplate.save(trafficTrend);

        } catch (JsonProcessingException e) {
            logger.error("[traffic] ipc msg (car delay) format error: ", e);
        }
    }
}