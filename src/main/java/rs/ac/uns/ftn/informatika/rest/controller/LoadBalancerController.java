package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.service.LoadBalancerService;

@RestController
@RequestMapping("/loadbalancer")
public class LoadBalancerController {

    private final LoadBalancerService loadBalancerService;

    public LoadBalancerController(LoadBalancerService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
    }

    @GetMapping("/simulate")
    public String simulateRequest() {
        String request = "Sample Request";
        int maxRetries = 3;
        return loadBalancerService.sendRequestWithRetry(request, maxRetries);
    }
}