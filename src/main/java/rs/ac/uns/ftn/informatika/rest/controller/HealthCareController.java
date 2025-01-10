package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/healthCare")
public class HealthCareController {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping
    public Object healthCare() {
        Object message = rabbitTemplate.receiveAndConvert("spring-boot2");
        return message != null ? message : "No messages available";
    }

}
