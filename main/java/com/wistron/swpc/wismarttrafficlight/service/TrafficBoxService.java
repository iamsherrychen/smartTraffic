package com.wistron.swpc.wismarttrafficlight.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.microsoft.applicationinsights.core.dependencies.javaxannotation.meta.When;
import com.wistron.swpc.wismarttrafficlight.constant.ParameterKeyConst;
import com.wistron.swpc.wismarttrafficlight.constant.SubPhaseEnum;
import com.wistron.swpc.wismarttrafficlight.constant.SubPhaseStepEnum;
import com.wistron.swpc.wismarttrafficlight.constant.TrafficBoxConst;
import com.wistron.swpc.wismarttrafficlight.dto.ControlStrategyDTO;
import com.wistron.swpc.wismarttrafficlight.dto.TrafficBoxMsgDTO;
import com.wistron.swpc.wismarttrafficlight.entity.SubIntersection;
import com.wistron.swpc.wismarttrafficlight.entity.SubPhase;
import com.wistron.swpc.wismarttrafficlight.entity.TimePeriod;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficBoxCmdHelper;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficCarDelayDataHelper;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.helper.WebSocketHelper;
import com.wistron.swpc.wismarttrafficlight.mapper.SubIntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.SubPhaseMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.TimePeriodMapper;
import com.wistron.swpc.wismarttrafficlight.util.DateUtil;
import com.wistron.swpc.wismarttrafficlight.vo.StatisticDataVO;
import com.wistron.swpc.wismarttrafficlight.vo.StorageSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubIntersectionFlowVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubPhaseDetailVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubPhaseVO;
import com.wistron.swpc.wismarttrafficlight.vo.TimeStatisticVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TrafficBoxService {

    private static Logger logger = LoggerFactory.getLogger(TrafficBoxService.class);

    /**
     * 转向量需要统计的时间点 单位分钟
     */
    private static final int[] INTERSECTION_FLOW_TIME = {1, 15, 60};

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TrafficFlowDataHelper trafficFlowDataHelper;

    @Autowired
    private TrafficCarDelayDataHelper trafficCarDelayDataHelper;

    @Autowired
    private GoolgeService googleService;

    @Autowired
    private SubPhaseMapper subPhaseDao;

    @Autowired
    private SubIntersectionMapper subIntersectionDao;

    @Autowired
    private TimePeriodMapper timePeriodDao;

    private JsonMapper jsonMapper = new JsonMapper();

    public void getControlStrategy() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(ParameterKeyConst.INTERSECTION_ID, "0x0000");
            TrafficBoxMsgDTO req = TrafficBoxCmdHelper.initReqCmd(TrafficBoxConst.MSG_TYPE_GET,
                    TrafficBoxConst.GET_CONTROL_STRATEGY, params);
            WebSocketHelper.broadcastMsg(jsonMapper.writeValueAsString(req));
        } catch (JsonProcessingException e) {
            logger.error("[traffic] {}", e.getMessage());
        }
    }

    public void setControlStrategy(String IntersectionId, String controlStrategy) {
        try {
            Map<String, Object> params = new HashMap<>();
            // params.put(ParameterKeyConst.INTERSECTION_ID, "0x0000"); //這是原本針對整個table去做修改
            params.put(ParameterKeyConst.INTERSECTION_ID, IntersectionId);
            params.put(ParameterKeyConst.CONTROL_STRATEGY, controlStrategy);
            TrafficBoxMsgDTO req = TrafficBoxCmdHelper.initReqCmd(TrafficBoxConst.MSG_TYPE_SET,
                    TrafficBoxConst.SET_CONTROL_STRATEGY, params);
            WebSocketHelper.broadcastMsg(jsonMapper.writeValueAsString(req));
            // 设置完时序后再次获取最新值, 推送给所有dashboard
            // getControlStrategy();
        } catch (JsonProcessingException e) {
            logger.error("[traffic] {}", e.getMessage());
        }
    } 

    /**
     * 处理 GET_CONTROL_STRATEGY 命令获取到的 ipc msg
     * 
     * @param ipcMsg
     */
    public void publishControlStrategy(List<Object> ipcMsg) {

        try {
            List<ControlStrategyDTO> msgList = jsonMapper.readValue(jsonMapper.writeValueAsString(ipcMsg),
                    new TypeReference<List<ControlStrategyDTO>>() {
                    });

            for (ControlStrategyDTO msg : msgList) {
                messagingTemplate.convertAndSend(
                        String.format("/topic/intersection/%s/sub_phases", msg.getIntersectionId()),
                        TrafficResponse.ok().setResult(getSubPhases(msg)));
            }

        } catch (JsonProcessingException e) {
            logger.error("[traffic] msg: {}, 格式转换出错: {}", ipcMsg, e.getMessage());
        }

    }

    /**
     * 处理 GET_Intersection_FlowV2 命令获得的 ipc msg
     * 
     * @param ipcMsg
     */
    public void publishIntersectionFlow(TrafficBoxMsgDTO ipcMsg) {

        try {
            long currentSeconds = DateUtil.getUtcMillis() / 1000;

            if (null == ipcMsg.getIntersectionId()) {
                return;
            }

            // save intersectionFlow to redis, key: intersection_flow:{intersectionId}
            String redisKey = TrafficFlowDataHelper.TRAFFIC_FLOW_REDIS_PREFIX + ipcMsg.getIntersectionId();
            stringRedisTemplate.opsForZSet().add(redisKey, jsonMapper.writeValueAsString(ipcMsg), currentSeconds);
        } catch (JsonProcessingException e) {
            logger.error("[traffic] msg: {}, 格式转换出错: {}", ipcMsg, e.getMessage());
        }

        List<SubIntersection> subIntersectionList = subIntersectionDao
                .selectByIntersectionId(ipcMsg.getIntersectionId());

        Map<String, SubIntersection> subIntersectionIdMap = subIntersectionList.stream()
                .collect(Collectors.toMap(SubIntersection::getId, item -> item, (key1, key2) -> key1));

        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("publishIntersectionFlow-")
                .build();
        ExecutorService pool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(),
                namedThreadFactory);
        List<Future<Boolean>> taskResultList = new ArrayList<>();

        // 推送 车速，存车空间 状态
        Future<Boolean> taskResult = pool.submit(() -> {
            StorageSpeedVO storageSpeedVO = trafficFlowDataHelper.processStorageSpeedData(ipcMsg.getIntersectionId(),
                    subIntersectionList);
            TrafficResponse response = TrafficResponse.ok().setResult(storageSpeedVO);
            messagingTemplate.convertAndSend(
                    String.format("/topic/intersection/%s/storage_space_speed", ipcMsg.getIntersectionId()), response);

            ((StorageSpeedVO) response.getData()).setIntersectionId(ipcMsg.getIntersectionId());
            messagingTemplate.convertAndSend("/topic/storage_space_speed", response);
            return true;
        });
        taskResultList.add(taskResult);

        // 推送今天的流量趋势图更新
        taskResult = pool.submit(() -> {
            List<TimeStatisticVO<List<StatisticDataVO>>> timeStatisticVOList = trafficFlowDataHelper
                    .processTrafficTrend(ipcMsg, null);
            messagingTemplate.convertAndSend("/topic/time_statistic",
                    TrafficResponse.ok().setResult(timeStatisticVOList));
            return true;
        });
        taskResultList.add(taskResult);

        // // 推送今天的流量趋势图更新(google)
        // taskResult = pool.submit(() -> {
        //     List<TimeStatisticVO<List<StatisticDataVO>>> timeStatisticVOList = googleService
        //             .processTrafficTrend(ipcMsg, null);
        //     messagingTemplate.convertAndSend("/topic/time_statistic/google",
        //             TrafficResponse.ok().setResult(timeStatisticVOList));
        //     return true;
        // });
        // taskResultList.add(taskResult);

        // get intersection_flow data, send topic
        for (int min : INTERSECTION_FLOW_TIME) {
            taskResult = pool.submit(() -> {
                List<SubIntersectionFlowVO> data = trafficFlowDataHelper.processTrafficFlow(ipcMsg.getIntersectionId(),
                        min, subIntersectionIdMap);
                messagingTemplate.convertAndSend(String.format("/topic/intersection/%s/traffic_flow/duration/%d",
                        ipcMsg.getIntersectionId(), min), TrafficResponse.ok().setResult(data));
                return true;
            });
            taskResultList.add(taskResult);
        }
        pool.shutdown();

        for (int i = 0; i < taskResultList.size(); i++) {
            try {
                if (!taskResultList.get(i).get()) {
                    logger.error("[traffic] publishIntersectionFlow task[{}] error", i);
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("[traffic] publishIntersectionFlow task error", e);
            }
        }
    }

        /**
     * 处理 GET_Intersection_VQ 命令获得的 ipc msg
     * 
     * @param ipcMsg
     */
    public void publishIntersectionVQ(TrafficBoxMsgDTO ipcMsg) {

        try {
            long currentSeconds = DateUtil.getUtcMillis() / 1000;

            if (null == ipcMsg.getIntersectionId()) {
                return;
            }

            // save intersectionFlow to redis, key: intersection_flow:{intersectionId}
            String redisKey = TrafficCarDelayDataHelper.CAR_DELAY_REDIS_PREFIX + ipcMsg.getIntersectionId();
            stringRedisTemplate.opsForZSet().add(redisKey, jsonMapper.writeValueAsString(ipcMsg), currentSeconds);
        } catch (JsonProcessingException e) {
            logger.error("[traffic] msg: {}, 格式转换出错: {}", ipcMsg, e.getMessage());
        }

        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("publishIntersectionFlow-")
                .build();
        ExecutorService pool = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(),
                namedThreadFactory);
        List<Future<Boolean>> taskResultList = new ArrayList<>();

        //推送今天的停等時間更新
        Future<Boolean> taskResult = pool.submit(() -> {
            List<TimeStatisticVO<List<StatisticDataVO>>> timeStatisticVOList = trafficCarDelayDataHelper
                    .processCarDelay(ipcMsg, null);
            messagingTemplate.convertAndSend("/topic/car_delay",
                    TrafficResponse.ok().setResult(timeStatisticVOList));
            return true;
        });
        taskResultList.add(taskResult);

        pool.shutdown();

        for (int i = 0; i < taskResultList.size(); i++) {
            try {
                if (!taskResultList.get(i).get()) {
                    logger.error("[traffic] publishIntersectionVQ task[{}] error", i);
                }
            } catch (InterruptedException | ExecutionException e) {
                logger.error("[traffic] publishIntersectionVQ task error", e);
            }
        }
    }

    private SubPhaseDetailVO getSubPhases(ControlStrategyDTO msg) {

        SubPhaseDetailVO detail = new SubPhaseDetailVO();

        detail.setIntersectionId(msg.getIntersectionId());
        detail.setControlStrategy(msg.getControlStrategy());
        detail.setControlStrategyFromCenter(msg.getControlStrategyFromCenter());
        detail.setCurrentSubPhaseId(msg.getCurrSubPhaseId());

        List<SubPhaseVO> subPhaseVOList = new ArrayList<>();

        String timingUuid = getTimingUuidByIntersectionId(msg.getIntersectionId());
        List<SubPhase> subPhaseList = subPhaseDao.selectByTimingUuid(timingUuid);

        for (SubPhase subPhase : subPhaseList) {
            SubPhaseVO subPhaseVO = new SubPhaseVO();
            subPhaseVO.setSubPhaseId(subPhase.getId());

            /* 時相時間為 step1(綠燈，已含行閃、行紅，資料庫設定與文件一樣) */
            if (TrafficBoxConst.CONTROL_STRATEGY_FIXED.equals(msg.getControlStrategy())
                    || TrafficBoxConst.CONTROL_STRATEGY_MANUAL.equals(msg.getControlStrategy())) {
                int effectTimeTotal = subPhase.getEffectTimeStep1();
                subPhaseVO.setEffectTimeTotal(effectTimeTotal);
            } else {
                /* IPC只給綠燈時間，須加上行閃、行紅 */
                int effectTimeTotal = getEffectTimeGreen(subPhase.getId(), msg) + subPhase.getEffectTimeStep2() + subPhase.getEffectTimeStep3();
                subPhaseVO.setEffectTimeTotal(effectTimeTotal);
            }

            subPhaseVO.setEffectTimeLast(getEffectTimeLasr(msg, subPhase));
            subPhaseVO.setSubIntersection1Light(subPhase.getSubIntersection1Light());
            subPhaseVO.setSubIntersection2Light(subPhase.getSubIntersection2Light());
            subPhaseVO.setSubIntersection3Light(subPhase.getSubIntersection3Light());
            subPhaseVO.setSubIntersection4Light(subPhase.getSubIntersection4Light());
            subPhaseVO.setSubIntersection5Light(subPhase.getSubIntersection5Light());
            subPhaseVO.setSubIntersection6Light(subPhase.getSubIntersection6Light());
            subPhaseVO.setSubIntersection7Light(subPhase.getSubIntersection7Light());
            subPhaseVO.setSubIntersection8Light(subPhase.getSubIntersection8Light());
            subPhaseVOList.add(subPhaseVO);
        }
        detail.setSubPhase(subPhaseVOList);

        return detail;
    }

    private int getEffectTimeLasr(ControlStrategyDTO msg, SubPhase subPhase) {
        int effectTimeLast = Integer.valueOf(msg.getCurrEffectTime());
        SubPhaseStepEnum subPhaseCurrentStep = SubPhaseStepEnum.getInstance(msg.getCurrStep());

        if(subPhaseCurrentStep != null) {
            switch (subPhaseCurrentStep) {
                case STEP4:
                case STEP5:
                    effectTimeLast = 0;
                    break;
                case STEP1:
                    effectTimeLast = effectTimeLast + subPhase.getEffectTimeStep2();
                case STEP2:
                    effectTimeLast = effectTimeLast + subPhase.getEffectTimeStep3();
                    break;
                case STEP3:
                    break;
                case FLASH:
                    break;
            }
        }

        return effectTimeLast;
    }

    private String getTimingUuidByIntersectionId(String intersectionId) {
        String timingUuid = "";
        Double thisHourTime = Double.valueOf(DateUtil.getLocalHourTime());
        
        List<TimePeriod> timePeriods = timePeriodDao.selectTimePeriodByIntersectionId(intersectionId);
        if(timePeriods == null || timePeriods.size() <= 0) {
            logger.error("[traffic] 時制抓取失敗: {}", intersectionId);
            return timingUuid;
        }
        
        int timePeriodSize = timePeriods.size();
        Double fromHourTime = 0D;
        Double toHourTime = 0D;
        for(int i=0 ; i < timePeriodSize ; i++) {
            TimePeriod timePeriod = timePeriods.get(i);
            int lastDataIndex = timePeriodSize - 1;
            fromHourTime = 0D;
            toHourTime = 0D;
            try {
                fromHourTime = Double.valueOf(timePeriod.getFromHourTime());
                toHourTime = Double.valueOf(timePeriod.getToHourTime());
            } catch (NumberFormatException e) {
                // logger.error("[traffic] TimePeriod : {}, 時段轉換失敗: {}", timePeriod, e.getMessage());
                String log = logSecureService.vaildLog("[traffic] TimePeriod : " + timePeriod.toString() + ", 時段轉換失敗: " + e.getMessage().toString());
                logger.error(log);
                break;
            }

            if(i == 0 && thisHourTime < fromHourTime) {
                timingUuid = timePeriods.get(lastDataIndex).getTimingUuid();
                break;
            }

            if(thisHourTime > fromHourTime && thisHourTime < toHourTime) {
                timingUuid = timePeriod.getTimingUuid();
                break;
            }

            if(i == lastDataIndex && thisHourTime > fromHourTime){
                timingUuid = timePeriod.getTimingUuid();
                break;
            }
        }

        return timingUuid;
    }

    private int getEffectTimeGreen(String subPhaseId, ControlStrategyDTO msg) {
        //TODO 先共用，待移除 effectTimeTotal
        String effectTimeTotal = null;
        String effectTimeGreen = null;
        switch (SubPhaseEnum.getInstance(subPhaseId)) {
            case ONE:
                effectTimeTotal = msg.getSubPhaseId1EffectTimeTotal();
                effectTimeGreen = msg.getSubPhaseId1EffectTimeGreen();
                break;
            case TWO:
                effectTimeTotal = msg.getSubPhaseId2EffectTimeTotal();
                effectTimeGreen = msg.getSubPhaseId2EffectTimeGreen();
                break;
            case THREE:
                effectTimeTotal = msg.getSubPhaseId3EffectTimeTotal();
                effectTimeGreen = msg.getSubPhaseId3EffectTimeGreen();
                break;
            case FOUR:
                effectTimeTotal = msg.getSubPhaseId4EffectTimeTotal();
                effectTimeGreen = msg.getSubPhaseId4EffectTimeGreen();
                break;
            case FIVE:
                effectTimeTotal = msg.getSubPhaseId5EffectTimeTotal();
                effectTimeGreen = msg.getSubPhaseId5EffectTimeGreen();
                break;
            case SIX:
                effectTimeTotal = msg.getSubPhaseId6EffectTimeTotal();
                effectTimeGreen = msg.getSubPhaseId6EffectTimeGreen();
                break;
            case SEVEN:
                effectTimeTotal = msg.getSubPhaseId7EffectTimeTotal();
                effectTimeGreen = msg.getSubPhaseId7EffectTimeGreen();
                break;
            case EIGHT:
                effectTimeTotal = msg.getSubPhaseId8EffectTimeTotal();
                effectTimeGreen = msg.getSubPhaseId8EffectTimeGreen();
                break;
            default:
        }

        Integer effectTimeGreenIteger = 0;
        if(effectTimeTotal != null) {
            effectTimeGreenIteger = Integer.parseInt(effectTimeTotal);
        }

        if(effectTimeGreenIteger == 0 && effectTimeGreen != null) {
            effectTimeGreenIteger = Integer.parseInt(effectTimeGreen);
        }
        return effectTimeGreenIteger;
    }


}
