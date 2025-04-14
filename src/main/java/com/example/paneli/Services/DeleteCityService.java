package com.example.paneli.Services;

import com.example.paneli.Models.City;
import com.example.paneli.Models.Contract.Agreement;
import com.example.paneli.Repositories.AgreementRepository;
import com.example.paneli.Repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeleteCityService {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AgreementRepository agreementRepository;

    @Transactional
    public Map<String, Object> deleteCitySync(Long id) {
        Map<String, Object> response = new HashMap<>();
        City city = cityRepository.findById(id).orElse(null);

        if (city == null) {
            response.put("success", false);
            response.put("message", "City not found");
            return response;
        }

        System.out.println("Deleting City ID: " + city.getId());

        long startTime = System.currentTimeMillis();

        try {
            // Check if there are blocking dependencies before proceeding
            boolean hasBlockingDependencies = false;
            if (!city.getCityPhotos().isEmpty() || !city.getAddresses().isEmpty()) {
                hasBlockingDependencies = true;
                System.out.println("City had photo or addresses linked: " + city.getId());
            }

            List<Agreement> agreements = agreementRepository.findByCityId(id);
            if (agreements != null && !agreements.isEmpty()) {
                hasBlockingDependencies = true;
                System.out.println("City agreements linked: " + city.getId());
            }

            if (hasBlockingDependencies) {
                response.put("success", false);
                response.put("message", "City cannot be deleted due to existing properties.");
                return response;
            }

            // Proceed with deletion logic if no blocking dependencies found
            cityRepository.deleteCityById(id);
            cityRepository.flush();

            response.put("success", true);
            response.put("message", "City deleted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error deleting city: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken: " + (endTime - startTime) + " ms");

        return response;
    }
}
