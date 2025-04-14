package com.example.paneli.Controllers;


import com.example.paneli.Models.Country;
import com.example.paneli.Repositories.CountryRepository;
import com.example.paneli.Repositories.CountyRepository;
import com.example.paneli.DataObjects.NewCountry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class CountryController {

    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CountyRepository countyRepository;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addCountry")
    public ModelAndView addCountryGet(HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/country/addCountry");
//
            NewCountry newCountry = new NewCountry();
            modelAndView.addObject("newCountry", newCountry);
            modelAndView.addObject("county", countyRepository.findAll());
        }

        return modelAndView;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/country/addCountry")
    public ModelAndView addCountry(HttpServletRequest request,
                                   NewCountry newCountry,
                                   @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView();

        if (request.isUserInRole("ROLE_ADMIN")) {
            Path uploadDir = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            try {
                Country country;
                if (file != null && !file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();

                    country = new Country(newCountry.getCountryName(), originalFilename, "0.0");
                    country.setVatPercentage(newCountry.getVatPercentage());
                    countryRepository.saveAndFlush(country);

                    Path targetPath = uploadDir.resolve(originalFilename);
                    Files.write(targetPath, file.getBytes());
                } else {
                    country = new Country(newCountry.getCountryName(), null, "0.0");
                    country.setVatPercentage(newCountry.getVatPercentage());
                    countryRepository.saveAndFlush(country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            modelAndView.setViewName("redirect:/countryList");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deleteCountry")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteCountry(@RequestParam(value = "id") Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Country> countryOptional = countryRepository.findById(id);

            //kontrollojme nese ka counties ose costumer qe perdor id e shtetit qe duam te bejme delete
            Country country = countryOptional.get();
            if (!country.getCounties().isEmpty() ) {
                response.put("success", false);
                response.put("message", "Cannot delete country with associated counties or bookings");
                return ResponseEntity.ok(response);
            }

            countryRepository.deleteCountry_ById(id);
            response.put("success", true);
            response.put("message", "Country deleted successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Cannot delete country with associated counties or bookings");
            return ResponseEntity.ok(response);
        }
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/showCountry")
    public ModelAndView showCountry(@RequestParam(value ="countryId") Long countryId, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.addObject("country", countryRepository.findById(countryId).get());
            modelAndView.addObject("county", countyRepository.findAll());
            modelAndView.setViewName("ROLE_ADMIN/country/showCountry");
        }
        return modelAndView;
    }






    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editCountry")
    public ModelAndView editCountryGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){

            Optional<Country> countryOptional = countryRepository.findById((long) Math.toIntExact(id));
            if(countryOptional.isPresent()) {
                Country country = countryRepository.findById(id).get();
                modelAndView.addObject("country", countryRepository.findById(id).get());
                modelAndView.addObject("counties", countyRepository.findAll());
                modelAndView.addObject("countries", countryRepository.findAll());
                modelAndView.addObject("imagePath", "/uploads/" + country.getFile_name());
                modelAndView.setViewName("ROLE_ADMIN/country/editCountry");
            }
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editCountry")
    public ModelAndView editCountryPost(HttpServletRequest request,
                                        @RequestParam("id") Long id,
                                        @RequestParam("newCountryName") String newCountryName,
                                        @RequestParam("newVatPercentage") Float newVatPercentage,
                                        @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView();

        if (request.isUserInRole("ROLE_ADMIN")) {
            Optional<Country> countryOptional = countryRepository.findById(id);
            if (countryOptional.isPresent()) {
                Country country = countryOptional.get();
                country.setCountry_name(newCountryName);
                country.setVatPercentage(newVatPercentage);

                Path uploadDir = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }

                if (file != null && !file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();

                    country.setFile_name(originalFilename);

                    Path targetPath = uploadDir.resolve(originalFilename);
                    Files.write(targetPath, file.getBytes());
                }

                countryRepository.saveAndFlush(country);
                modelAndView.setViewName("redirect:/countryList");
            }
        }
        return modelAndView;
    }

}
