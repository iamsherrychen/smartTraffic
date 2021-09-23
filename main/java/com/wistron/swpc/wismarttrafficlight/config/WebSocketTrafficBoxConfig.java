package com.wistron.swpc.wismarttrafficlight.config;

import com.wistron.swpc.wismarttrafficlight.constant.WebSocketConst;
import com.wistron.swpc.wismarttrafficlight.handler.TrafficBoxHandler;
import com.wistron.swpc.wismarttrafficlight.interceptor.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketTrafficBoxConfig implements WebSocketConfigurer {

    @Autowired
    private TrafficBoxHandler trafficBoxHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {

        webSocketHandlerRegistry.addHandler(trafficBoxHandler, WebSocketConst.TRAFFIC_BOX)
                .addInterceptors(new WebSocketInterceptor()).setAllowedOrigins("*");

    }

}
