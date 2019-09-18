package com.huamai.hdServer.client;

import com.huamai.hdServer.vo.InMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Auther: 旭燃
 * @Date: 2019/5/1 22:11
 * @Description:
 */
@Component
public class DemoWebSocketClient {

    static public class MyStompSessionHandler extends StompSessionHandlerAdapter {

        private String name;

        public MyStompSessionHandler(String name) {
            this.name = name;
        }

        private void showHeaders(StompHeaders headers) {
            for (Map.Entry<String, List<String>> e : headers.entrySet()) {
                System.err.print("  " + e.getKey() + ": ");
                boolean first = true;
                for (String v : e.getValue()) {
                    if (!first) {
                        System.err.print(", ");
                    }
                    System.err.print(v);
                    first = false;
                }
                System.err.println();
            }
        }

        private void sendJsonMessage(StompSession session) {
            InMessage msg = new InMessage("I love you");
                synchronized (session) {
                    session.send("/app/v1/chat", msg);
                }
        }

        private void subscribeTopic(String topic, StompSession session) {
            session.subscribe(topic, new StompFrameHandler() {

                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return ResponseEntity.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    System.err.println(payload.toString());
                }
            });
        }

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.err.println("Connected! Headers:");
            showHeaders(connectedHeaders);

            subscribeTopic("/topic/game_chat", session);
            sendJsonMessage(session);

            System.err.println("please input your new name:");
        }
    }

    public void propellingMovement() throws Exception {
        WebSocketClient simpleWebSocketClient = new StandardWebSocketClient();
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(simpleWebSocketClient));

        SockJsClient sockJsClient = new SockJsClient(transports);
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        String url = "ws://localhost:8083/endpoint-websocket";
        String name = "spring-" + ThreadLocalRandom.current().nextInt(1, 99);
        StompSessionHandler sessionHandler = new MyStompSessionHandler(name);
        ListenableFuture<StompSession> connect = stompClient.connect(url, sessionHandler);

        StompSession session = connect.get();

    }
}
