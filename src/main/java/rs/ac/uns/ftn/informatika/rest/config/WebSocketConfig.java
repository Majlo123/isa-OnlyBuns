package rs.ac.uns.ftn.informatika.rest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Omogućava slanje poruka klijentu
        registry.setApplicationDestinationPrefixes("/app"); // Endpoint za primanje poruka
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // URL za SockJS konekciju
                .setAllowedOrigins("http://localhost:4200") // Dozvoli sve origin-e (samo za razvoj)
                .withSockJS(); // Koristi SockJS kao fallback
    }
}

