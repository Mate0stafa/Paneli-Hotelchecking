package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.RequestChanges;
import com.example.paneli.Models.*;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.*;
import com.example.paneli.Repositories.UserPanel.RoleRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.Mail.JavaMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller

public class RoleUserPropertyController {

    @Autowired
    AgreementRepository agreementRepository;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    HotelTimeRepository hotelTimeRepository;
    @Autowired
    ZanaTimeZoneRepository zanaTimeZoneRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HotelStatusRepository hotelStatusRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    Hotel_TypeRepository hotel_typeRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    AddressRepostitory addressRepostitory;
    @Autowired
    JavaMailService javaMailService;
    @Autowired
    AddressChangeRepository addressChangeRepository;


    @PreAuthorize("hasRole('ROLE_GROUP_ACCOUNT') OR hasRole('ROLE_USER')")
    @GetMapping(value = "/myProperty")
    public ModelAndView getPropertyDetails(@RequestParam(value = "id") Long id, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        boolean hasAccess = property.getRoles().contains(specialRole);
        long startTime = System.currentTimeMillis();
        // Kontrollo nëse përdoruesi ka rolin 'ROLE_GROUP_ACCOUNT' në mënyrë më efikase
        boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        long endTime = System.currentTimeMillis();
        double durationInSeconds = (endTime - startTime) / 1000.0; // konvertohet në sekonda
        System.out.println("Koha e ekzekutimit qe vonon : " + durationInSeconds + " sekonda");
        long startTime2 = System.currentTimeMillis();
        modelAndView.addObject("specialRole", specialRole);
        long endTime2 = System.currentTimeMillis();
        double durationInSeconds2 = (endTime2 - startTime2) / 1000.0; // konvertohet në sekonda
        System.out.println("Koha e ekzekutimit2: " + durationInSeconds2 + " sekonda");
        long startTime3 = System.currentTimeMillis();
        modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
        long endTime3 = System.currentTimeMillis();
        double durationInSeconds3 = (endTime3 - startTime3) / 1000.0; // konvertohet në sekonda
        System.out.println("Koha e ekzekutimit3: " + durationInSeconds3 + " sekonda");
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            modelAndView.addObject("property", property);
            modelAndView.addObject("propertylanguages", property.getLanguages());
            modelAndView.addObject("countries", countryRepository.findAllCountryNames());
            modelAndView.addObject("languagesall", languageRepository.findAllIdsAndNames());
            modelAndView.addObject("hotelTypes", hotel_typeRepository.findAllIdsAndTypes());

            modelAndView.setViewName("ROLE_USER/Property/propertyDetails");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping (value = "/myProperty")
    public String editPropertyBasic(@RequestParam(value = "id") Long id, HttpServletRequest request, Property property){
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property dbProperty = propertyRepository.findById(id).get();

        boolean hasAccess = dbProperty.getRoles().contains(specialRole);

        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {

            for (int i = 0; i<property.getLanguages().size();i++){
                System.out.println(property.getLanguages().get(i).getName());
            }
            // System.out.println(property.getHotel_status().getStatus());

//            dbProperty.setName(property.getName());

            dbProperty.setNumber_of_rooms(property.getNumber_of_rooms());
            dbProperty.setCountry(property.getCountry());
            System.out.println(property.getCountry());
            System.out.println(property.getRealId() + " vcyudhsbvjiksdniovnhiedsvfd");

            Hotel_Type hotel_type = hotel_typeRepository.findById(property.getRealId()).get();
            dbProperty.setHotel_type(hotel_type);
            System.out.println("hotel typeeeeeeeee " + dbProperty.getHotel_type());
            dbProperty.setLanguage(languageRepository.findById(property.getLanguage().getId()).get());
            // property.setStars(property.getStars());
            dbProperty.setLanguages(property.getLanguages());
            // dbProperty.setHotel_status(property.getHotel_status());
            property.setStars(property.getStars() <= 0 ? 1 : property.getStars());
            property.setStars(property.getStars() >= 6 ? 5 : property.getStars());
            dbProperty.setStars(property.getStars());

            propertyRepository.save(dbProperty);




        }
        return "redirect:/myProperty?id=" + id;
    }


    @Autowired
    PropertyChangeRepository propertyChangeRepository;
    @PostMapping(value="/requestChangesPropertyName")
    @ResponseBody
    public Boolean requestChangesPropertyName (@RequestParam(name = "id") Long propertyId, @RequestBody RequestChanges requestChanges) throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(authenticatedUser.getUsername());
        System.out.println(propertyId + " p");
        Property property = propertyRepository.findById(propertyId).get();


        PropertyChange propertyChange =  property.getPropertyChange() == null ? new PropertyChange() : property.getPropertyChange();
        propertyChange.setPropertyName(requestChanges.getPropertyName());
        propertyChange.setProperty(property);
        property.setPropertyChange(true);
        propertyRepository.save(property);
        propertyChangeRepository.save(propertyChange);

        String adminEmail = userRepository.findAdminEmail();
        System.out.println(adminEmail + "  email i req ");
        javaMailService.sendRequestPropertyName(user, property, propertyChange, adminEmail);

        return true;

    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/addressDetails")
    public ModelAndView editAddressData(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        boolean hasAccess = property.getRoles().contains(specialRole);

        boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        modelAndView.addObject("specialRole", specialRole);
        modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            modelAndView.setViewName("ROLE_USER/Property/addressDetails");
            modelAndView.addObject("property", property);
            modelAndView.addObject("address", property.getAddress());
            modelAndView.addObject("cities", cityRepository.findAll());
        }

        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/addressDetails")
    public String submitAddressDetails(@RequestParam(value = "id") Long id, HttpServletRequest request, @ModelAttribute Property property) {
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property dbProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        boolean hasAccess = dbProperty.getRoles().contains(specialRole);

        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            Address objAddress = property.getAddress();
            Address address = addressRepostitory.findByProperty(dbProperty);

            address.setAddress_city(objAddress.getAddress_city());
            address.setOn_map(objAddress.getOn_map());
            address.setWebsite(objAddress.getWebsite());

            addressRepostitory.save(address);
        }

        return "redirect:/addressDetails?id=" + id;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/requestChanges")
    @ResponseBody
    public Boolean requestChanges(@RequestParam(name = "id") Long propertyId, @RequestBody RequestChanges requestChanges, HttpServletRequest request) throws MessagingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        User currentLoggedInUser = userRepository.findByUsername(authenticatedUser.getUsername());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

        boolean hasAccess = property.getRoles().contains(specialRole);

        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {

            AddressChange addressChange = property.getAddressChange() == null ? new AddressChange() : property.getAddressChange();

            addressChange.setProperty(property);
            addressChange.setStreetName(requestChanges.getStreetName());
            addressChange.setCityId(requestChanges.getCityId());
            addressChange.setCityName(requestChanges.getCityName());
            addressChange.setZipCode(requestChanges.getZipCode());

            addressChangeRepository.save(addressChange);

            property.setAddressChange(addressChange);
            propertyRepository.save(property);

            String adminEmail = userRepository.findAdminEmail();
            System.out.println("adminEmail: " + adminEmail);
            javaMailService.requestForChanges(currentLoggedInUser, property, requestChanges, adminEmail);
        }
        return true;
    }

    @Autowired
    HotelFacilityRepository hotelFacilityRepository;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/myPropertyFacilities")
    public ModelAndView getFacilitiesDetails(@RequestParam(value = "id") Long id, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        List<HotelFacility> hotelFacilitess = hotelFacilityRepository.findAll();
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        boolean hasAccess = property.getRoles().contains(specialRole);

        boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        modelAndView.addObject("specialRole", specialRole);
        modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            modelAndView.addObject("property", property);
            modelAndView.addObject("hotelFacilites", hotelFacilitess);
            hotelFacilitess.forEach(s-> System.out.println(s.getName()));
            modelAndView.setViewName("ROLE_USER/Property/myPropertyFacilities");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/myPropertyFacilities")
    public String FacilitiesDetails(@RequestParam(value = "id") Long id, HttpServletRequest request, Property property){
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property dbProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        boolean hasAccess = dbProperty.getRoles().contains(specialRole);

        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            List<HotelFacility> hotelFacilities = property.getHotel_facility();
            for (int i=0;i<hotelFacilities.size();i++){
                System.out.println(hotelFacilities.get(i).getName());
            }
            property.setHotel_facility(property.getHotel_facility());
            dbProperty.setHotel_facility(property.getHotel_facility());
            propertyRepository.save(dbProperty);
        }

        return "redirect:/myPropertyFacilities?id=" + id;
    }

    @Autowired
    HotelAttributeRepository hotelAttributeRepository;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/propertyAttributes")
    public ModelAndView getAttributeDetails(@RequestParam(value = "id") Long id, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        boolean hasAccess = property.getRoles().contains(specialRole);

        boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        modelAndView.addObject("specialRole", specialRole);
        modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            modelAndView.setViewName("ROLE_USER/Property/propertyAttributes");
            modelAndView.addObject("property", property);
            modelAndView.addObject("propertyAttribute", hotelAttributeRepository.findAll());
        }
        return modelAndView;
    }



    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/propertyAttributes")
    public ModelAndView AttributeDetails(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, Property property){
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property dbProperty = propertyRepository.findById(id).get();

        boolean hasAccess = dbProperty.getRoles().contains(specialRole);

        boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        modelAndView.addObject("specialRole", specialRole);
        modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            modelAndView.setViewName("ROLE_USER/Property/propertyAttributes");

            List<HotelAttribute> hotelAttributes = property.getHotel_attribute();
            for (int i=0;i<hotelAttributes.size();i++){
                System.out.println(hotelAttributes.get(i).getName());
            }
            property.setHotel_attribute(property.getHotel_attribute());
            dbProperty.setHotel_attribute(property.getHotel_attribute());
            propertyRepository.save(dbProperty);

            modelAndView.addObject("property", dbProperty);
            modelAndView.addObject("propertyAttribute", hotelAttributeRepository.findAll());
        }

        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/propertyContactDetails")
    public ModelAndView editContactDetails(@RequestParam(value = "id") Long id, ModelAndView modelAndView, HttpServletRequest request){
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        boolean hasAccess = property.getRoles().contains(specialRole);

        boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        modelAndView.addObject("specialRole", specialRole);
        modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return modelAndView;
        }
        if (request.isUserInRole(specialRole.getAuthority())|| hasAccess){
            modelAndView.setViewName("ROLE_USER/Property/propertyContactDetails");
            modelAndView.addObject("property", property);

        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/propertyContactDetails")
    public String editContactDetailsPost(@RequestParam(value = "id") Long id, HttpServletRequest request, Property property, ModelAndView modelAndView) {
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property dbProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        boolean hasAccess = dbProperty.getRoles().contains(specialRole);

        boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                .anyMatch(role -> role.getId() == 3L);
        modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
        modelAndView.addObject("specialRole", specialRole);
        modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
        if (!hasAccess) {
            modelAndView.setViewName("/error");
            return "redirect:/error";
        }
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            Address dbAddress = dbProperty.getAddress();
            Address objAddress = property.getAddress();
            dbAddress.setEmail(objAddress.getEmail());
            dbAddress.setTelephone(objAddress.getTelephone());

            dbProperty.setFirstName(property.getFirstName());
            dbProperty.setLastName(property.getLastName());
            propertyRepository.saveAndFlush(dbProperty);
            addressRepostitory.save(dbAddress);

        }
        return "redirect:/propertyContactDetails?id=" + id;
    }
}