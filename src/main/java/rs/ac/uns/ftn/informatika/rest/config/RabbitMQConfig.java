package rs.ac.uns.ftn.informatika.rest.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public FanoutExchange advertisementFanoutExchange() {
        return new FanoutExchange("advertisementFanout");
    }
}
