package com.wistron.swpc.wismarttrafficlight.interceptor;

import com.wistron.swpc.wismarttrafficlight.helper.WiLicenseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {

        // 证书校验
        if (WiLicenseHelper.licenseStatus) {
            return true;
        } else {
            serverHttpResponse.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return false;
        }

    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception e) {

    }

}
