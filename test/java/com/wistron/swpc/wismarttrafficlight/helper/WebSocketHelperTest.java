package com.wistron.swpc.wismarttrafficlight.helper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
class WebSocketHelperTest {

    @Test
    void sendMsg() {
        WebSocketSession webSocketSession = new WebSocketSession() {
            @Override
            public String getId() {
                return null;
            }

            @Override
            public URI getUri() {
                return null;
            }

            @Override
            public HttpHeaders getHandshakeHeaders() {
                return null;
            }

            @Override
            public Map<String, Object> getAttributes() {
                return null;
            }

            @Override
            public Principal getPrincipal() {
                return null;
            }

            @Override
            public InetSocketAddress getLocalAddress() {
                return null;
            }

            @Override
            public InetSocketAddress getRemoteAddress() {
                return null;
            }

            @Override
            public String getAcceptedProtocol() {
                return null;
            }

            @Override
            public void setTextMessageSizeLimit(int i) {

            }

            @Override
            public int getTextMessageSizeLimit() {
                return 0;
            }

            @Override
            public void setBinaryMessageSizeLimit(int i) {

            }

            @Override
            public int getBinaryMessageSizeLimit() {
                return 0;
            }

            @Override
            public List<WebSocketExtension> getExtensions() {
                return null;
            }

            @Override
            public void sendMessage(WebSocketMessage<?> webSocketMessage) throws IOException {

            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public void close(CloseStatus closeStatus) throws IOException {

            }
        };
        WebSocketHelper.sendMsg(webSocketSession, "test");
    }

    @Test
    void broadcastMsg() {
        WebSocketHelper.broadcastMsg("test");
    }
}