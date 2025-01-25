package rs.ac.uns.ftn.informatika.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rs.ac.uns.ftn.informatika.rest.service.LoadBalancerService;

@Configuration
public class LoadBalancerConfig {

    @Bean
    public LoadBalancerService loadBalancerService() {
        return new LoadBalancerService();
    }
}
