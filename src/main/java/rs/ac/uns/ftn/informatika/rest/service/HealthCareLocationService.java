package rs.ac.uns.ftn.informatika.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.domain.HealthCareLocation;
import rs.ac.uns.ftn.informatika.rest.repository.HealthCareLocationRepository;

import java.util.List;

@Service
public class HealthCareLocationService {

    @Autowired
    private HealthCareLocationRepository healthCareLocationRepository;

    public HealthCareLocation saveHealthCareLocation(HealthCareLocation healthCareLocation) {
        return healthCareLocationRepository.save(healthCareLocation);
    }
    public HealthCareLocation getHealthCareLocation(Long id) {
        return healthCareLocationRepository.findById(id).orElse(null);
    }
    public List<HealthCareLocation> getAllHealthCareLocations() {
        return healthCareLocationRepository.findAll();
    }
}
