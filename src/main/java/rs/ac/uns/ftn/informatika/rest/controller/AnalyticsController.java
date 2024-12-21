package rs.ac.uns.ftn.informatika.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.informatika.rest.dto.AnalyticsResponse;
import rs.ac.uns.ftn.informatika.rest.service.AnalyticsService;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/admin")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            AnalyticsResponse response = analyticsService.getAnalytics(start, end);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

