package com.example.paneli.Services;



import com.example.paneli.Models.City;
import com.example.paneli.Repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("CityService")
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> findall(){
        List<City> city = new ArrayList<>();
        return cityRepository.findAll();
    }


    public Iterable<City> getAll(){
        return cityRepository.findAll();
    }



    public List<City> findByCounty(Long county){
        return cityRepository.findByCounty(county);
    }

    public List<City> getPromotedCities(){
        return cityRepository.findAllByShow_city(1);
    }










}
