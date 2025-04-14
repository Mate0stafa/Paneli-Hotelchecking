package com.example.paneli.Controllers;

import com.example.paneli.Models.*;
import com.example.paneli.Models.Contract.Agreement;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.*;
import com.example.paneli.Repositories.UserPanel.RoleRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.DataObjects.NewProperty;
import com.example.paneli.Services.Mail.JavaMailService;
import com.example.paneli.Services.Number.NumberService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpServletRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class NewPropertyController {
    @Autowired
    JavaMailService javaMailService;
    @Autowired
    Hotel_TypeRepository hotelTypeRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    ZanaTimeZoneRepository zanaTimeZoneRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    CountyRepository countyRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    HotelTimeRepository hotelTimeRepository;
    @Autowired
    HotelStatusRepository hotelStatusRepository;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    AddressRepostitory addressRepostitory;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PropertyCrudRepository propertyCrudRepository;
    @Autowired
    NumberService numberService;
    @Autowired
    HotelierRepository hotelierRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addNewProperty")
    public ModelAndView shtoprone(HttpServletRequest request, ModelAndView modelAndView) {

        if (request.isUserInRole("ROLE_ADMIN")) {

            modelAndView.setViewName("ROLE_ADMIN/Property/addProperty");

            //Krijojme nje list e cila permban emails
            List<String> emails = userRepository.findAllEmails();
            modelAndView.addObject("emails", emails);

            //Krijojme nje list e cila permban username-et ekzistues
            List<String> usernames = userRepository.findAllUsernames();
            modelAndView.addObject("usernames", usernames);

            NewProperty newProperty = new NewProperty();

            // Set default values for latitude and longitude
            newProperty.setPropertyLongitude(String.valueOf(19.8187));
            newProperty.setPropertyLatitude(String.valueOf(41.3275));

            //Vendosim si defualt timezone Europe/Tirana
            ZanaTimeZone defaultTimezone = zanaTimeZoneRepository.findById(474L).orElse(null);
            if (defaultTimezone != null) {
                newProperty.setPropertyTimeZone(defaultTimezone.getName());
            }
            newProperty.setPropertyCountry("Albania");
            List<Country> countries = countryRepository.findAll();

            modelAndView.addObject("hotelierId", new HotelierId());
            modelAndView.addObject("property",newProperty);
            modelAndView.addObject("categories", hotelTypeRepository.findAll());
            modelAndView.addObject("languages", languageRepository.findAll());
            modelAndView.addObject("timezone", zanaTimeZoneRepository.findAll());
            modelAndView.addObject("defaultTimezone", defaultTimezone);
            modelAndView.addObject("countries", countries);
            modelAndView.addObject("propertyCountry", "Albania");
            modelAndView.addObject("cities", cityRepository.findAll());
            modelAndView.addObject("counties", countyRepository.findAll());

        }
        return modelAndView;
    }

    @Autowired
    AgreementRepository agreementRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/addNewProperty")
    public String addNewProperty(
            @RequestParam(value = "photoInput", required = false) MultipartFile photoInput,
            @RequestParam(value = "taxExtractInput", required = false) MultipartFile taxExtractInput,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            NewProperty property) {

        if (request.isUserInRole("ROLE_ADMIN")) {
            String propertyName = property.getPropertyName();
            System.out.println("propertyName: " + propertyName);

            Hotel_Type hotel_type = hotelTypeRepository.findByType(property.getPropertyCategory());
            System.out.println("hoteltype: " + hotel_type.getType());

            int numberOfRooms = property.getPropertyRooms();
            System.out.println("nrooms" + numberOfRooms);

            County county = new County();
            if (!(property.getPropertyCounty() == "other")) {
                county = countyRepository.findByName(property.getPropertyCounty());
            } else {
                county = countyRepository.findByName("Albania");
            }

            property.setPropertyStars(property.getPropertyStars() <= 0 ? 1 : property.getPropertyStars());
            property.setPropertyStars(property.getPropertyStars() >= 6 ? 5 : property.getPropertyStars());
            int numberOfStars = property.getPropertyStars();
            System.out.println("nrstars " + numberOfStars);

            Language language = languageRepository.findByName(property.getPropertyLanguage());
            System.out.println("languaganame " + language.getName());

            String checkIN = property.getPropertyCheckIn();
            System.out.println("checkIn: " + checkIN);

            String checkOut = property.getPropertyCheckOut();
            System.out.println("checkOut: " + checkOut);

            ZanaTimeZone timeZone = zanaTimeZoneRepository.findByName(property.getPropertyTimeZone());
            System.out.println("timezone: " + timeZone.getName());

            Country country = countryRepository.findByCountry_name(property.getPropertyCountry());
            System.out.println("country: " + country.getCountry_name());

            String city = property.getPropertyCity();
            System.out.println("City: " + city);

            String street = "";
            if ((property.getPropertyStreet() != "") || (property.getPropertyStreet() != null)) {
                street = property.getPropertyStreet();
            }
            System.out.println("street: " + street);

            String zipCode = property.getPropertyZipCode();
            System.out.println("onmap: " + zipCode);

            String onMap = property.getPropertyLatitude() + " , " + property.getPropertyLongitude();
            System.out.println("onMap: " + onMap);

            String web = property.getPropertyWebAddress();
            System.out.println("web: " + web);

            String phone = property.getPropertyPhoneNumber();
            System.out.println("phone: " + phone);

            String hotelierId = property.getIdCard();
            System.out.println("hotelierId: " + hotelierId);

            String username = "";
            if ((property.getPropertyAdminName() != null) || (property.getPropertyAdminName() != "")) {
                username = property.getPropertyAdminName().replaceAll(" ", "");
                if (userRepository.findAllByUsername(username).size() > 1) {
                    username = username + numberService.generateThreedigitNumber();
                }
            } else {
                username = propertyName.replaceAll(" ", "");
                if (userRepository.findAllByUsername(username).size() > 1) {
                    username = username + numberService.generateThreedigitNumber();
                }
            }
            System.out.println("username: " + username);

            String email = "";
            if (property.getPropertyAdminEmail() != "") {
                email = property.getPropertyAdminEmail();
            } else {
                email = propertyName + "@allbookers.com";
            }
            System.out.println("email: " + email);

            List<Role> userRoles = new ArrayList<>();

            try {
                String hotelNameForRole = propertyName.toString().replaceAll(" ", "_").toUpperCase();
                String roleName = "ROLE_ADMIN_HOTEL_" + hotelNameForRole;
                int roles = roleRepository.findAllByAuthority(roleName).size();
                roles = roles + 1;
                roleName = (roles > 0 ? roleName + "_" + roles + numberService.generateThreedigitNumber() : roleName + numberService.generateThreedigitNumber());
                Role role = new Role(roleName);
                userRoles.add(role);
                roleRepository.saveAndFlush(role);

                List<Role> rolesList = new ArrayList<>();
                rolesList.add(role);

                HotelTime hotelTime = new HotelTime(1, checkIN, checkOut, timeZone);
                hotelTimeRepository.save(hotelTime);
                HotelStatus hotelStatus = hotelStatusRepository.findById(1).get();
                Long a = Long.valueOf(0);
                Property dbProperty = new Property(
                        1,                           // version
                        numberOfStars,               // stars
                        numberOfRooms,               // number_of_rooms
                        true,                        // checked_out
                        propertyName,                // name
                        a,                           // offsetId
                        country.getCountry_name(),   // country
                        hotel_type,                  // hotel_type
                        hotelTime,                   // hotel_time
                        true,                        // acceptCard
                        rolesList,                   // roles
                        hotelStatus,                 // hotel_status
                        language,                    // language
                        true,                        // seasonalDeals
                        property.getFirstName(),     // firstName
                        property.getLastName(),      // lastName
                        property.getNUIS(),          // NUIS
                        false                        // promote
                );
                propertyCrudRepository.save(dbProperty);

                // Save HotelierId photo
                String uploadDirHotel = "/home/allbookersusr/home/BookersDesk/data/uploads/hotelierId/";
                Path uploadPathHotel = Paths.get(uploadDirHotel);
                if (!Files.exists(uploadPathHotel)) {
                    Files.createDirectories(uploadPathHotel);
                }
                if (photoInput != null && !photoInput.isEmpty()) {
                    byte[] bytes = photoInput.getBytes();
                    String fileName = photoInput.getOriginalFilename();
                    String sanitizedFileName = fileName.replaceAll("\\s+", "");
                    Path filePath = uploadPathHotel.resolve(sanitizedFileName);
                    Files.write(filePath, bytes);
                    HotelierId ht = new HotelierId(hotelierId, sanitizedFileName, dbProperty);
                    hotelierRepository.save(ht);
                    dbProperty.setHotelierId(ht);
                    propertyRepository.save(dbProperty);
                    String savedFilePath = "/uploads/hotelierId/" + sanitizedFileName;
                    redirectAttributes.addFlashAttribute("savedFilePath", savedFilePath);
                }

                String taxExtractFileName = null;
                if (taxExtractInput != null && !taxExtractInput.isEmpty()) {
                    String uploadDirTax = "/home/allbookersusr/home/BookersDesk/data/uploads/taxExtracts/";
                    Path uploadPathTax = Paths.get(uploadDirTax);
                    if (!Files.exists(uploadPathTax)) {
                        Files.createDirectories(uploadPathTax);
                    }
                    String fileName = taxExtractInput.getOriginalFilename().replaceAll("\\s+", "");
                    Path filePath = uploadPathTax.resolve(fileName);
                    Files.write(filePath, taxExtractInput.getBytes());
                    dbProperty.setTaxExtractFileName(fileName);
                    System.out.println("Tax extract saved at: " + filePath);
                } else {
                    System.out.println("taxExtractInput is null or empty");
                }

                // Create Address
                Address address = new Address(
                        phone,
                        city,
                        onMap,
                        zipCode,
                        email,
                        "https://" + web,
                        street,
                        cityRepository.findByFull_name(city),
                        dbProperty
                );
                addressRepostitory.save(address);
                dbProperty.setAddress(address);
                propertyRepository.save(dbProperty);

                // Declare Agreement outside the if block
                if (dbProperty.getAgreement() == null && agreementRepository.findByProperty(dbProperty) == null) {
                    Agreement agreement = new Agreement(
                            0, address.getStreet() + ", " + address.getAddress_city() + ", " +
                            property.getPropertyCounty() + ", " + property.getPropertyCountry(),
                            property.getFirstName(), property.getPropertyName(), property.getLastName(),
                            property.getPropertyAdminEmail(), property.getPropertyPhoneNumber(),
                            property.getNUIS(), new Date(), cityRepository.findByFull_name(city),
                            dbProperty, property.getPropertyStreet(), property.getPropertyZipCode()
                    );
                    agreementRepository.save(agreement);
                    dbProperty.setAgreement(agreement);
                }

                propertyRepository.save(dbProperty);

                // Create User
                String password = "Bomba2021!";
                password = bCryptPasswordEncoder.encode(password);
                User userExists = userRepository.findByFull_nameAndUsername(property.getPropertyAdminName(), username);
                User user = null;
                if (userExists == null) {
                    user = new User(property.getPropertyAdminName(), email, username + numberService.generateThreedigitNumber(),
                            password, true);
                    Role role1 = roleRepository.findAllByAuthority("ROLE_USER").get(0);
                    userRoles.add(role1);
                    user.setRole(userRoles);
                    userRepository.saveAndFlush(user);
                } else {
                    user = new User(property.getPropertyAdminName(), email, username + numberService.generateThreedigitNumber(),
                            password, true);
                    Role role1 = roleRepository.findAllByAuthority("ROLE_USER").get(0);
                    userRoles.add(role1);
                    user.setRole(userRoles);
                    userRepository.saveAndFlush(user);
                }

                dbProperty.setChecked_out(true);
                propertyRepository.save(dbProperty);

                Agreement agreement = dbProperty.getAgreement();
                if (agreement != null) { // Only send email if agreement was created
                    javaMailService.sendemailwhenAddnewProperty(user, dbProperty, agreement);
                    System.out.println("Emaili u dergua");
                }

                return "redirect:/";
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        return "redirect:/";
    }


    //kontroller per listat e country dhe citi ne momentin qe shtojme prone te re

    @ResponseBody
    @GetMapping("/counties-by-country")
    public ResponseEntity<List<Map<String, Object>>> getCountiesByCountry(@RequestParam String countryName) {
        try {
            List<County> counties = countyRepository.findByCountry_country_name(countryName);
            List<Map<String, Object>> response = counties.stream()
                    .map(county -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", county.getId());
                        map.put("name", county.getName());
                        return map;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ResponseBody
    @GetMapping("/cities-by-county")
    public ResponseEntity<List<Map<String, Object>>> getCitiesByCounty(@RequestParam String countyName) {
        try {
            List<City> cities = cityRepository.findByCounty_name(countyName);
            List<Map<String, Object>> response = cities.stream()
                    .map(city -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", city.getId());
                        map.put("full_name", city.getFull_name());
                        return map;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}