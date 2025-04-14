package com.example.paneli.Controllers;


import com.example.paneli.Models.County;
import com.example.paneli.Repositories.CityRepository;
import com.example.paneli.Repositories.CountryRepository;
import com.example.paneli.Repositories.CountyRepository;
import com.example.paneli.DataObjects.NewCounty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CountyController {
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CountyRepository countyRepository;
    @Autowired
    CityRepository cityRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addCounty")
    public ModelAndView addCountyGet(HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/county/addCounty");

//
            NewCounty newCounty = new NewCounty();
            modelAndView.addObject("countries", countryRepository.findAll());
            modelAndView.addObject("newCounty", newCounty);
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addCounty")
    public ModelAndView addCounty(HttpServletRequest request, NewCounty newCounty) {
        ModelAndView modelAndView = new ModelAndView();

        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.addObject("newCounty", new NewCounty());
            System.out.println("Countryid= " + countryRepository.findById(newCounty.getCountryId()));

            County county = new County(newCounty.getName(), 0, countryRepository.findById(newCounty.getCountryId()).get());

            countyRepository.saveAndFlush(county);
            modelAndView.setViewName("redirect:/countyList");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/showCounty")
    public ModelAndView showCounty(@RequestParam(value ="countyId") Long countyId, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.addObject("county", countyRepository.findById(countyId).get());
            modelAndView.addObject("city", cityRepository.findAll());
            modelAndView.setViewName("ROLE_ADMIN/county/showCounty");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editCounty")
    public ModelAndView editCountyGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/county/editCounty");
            modelAndView.addObject("county", countyRepository.findById(id).get());
            modelAndView.addObject("counties", countyRepository.findAll());
            modelAndView.addObject("countries", countryRepository.findAll());
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editCounty")
    public ModelAndView editCounty(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, County county){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("redirect:/countyList");
            County dbCounty = countyRepository.findById(id).get();
            dbCounty.setName(county.getName());
            dbCounty.setCountry(countryRepository.findById(county.getCountry().getId()).get());
            countyRepository.save(dbCounty);
            modelAndView.addObject("county", countyRepository.findById(id).get());
            modelAndView.addObject("counties", countyRepository.findAll());
            modelAndView.addObject("countries", countryRepository.findAll());
        }
        return modelAndView;
    }





}
