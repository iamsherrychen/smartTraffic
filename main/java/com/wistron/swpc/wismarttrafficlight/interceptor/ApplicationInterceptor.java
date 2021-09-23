package com.wistron.swpc.wismarttrafficlight.interceptor;

import com.wistron.swpc.wismarttrafficlight.helper.WiLicenseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

@Component
public class ApplicationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // 证书校验
        if (WiLicenseHelper.licenseStatus) {
            return true;
        } else {

            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Expires", "0");
            response.addHeader("X-Content-Type-Options", "nosniff");
            response.addHeader("X-Frame-Options", "DENY");
            response.addHeader("X-XSS-Protection", "1; mode=block");

            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            response.getOutputStream().write("證書過期".getBytes(StandardCharsets.UTF_8));

            return false;
        }
    }

}
