package com.example.paneli.Services;
import java.util.List;


import com.example.paneli.Models.HotelFacility;
import com.example.paneli.Repositories.HotelFacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("HotelFacilityservice")
public class HotelFacilityservice {

    @Autowired
    private HotelFacilityRepository hotelFacilityRepository;


    public List<HotelFacility> getAllFacilities() {
        return hotelFacilityRepository.findAll();
    }


}
