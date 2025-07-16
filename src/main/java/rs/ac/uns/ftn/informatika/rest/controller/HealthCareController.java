package rs.ac.uns.ftn.informatika.rest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.domain.HealthCareLocation;
import rs.ac.uns.ftn.informatika.rest.service.HealthCareLocationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/healthCare")
public class HealthCareController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    HealthCareLocationService locationService;

    @GetMapping
    public Object healthCare() throws JsonProcessingException {
        List<HealthCareLocation> savedLocations = new ArrayList<>();
        savedLocations = locationService.getAllHealthCareLocations();
        ObjectMapper mapper = new ObjectMapper();

        while (true) {
            Object message = amqpTemplate.receiveAndConvert("spring-boot2");

            if (message == null) {
                break;
            }

            if (message instanceof Map) {
                String json = mapper.writeValueAsString(message);
                HealthCareLocation loc = mapper.readValue(json, HealthCareLocation.class);
                if(locationService.getHealthCareLocation(loc.getId()) == null) {
                    locationService.saveHealthCareLocation(loc);
                    savedLocations.add(loc);
                }
            } else {
                System.out.println("Unexpected message type: " + message.getClass());
            }
        }

        return savedLocations.isEmpty() ? "No messages available" : savedLocations;
    }

}
