package com.masters.chat.config;

import static com.masters.chat.constants.Constants.ALLOWED_ORIGINS;
import static com.masters.chat.constants.Constants.APP_PREFIX;
import static com.masters.chat.constants.Constants.GROUP_PREFIX;
import static com.masters.chat.constants.Constants.USER_PREFIX;
import static com.masters.chat.constants.Constants.WEBSOCKET_PREFIX;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(WEBSOCKET_PREFIX).setAllowedOriginPatterns(ALLOWED_ORIGINS).withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        messageBrokerRegistry.setApplicationDestinationPrefixes(APP_PREFIX);
        messageBrokerRegistry.enableSimpleBroker(GROUP_PREFIX, USER_PREFIX);
        messageBrokerRegistry.setUserDestinationPrefix(USER_PREFIX);
    }

}
