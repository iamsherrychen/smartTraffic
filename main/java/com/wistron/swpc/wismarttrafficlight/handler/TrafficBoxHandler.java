package com.wistron.swpc.wismarttrafficlight.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wistron.swpc.wismarttrafficlight.constant.TrafficBoxConst;
import com.wistron.swpc.wismarttrafficlight.dto.TrafficBoxMsgDTO;
import com.wistron.swpc.wismarttrafficlight.helper.WebSocketHelper;
import com.wistron.swpc.wismarttrafficlight.service.TrafficBoxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class TrafficBoxHandler extends TextWebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger(TrafficBoxHandler.class);

    @Autowired
    private TrafficBoxService trafficBoxService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        TrafficBoxMsgDTO msg;
        try {
             msg = objectMapper.readValue(message.getPayload(), TrafficBoxMsgDTO.class);
        } catch (JsonProcessingException e) {
            logger.error("[traffic] IPC 信息格式错误: {}, error: {}", message, e.getMessage());
            return;
        }

        switch (msg.getCmd()) {
            case TrafficBoxConst.GET_CONTROL_STRATEGY:
                trafficBoxService.publishControlStrategy(msg.getMsg());
                break;
            case TrafficBoxConst.GET_INTERSECTION_FLOW_V2:
                trafficBoxService.publishIntersectionFlow(msg);
                break;            
            case TrafficBoxConst.GET_Intersection_VQ:
                trafficBoxService.publishIntersectionVQ(msg);
                break;
            default:
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        WebSocketHelper.trafficBox.add(session);
        logger.info("new connect：" + session.getId());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // remove session
        WebSocketHelper.trafficBox.remove(session);
        logger.info("disconnect: " + session.getId());
    }

}
