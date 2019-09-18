package com.huamai.hdServer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	/**
	 * 注册端点，发布或者订阅消息的时候需要连接此端点
	 * setAllowedOrigins 非必须,*表示允许其他域进行连接
	 * withSockJS   表示开始支持sockjs
	 */
	public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
		stompEndpointRegistry.addEndpoint("/endpoint-websocket").setAllowedOrigins("*").withSockJS();
	}
	
	/**
	 * 配置消息代理(中介)
	 * enableSimpleBroker  服务端推送给客户端的路径前缀
	 * setApplicationDestinationPrefixes  客户端发送数据给服务器端的一个前缀
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}


}
