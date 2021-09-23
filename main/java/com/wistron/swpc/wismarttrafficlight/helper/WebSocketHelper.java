package com.wistron.swpc.wismarttrafficlight.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

public class WebSocketHelper {

    private static Logger logger = LoggerFactory.getLogger(WebSocketHelper.class);

    public static CopyOnWriteArraySet<WebSocketSession> trafficBox = new CopyOnWriteArraySet<>();

    public static void sendMsg(WebSocketSession session, String msg) {
        try {
            session.sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            logger.error("sendMsg error", e);
        }
    }

    public static void broadcastMsg(String msg) {
        for (WebSocketSession session : trafficBox) {
            sendMsg(session, msg);
        }
    }

}
