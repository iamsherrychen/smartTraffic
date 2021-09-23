package com.wistron.swpc.wismarttrafficlight.controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.wistron.swpc.wismarttrafficlight.entity.Secure;
import com.wistron.swpc.wismarttrafficlight.exception.ValidateException;
import com.wistron.swpc.wismarttrafficlight.service.RsyslogService;
import com.wistron.swpc.wismarttrafficlight.service.SecureService;
import com.wistron.swpc.wismarttrafficlight.vo.TrafficResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;

@RestController
public class SecureController {

    @Autowired
    private RsyslogService rsyslogService;

    @Autowired
    private SecureService secureService;

    @Value("${api.redirect}")
    private String redirectApi;

    @GetMapping(value = "/api/authenticate")
    public TrafficResponse login(HttpServletResponse response, @RequestParam("token") String token)
            throws KeyLengthException, ParseException, JOSEException {
                
                Secure result = null;

                response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.addHeader("Pragma", "no-cache");
                response.addHeader("Expires", "0");
                response.addHeader("X-Content-Type-Options", "nosniff");
                response.addHeader("X-Frame-Options", "DENY");
                response.addHeader("X-XSS-Protection", "1; mode=block");

                //檢查token格式
                if(!secureService.tokenValidate(token)) {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    return TrafficResponse.error().setResult("Invalid token");
                } else {
                    result = secureService.jweToken(token);
                    //檢查時間限制(<600sec)
                    if(!secureService.accessPermission(result)) {
                        response.setStatus(HttpStatus.FORBIDDEN.value());
                        return TrafficResponse.error().setResult("Timeout");
                    } else {
                        rsyslogService.log(String.format("Login Id: %s",result.getId()));
                        return TrafficResponse.ok().setResult(result);
                    }
                }
    }

    @PostMapping(value = "/api/tokenGenerator")
    public TrafficResponse tokenGenerator(@RequestBody Secure entity) throws KeyLengthException, JOSEException {
        return TrafficResponse.ok().setResult(secureService.tokenGenerator(entity));
    }

    @GetMapping(value = "/api/redirect")
    public TrafficResponse redirect() {
        return TrafficResponse.ok().setResult(redirectApi);
    }
}
