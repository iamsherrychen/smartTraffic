package com.wistron.swpc.wismarttrafficlight.controller;
import org.springframework.http.MediaType;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import com.wistron.swpc.wismarttrafficlight.vo.UtilityVO;
import org.springframework.beans.factory.annotation.Autowired;
import com.wistron.swpc.wismarttrafficlight.service.UtilityService;


@RestController
public class UtilityController {

    @Autowired
    private UtilityService utilityService;


    /**
     * Line Notify
     */
    @PostMapping(value = "/api/notification/line",
    consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TrafficResponse sendMessage(@RequestBody UtilityVO utility) {
        utilityService.sendMessage(utility);
        
        return TrafficResponse.ok();
    }

}
