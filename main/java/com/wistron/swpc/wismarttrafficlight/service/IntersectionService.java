package com.wistron.swpc.wismarttrafficlight.service;

import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import com.wistron.swpc.wismarttrafficlight.entity.SubIntersection;
import com.wistron.swpc.wismarttrafficlight.helper.TrafficFlowDataHelper;
import com.wistron.swpc.wismarttrafficlight.mapper.AccountMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.IntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.mapper.SubIntersectionMapper;
import com.wistron.swpc.wismarttrafficlight.vo.StorageSpeedVO;
import com.wistron.swpc.wismarttrafficlight.vo.SubIntersectionFlowVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.minidev.asm.ConvertDate.StringCmpNS;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IntersectionService {

    @Autowired
    private RsyslogService rsyslogService;

    @Autowired
    private IntersectionMapper intersectionDao;

    @Autowired
    private AccountMapper accountDao;

    @Autowired
    private TrafficBoxService trafficBoxService;

    @Autowired
    private TrafficFlowDataHelper trafficFlowDataHelper;

    @Autowired
    private SubIntersectionMapper subIntersectionDao;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RoadCoordinateService roadCoordinateService;

    /**
     * 验证权限，设置时序
     * @param controlStrategy
     * @param name
     * @param pwd
     * @return
     */
    public boolean setControlStrategyByToken(String projectId, String controlStrategy, String id, String role) {

        rsyslogService.log(String.format("SetControlStrategy: id- %s, role- %s, %s", id, role, controlStrategy));

        // send cmd to ipc
        trafficBoxService.setControlStrategy(projectId, controlStrategy);

        return true;

    }


    /**
     * 验证权限，设置时序
     * @param controlStrategy
     * @param name
     * @param pwd
     * @return
     */
    public boolean setControlStrategy(String projectId, String controlStrategy, String name, String pwd) {

        int count = accountDao.authAccount(name, pwd);

        if (count != 1) {
            return false;
        }

        rsyslogService.log(String.format("SetControlStrategy: %s, %s", name, controlStrategy));

        // send cmd to ipc
        trafficBoxService.setControlStrategy(projectId, controlStrategy);

        return true;

    }

    /**
     * 根据路口 id, 获取单个路口信息
     * @param id
     * @return
     */
    public Intersection getIntersectionById(String id) {
        return intersectionDao.selectById(id);
    }

    /**
     * 获取所有路口基本信息
     * @return
     */
    public List<Intersection> getAllIntersection() {
        return intersectionDao.selectAll();
    }

    /**
     * 获取转向量统计数据
     * @param intersectionId
     * @param min
     * @return
     */
    public List<SubIntersectionFlowVO> getTrafficFlow(String intersectionId, int min) {
        Map<String, SubIntersection> subIntersectionIdMap = subIntersectionDao
                .selectByIntersectionId(intersectionId)
                .stream()
                .collect(Collectors.toMap(SubIntersection::getId, item -> item, (key1, key2) -> key1));

        return trafficFlowDataHelper.processTrafficFlow(intersectionId, min, subIntersectionIdMap);
    }

    /**
     * 根据路口 id 获取存车空间，车速状态
     * @param intersectionId
     * @return
     */
    public StorageSpeedVO getStorageSpaceSpeedById(String intersectionId) {
        List<SubIntersection> subIntersectionList = subIntersectionDao.selectByIntersectionId(intersectionId);
        return trafficFlowDataHelper.processStorageSpeedData(intersectionId, subIntersectionList);
    }

    /**
     * 获取所有路口存车空间，车速状态
     * @return
     */
    public void getAllStorageSpaceSpeed() {
        List<Intersection> intersectionList = intersectionDao.selectAll();
        List<SubIntersection> subIntersectionList;
        StorageSpeedVO item;
        for (Intersection intersection : intersectionList) {
            subIntersectionList = subIntersectionDao.selectByIntersectionId(intersection.getId());
            item = trafficFlowDataHelper.processStorageSpeedData(intersection.getId(), subIntersectionList);
            item.setIntersectionId(intersection.getId());
            messagingTemplate.convertAndSend("/topic/storage_space_speed",
                    TrafficResponse.ok().setResult(item));
        }
    }

    /**
     * 获取所有路口存车空间，车速状态 定时任务
     */
    public void getAllStorageSpaceSpeedTask() {
        List<Intersection> intersectionList = intersectionDao.selectAll();
        List<SubIntersection> subIntersectionList;
        for (Intersection intersection : intersectionList) {
            subIntersectionList = subIntersectionDao.selectByIntersectionId(intersection.getId());
            for (SubIntersection subIntersection : subIntersectionList) {
                if (!StringUtils.isEmpty(subIntersection.getConnectTo())) {
                    roadCoordinateService.processRoutePath(intersection.getId(), subIntersection.getConnectTo(), true);
                }
            }
        }
    }

}
