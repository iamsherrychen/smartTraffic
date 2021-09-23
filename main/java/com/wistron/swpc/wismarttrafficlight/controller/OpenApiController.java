package com.wistron.swpc.wismarttrafficlight.controller;

import com.wistron.swpc.wismarttrafficlight.entity.SpecifiedDownload;
import com.wistron.swpc.wismarttrafficlight.entity.openapi.IntersectionType;
import com.wistron.swpc.wismarttrafficlight.entity.openapi.ProjectType;
import com.wistron.swpc.wismarttrafficlight.exception.ValidateException;
import com.wistron.swpc.wismarttrafficlight.service.RoadCoordinateService;
import com.wistron.swpc.wismarttrafficlight.service.SecureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.wistron.swpc.wismarttrafficlight.util.DateUtil;


import javax.servlet.http.HttpServletRequest;

@RestController
public class OpenApiController {

    @Autowired
    RoadCoordinateService roadCoordinateService;

    @Autowired
    private SecureService secureService;

    @Value("${api.address}")
    private String address;

    //Open api support **取得設備上的log(含交控盒狀態、車街流資訊、設備異常狀態)
    @PostMapping(value = "api/intersections/get_log_all")
    public IntersectionType getLogAll(@RequestBody SpecifiedDownload entity, HttpServletRequest request) throws Exception {

        String token = request.getHeader("Authorization");
        IntersectionType result = new IntersectionType();

        if (null != token) {
            //檢查token格式
            if(!secureService.tokenValidate(token)) {
                throw new ValidateException("Invalid token");
            }
            //驗證日期
            if (!(DateUtil.isLegalDate(entity.getDate()) && DateUtil.validateSearchDate(entity.getDate(), 2, 0))) {
                throw new ValidateException("Date validation failed");
            }
            result.setDate(entity.getDate());
            result.setIntersectionId(entity.getId());
            result.setDocumentFileUrl(address + "/api/history/download/timeslot_status?date=" + entity.getDate());
        } else {
            throw new ValidateException("Token miss");
        }
        return result;
    }

    @PostMapping(value = "api/intersections/get_car_storage_data")
    public IntersectionType getCarStorageData(@RequestBody SpecifiedDownload entity, HttpServletRequest request) throws Exception {

        String token = request.getHeader("Authorization");
        IntersectionType result = new IntersectionType();

        if (null != token) {
            //檢查token格式
            if(!secureService.tokenValidate(token)) {
                throw new ValidateException("Invalid token");
            }
            //驗證日期
            if (!(DateUtil.isLegalDate(entity.getDate()) && DateUtil.validateSearchDate(entity.getDate(), 2, 0))) {
                throw new ValidateException("Date validation failed");
            }

            result.setDate(entity.getDate());
            result.setIntersectionId(entity.getId());
            result.setDocumentFileUrl(address + "/api/history/download/flow?date=" + entity.getDate());
        } else {
            throw new ValidateException("Token miss");
        }
        return result;
    }

    @PostMapping(value = "api/project/get_travel_time")
    public ProjectType getTravelTime(@RequestBody SpecifiedDownload entity, HttpServletRequest request) throws Exception {

        String token = request.getHeader("Authorization");
        ProjectType result = new ProjectType();

        if (null != token) {
            //檢查token格式
            if(!secureService.tokenValidate(token)) {
                throw new ValidateException("Invalid token");
            }
            //驗證日期
            if (!(DateUtil.isLegalDate(entity.getDate()) && DateUtil.validateSearchDate(entity.getDate(), 2, 0))) {
                throw new ValidateException("Date validation failed");
            }

            result.setDate(entity.getDate());
            result.setProjectId(entity.getId());
            result.setDocumentFileUrl(address + "/api/history/download/flow?date=" + entity.getDate());
        }   else {
            throw new ValidateException("Token miss");
        }
        return result;
    }
}
