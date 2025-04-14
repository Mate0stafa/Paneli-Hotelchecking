package com.example.paneli.Services;


import com.example.paneli.Models.County;
import com.example.paneli.Repositories.CityRepository;
import com.example.paneli.Repositories.CountyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("CountyService")
public class CountyService {
    @Autowired
    private CountyRepository countyRepository;
    @Autowired
    private CityRepository cityRepository;
    
    public List<County> getAllCounties() {
        return countyRepository.findAll();
    }

    public List<County> getCountyByCountryId(Long countryId){
        return countyRepository.findAllByCountryId(countryId);
    }

    public List<County> getCountyByCountryName(String countryName){
        return countyRepository.findByCountry_country_name(countryName);
    }

}
