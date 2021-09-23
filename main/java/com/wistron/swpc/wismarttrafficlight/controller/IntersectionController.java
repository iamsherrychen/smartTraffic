package com.wistron.swpc.wismarttrafficlight.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.wistron.swpc.wismarttrafficlight.constant.TrafficBoxConst;
import com.wistron.swpc.wismarttrafficlight.entity.Intersection;
import com.wistron.swpc.wismarttrafficlight.entity.Secure;
import com.wistron.swpc.wismarttrafficlight.exception.ValidateException;
import com.wistron.swpc.wismarttrafficlight.service.AreaService;
import com.wistron.swpc.wismarttrafficlight.service.DeviceService;
import com.wistron.swpc.wismarttrafficlight.service.IntersectionService;
import com.wistron.swpc.wismarttrafficlight.service.OpenDataService;
import com.wistron.swpc.wismarttrafficlight.service.RoadCoordinateService;
import com.wistron.swpc.wismarttrafficlight.service.SecureService;
import com.wistron.swpc.wismarttrafficlight.service.logSecureService;
import com.wistron.swpc.wismarttrafficlight.vo.ControlStrategyVO;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class IntersectionController {

    private static Logger logger = LoggerFactory.getLogger(IntersectionController.class);

    @Autowired
    private IntersectionService intersectionService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    RoadCoordinateService roadCoordinateService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private OpenDataService openDataService;

    @Autowired
    private SecureService secureService;

    private String[] dayuan_id = { "H33701502", "H33701801", "H33700402", "H33700401", "H33701501" };
    private String[] daju_id = { "H33700302-0-PC01", "H33700301-0-PC01" };
    // private String[] all_id = { "0x0000" };

    /**
     * 设置时序
     * 
     * @param vo
     * @param response
     * @param request
     * @return
     * @throws JOSEException
     * @throws ParseException
     * @throws KeyLengthException
     */
    // New feature 針對單一路口id去進行變動，要在後面代入projectName，原本的0x0000已不適用
    @PostMapping(value = "/api/intersections/control_strategy/{projectName}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse setControlStrategy(@RequestBody ControlStrategyVO vo, HttpServletResponse response,
            HttpServletRequest request, @PathVariable("projectName") String projectName)
            throws KeyLengthException, ParseException, JOSEException {
        if (!(TrafficBoxConst.CONTROL_STRATEGY_MANUAL.equals(vo.getControlStrategy())
                || TrafficBoxConst.CONTROL_STRATEGY_AUTO.equals(vo.getControlStrategy())
                || TrafficBoxConst.CONTROL_STRATEGY_FIXED.equals(vo.getControlStrategy()))) {
            throw new ValidateException("參數驗證失敗");
        }

        TrafficResponse result = TrafficResponse.error().setResult("帳號驗證失敗");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Expires", "0");
        response.addHeader("X-Content-Type-Options", "nosniff");
        response.addHeader("X-Frame-Options", "DENY");
        response.addHeader("X-XSS-Protection", "1; mode=block");

        String authInfo = request.getHeader("Authorization");
        
        if (null != authInfo) {
            
            if(secureService.tokenValidate(authInfo)) {
                Secure tokenInfo = secureService.jweToken(authInfo);

                //authResult先預設false
                boolean authResult = false;
                String authResultString= "";
                String[] projectStrings = {};
                switch (projectName) {
                    case "dayuan":
                    projectStrings = dayuan_id;
                        break;
                    case "daju":
                    projectStrings = daju_id;
                        break;
                    default:
                    logger.error("[traffic] {}, 請代入正確的專案名稱", projectName);
                    // projectStrings = all_id;
                        break;
                }
                //從這邊用for迴圈針對專案路口去打
                for(int i=0; i<projectStrings.length;i++) {
                    authResultString += intersectionService.setControlStrategyByToken(projectStrings[i], vo.getControlStrategy(),
                    tokenInfo.getId(), tokenInfo.getRole());
                }

                if(!authResultString.contains("false")) {
                    authResult = true;
                }
                
                if (authResult) {
                    result.setSuccess(true);
                    result.setData(null);
                    response.setStatus(HttpStatus.ACCEPTED.value());
                } else {
                    logger.error("[traffic] {}, 帳號驗證失敗", tokenInfo.getRole());
                }
            } else {
                logger.error("[traffic] {}, 非使用Token進行時制修改");
            }



            String[] accountInfo =  new String(Base64.decodeBase64(authInfo.substring(6))).split(":");
            if (accountInfo.length == 2) {
                //authResult先預設false
                boolean authResult = false;
                String authResultString= "";
                String[] projectStrings = {};
                switch (projectName) {
                    case "dayuan":
                    projectStrings = dayuan_id;
                        break;
                    case "daju":
                    projectStrings = daju_id;
                        break;
                    default:
                    logger.error("[traffic] {}, 請代入正確的專案名稱", projectName);
                    // projectStrings = all_id;
                        break;
                }
                //從這邊用for迴圈針對專案路口去打
                for(int i=0; i<projectStrings.length;i++) {
                    authResultString += intersectionService.setControlStrategy(projectStrings[i], vo.getControlStrategy(),
                        accountInfo[0], accountInfo[1]) + ",";
                }

                if(!authResultString.contains("false")) {
                    authResult = true;
                }
                
                if (authResult) {
                    result.setSuccess(true);
                    result.setData(null);
                    response.setStatus(HttpStatus.ACCEPTED.value());
                } else {
                    // logger.error("[traffic] {}, 帳號驗證失敗", accountInfo[0]);
                    String log = logSecureService.vaildLog("[traffic] {}, 帳號驗證失敗" + accountInfo[0].toString());
                    logger.error(log);
                }
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        return result;
    }

    /**
     * 获取所有路口信息
     * @return
     */
    @GetMapping(value = "/api/intersections",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse getAllIntersection() {
        return TrafficResponse.ok().setResult(intersectionService.getAllIntersection());
    }

    /**
     * 获取单个路口信息
     * @param id
     * @return
     */
    @GetMapping(value = "/api/intersections/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse getIntersectionById(@PathVariable("id") String id) {
        Intersection intersection = intersectionService.getIntersectionById(id);
        if (null == intersection) {
            return TrafficResponse.error();
        } else {
            return TrafficResponse.ok().setResult(intersection);
        }
    }

    /**
     * 获取单个路口 所有设备信息
     * @param id
     * @return
     */
    @GetMapping(value = "/api/intersections/{id}/devices",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse getIntersectionDevice(@PathVariable("id") String id) {
        return TrafficResponse.ok().setResult(deviceService.getDeviceByIntersectionId(id));
    }

    @GetMapping(value = "/api/test/process_route_path",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse getProcessRoutePath(@DestinationVariable("startId") String startId, @DestinationVariable("endId") String endId) {
        return TrafficResponse.ok().setResult(roadCoordinateService.processRoutePath(startId, endId, true));
    }

    @GetMapping(value = "/api/test/queryOpenData2",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse getqueryOpenData2() {
        return TrafficResponse.ok().setResult(openDataService.getTravelTime());
    }

    @GetMapping(value = "/api/test/queryOpenData",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse getqueryOpenData() {
        return TrafficResponse.ok().setResult(areaService.getTimeStatistic());
    }


    @SubscribeMapping("/topic/intersection/{id}/storage_space_speed")
    public TrafficResponse getStorageSpaceSpeedById(@DestinationVariable("id") String id) {
        return TrafficResponse.ok().setResult(intersectionService.getStorageSpaceSpeedById(id));
    }

    @SubscribeMapping("/topic/storage_space_speed")
    public void getAllStorageSpaceSpeed() {
        intersectionService.getAllStorageSpaceSpeed();
    }

    @SubscribeMapping("/topic/intersection/{id}/traffic_flow/duration/{min}")
    public TrafficResponse getTrafficFlow(@DestinationVariable("id") String id,
                                          @DestinationVariable("min") Integer min) {
        return TrafficResponse.ok().setResult(intersectionService.getTrafficFlow(id, min));
    }

}
