package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LoadBalancerService {

    private final List<String> instances;
    private final AtomicInteger currentIndex;

    public LoadBalancerService() {
        this.instances = new ArrayList<>();
        this.currentIndex = new AtomicInteger(0);
        // Add instance URLs (e.g., "http://localhost:8081", "http://localhost:8082")
        instances.add("http://localhost:8081");
        instances.add("http://localhost:8082");
    }

    // Round Robin Algorithm
    public String getNextInstance() {
        if (instances.isEmpty()) {
            throw new IllegalStateException("No instances available");
        }
        int index = currentIndex.getAndUpdate(i -> (i + 1) % instances.size());
        return instances.get(index);
    }

    // Retry Policy
    public String sendRequestWithRetry(String request, int maxRetries) {
        for (int attempt = 0; attempt < maxRetries; attempt++) {
            String instance = getNextInstance();
            try {
                // Simulate sending a request (replace with actual HTTP client logic)
                System.out.println("Sending request to: " + instance);
                // Mock response (Assume success for demonstration purposes)
                return "Response from " + instance;
            } catch (Exception e) {
                System.err.println("Request to " + instance + " failed. Retrying...");
            }
        }
        throw new RuntimeException("All retries failed");
    }
}
