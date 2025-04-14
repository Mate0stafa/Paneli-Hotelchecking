package com.example.paneli.Controllers;


import com.example.paneli.DataObjects.Property.SearchDto;
import com.example.paneli.DataObjects.Property.SearchPropertyDTO;
import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.CityRepository;
import com.example.paneli.Repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class SearchController {


    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    CityRepository cityRepository;



    @GetMapping(value = "/autocomplete")
    @ResponseBody
    public List<String> autoName(@RequestParam(value = "searchvalue", required = false, defaultValue = "") String searchvalue) {
        List<String> properties = new ArrayList<>();
        System.out.println(searchvalue);

        boolean containsOnlyDigits = true;
        long id = 0;
        long newID = 0;
        for (int i = 0; i < searchvalue.length(); i++) {
            if (!Character.isDigit(searchvalue.charAt(i))) { // in case that a char is NOT a digit, enter to the if code block
                containsOnlyDigits = false;
                break;
            } else {
                if (containsOnlyDigits) {
                    id = Long.parseLong(searchvalue);
                    newID = id - 2654435L;
                }else {
                    System.out.println("ERROR");
                }
            }
        }

        if (searchvalue == "") {
            properties = properties;
        }

        if (!containsOnlyDigits) {
            Pageable pageable = PageRequest.of(0, 20);
            Page<SearchDto> results = propertyRepository.findAllByNameContainingSearch(searchvalue, pageable);
            List<SearchDto> propertyList =  results.getContent();
            for (int i = 0; i < propertyList.size(); i++) {
                properties.add(propertyList.get(i).getName() + ", " + "(" + propertyList.get(i).getLoginId() + "), " + propertyList.get(i).getCity() + ", " + propertyList.get(i).getCountry());
            }
        }else {
            SearchDto property = propertyRepository.findByIdForSearch(newID);
            if (property != null) {
                properties.add(property.getLoginId()+ ", " + property.getName());
            }
        }

        return properties;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/searchByValue")
    public ModelAndView searchByValue(HttpServletRequest request, @RequestParam(value = "searchValue") String searchValue) {

        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            System.out.println("Search Value: " + searchValue);
            String mySearchValue = searchValue.split(",")[0].trim(); // Simplify the extraction of the main search value

            // Check if the search value contains only digits
            boolean containsOnlyDigits = mySearchValue.chars().allMatch(Character::isDigit);

            try {
                // Assuming property name search by default
                modelAndView.setViewName("ROLE_ADMIN/searchPages/propertyResults");
                List<SearchPropertyDTO> searchResults = propertyRepository.findAllByNameContainingIgnoreCase(mySearchValue);

                if (containsOnlyDigits) {
                    long id = Long.parseLong(mySearchValue);
                    long newID = id - 2654435L;
                    SearchPropertyDTO property = propertyRepository.findByIdForSearchResult(newID);

                    if (property != null) {
                        searchResults.add(property);
                    } else {
                        modelAndView.addObject("error", "No property found with the provided ID.");
                    }
                }

                if (searchResults.isEmpty()) {
                    modelAndView.addObject("error", "No results found for the search value: " + searchValue);
                } else {
                    modelAndView.addObject("searchresults", searchResults);
                }
            } catch (Exception e) {
                modelAndView.addObject("error", "An error occurred while processing your search. Please try again.");
                e.printStackTrace();
            }
        } else {
            modelAndView.setViewName("accessDenied");
            modelAndView.addObject("error", "You do not have the required permissions to perform this action.");
        }

        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/seasonalDeals")
    public ModelAndView searchByValueAndVersion(HttpServletRequest request, @RequestParam(value = "id") Long id, @RequestParam(value = "searchValue") String searchValue, @RequestParam(value = "version") int version) {
        ModelAndView modelAndView = new ModelAndView();

        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.addObject("searchresults", propertyRepository.findAllByNameContaining(searchValue).stream().filter(x -> x.getHotel_status().getId() != 3).collect(Collectors.toList()));
            Optional<Property> propertyOptional = propertyRepository.findById(id);

            if (propertyOptional.isPresent()) {
                Property property = propertyOptional.get();
                property.setSeasonalDeals(!property.isSeasonalDeals());
                propertyRepository.save(property);
            }
        }
        return modelAndView;
    }



//    @Autowired
//    GrandRoleRepository grandRoleRepository;
//    @GetMapping(value = "getGrantRole")
//    @ResponseBody
//    public GrandRole grandRole(@RequestParam(value = "id") Long id){
//        return grandRoleRepository.findById(id).get();
//    }

}
