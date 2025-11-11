package com.example.elearning_platform.config;

import com.example.elearning_platform.service.UserDetailsServiceImpl;
import com.example.elearning_platform.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // Check if it's a CONNECT frame
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    // Get the "Authorization" header from the STOMP message
                    List<String> authorization = accessor.getNativeHeader("Authorization");

                    if (authorization != null && !authorization.isEmpty()) {
                        String authHeader = authorization.get(0);
                        String jwt = null;

                        if (authHeader != null && authHeader.startsWith("Bearer ")) {
                            jwt = authHeader.substring(7);
                        }

                        if (jwt != null) {
                            try {
                                // Validate the token
                                String username = jwtUtil.extractUsername(jwt);
                                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                                    if (jwtUtil.isTokenValid(jwt, userDetails)) {

                                        // 3. SET THE USER
                                        // This is the magic line that associates the user
                                        // with this specific WebSocket session.
                                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                userDetails, null, userDetails.getAuthorities()
                                        );
                                        accessor.setUser(authToken);
                                    }
                                }
                            } catch (Exception e) {
                                // If token is invalid, auth fails
                                System.err.println("WebSocket authentication failed: " + e.getMessage());
                            }
                        }
                    }
                }
                return message;
            }
        });
    }
}
