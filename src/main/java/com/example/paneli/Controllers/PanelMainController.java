package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.NewPassAfterRegister;
import com.example.paneli.DataObjects.Property.FacilityHotelProjection;
import com.example.paneli.DataObjects.PropertyTypeDto;
import com.example.paneli.Models.*;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.*;
import com.example.paneli.Repositories.UserPanel.RoleRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.*;
import com.example.paneli.Services.Mail.JavaMailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PanelMainController {
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    PropertyService propertyService;
    @Autowired
    Hotel_TypeRepository hotelTypeRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    ZanaTimeZoneRepository zanaTimeZoneRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CountyRepository countyRepository;
    @Autowired
    CityRepository cityRepository;

    @Autowired
    HotelFacilityRepository hotelFacilityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewService reviewService;

    @Autowired
    DateService dateService;

    @Autowired
    HotelStatusRepository hotelStatusRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JavaMailService javaMailService;

    @Autowired
    ForgotPasswordController forgotPasswordController;

    @Autowired
    private ToDoService toDoService;

    @Autowired
    ToDoRepository todorepository;

    /***TwoFactorAuth get mapping*/
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping("/")
    public ModelAndView verifyCode(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws ParseException, IOException {

        if (request.getSession().getAttribute("goToReservationAfterLogin")!= null) {
            String url = (String) request.getSession().getAttribute("goToReservationAfterLogin");
            request.getSession().removeAttribute("goToReservationAfterLogin");
            response.sendRedirect(url);
        }

        if (request.isUserInRole("ROLE_USER")) {

            // Merr përdoruesin aktual
            User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

            // Gjej rolin special të përdoruesit
            Role specialRole = currentLoggedInUser.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

            Property property = specialRole.getProperties().get(0);

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

                long startTime = System.currentTimeMillis();
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
                User user = userRepository.findByUsername(authenticatedUser.getUsername());

                Map<Integer, Double> ratingPercentages = reviewService.getRatingPercentages(property);
                modelAndView.addObject("ratingPercentages", ratingPercentages);

                modelAndView.addObject("user", user);
                modelAndView.addObject("mes", reviewService.getReviewSum(property));
                modelAndView.addObject("property", property);
                int propSize = user.getRole().size() / 2;
                modelAndView.addObject("propSize", propSize);
                modelAndView.addObject("thirrje", request);

                long endTime = System.currentTimeMillis();
                double durationInSeconds = (endTime - startTime) / 1000.0; // konvertohet në sekonda
                System.out.println("Koha e ekzekutimit: " + durationInSeconds + " sekonda");

                modelAndView.setViewName("ROLE_USER/index");
            }
        } else if (request.isUserInRole("ROLE_ADMIN")) {
            long startTime = System.currentTimeMillis();
            Date date = new Date();
            modelAndView.addObject("suspendProperty", propertyRepository.countByHotelStatus(3));
            modelAndView.addObject("propertyNumber", propertyRepository.count());
            long endTime = System.currentTimeMillis();
            double durationInSeconds = (endTime - startTime) / 1000.0; // konvertohet në sekonda
            System.out.println("Koha e ekzekutimit: " + durationInSeconds + " sekonda");

            long startTime2 = System.currentTimeMillis();
            modelAndView.addObject("users", userRepository.count());
            modelAndView.addObject("massive", cityRepository.findAllByPromote(true));
            long endTime2 = System.currentTimeMillis();
            double durationInSeconds2 = (endTime2 - startTime2) / 1000.0; // konvertohet në sekonda
            System.out.println("Koha e ekzekutimit2: " + durationInSeconds2 + " sekonda");

            modelAndView.addObject("todayDate", new Date());
            modelAndView.setViewName("ROLE_ADMIN/index");


        } else if (request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            System.out.println("ROLE_GROUP_ACCOUNT-------------------------------------> Open");
            User usertest = userRepository.findByUsername(request.getUserPrincipal().getName());

            System.out.println("+++++++++++++++>>>>>> " + usertest.getRole().get(0).getAuthority());
            System.out.println("+++++++++++++++>>>>>> " + usertest.getRole().get(1).getAuthority());

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(authenticatedUser.getUsername());

            //  Useri i indentifikuar
            Role basicRolelogin = null;  // Roli bazik (me ID = 1,2,3 )
            Role specialRolelogin = null;  // Roli unik
            List<Role> roleslogin = user.getRole();
            for (Role role : roleslogin) {
                System.out.println("===" + role.getId());
                if (role.getId() == 1L) {
                    basicRolelogin = role;
                } else if (role.getId() == 2L) {
                    basicRolelogin = role;
                } else if (role.getId() == 3L) {
                    basicRolelogin = role;
                }else {
                    specialRolelogin = role;
                }
            }
            System.out.println("00000 " + specialRolelogin.getAuthority() + "=Id=" + specialRolelogin.getId());
            List<Property> properties = specialRolelogin.getProperties();

            modelAndView.addObject("propSize", properties.size());
            modelAndView.addObject("properties", properties);

            Set<ReviewsTab> allReviews = new HashSet<>();
            for (Property property : properties) {
                if (property.getReviewsTabList() != null) {
                    allReviews.addAll(property.getReviewsTabList());
                }
            }
            modelAndView.addObject("allReviewsSize", allReviews.size());

            modelAndView.setViewName("GROUP_ACCOUNT/index");
        }

        return modelAndView;

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.POST, params = "_method=delete")
    @ResponseBody
    public ResponseEntity<?> deleteToDoById(@PathVariable Long id) {
        toDoService.deleteToDo(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping("/todos")
    @ResponseBody
    public ResponseEntity<?> createToDo(HttpServletRequest request,
                                        @RequestParam String description,
                                        @RequestParam Long propertyId) {
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

        boolean hasAccess = property.getRoles().contains(specialRole);

        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            ToDo newToDo = new ToDo();
            newToDo.setDescription(description);
            newToDo.setCompleted(false);
            newToDo.setProperty(property);
            ToDo savedTodo = toDoService.addToDo(newToDo);

            Map<String, Object> response = new HashMap<>();
            response.put("id", savedTodo.getId());
            response.put("description", savedTodo.getDescription());
            response.put("completed", savedTodo.getCompleted());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @RequestMapping(value = "/todos/{id}", method = RequestMethod.POST, params = "_method=put")
    @ResponseBody
    public ResponseEntity<?> updateToDo(@PathVariable Long id,
                                        @RequestParam Boolean completed) {
        ToDo existingToDo = toDoService.getToDoById(id);
        existingToDo.setCompleted(completed);
        toDoService.updateToDo(existingToDo);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "setNewPassAfterRegister")
    public ModelAndView setNewPassAfterRegister(ModelAndView modelAndView, HttpServletRequest request) {

        User user = userRepository.findByUsername(request.getUserPrincipal().getName());

        request.getSession().setAttribute("authenticatedUser", user);

        SecurityContextHolder.clearContext();

        modelAndView.addObject("user" , user);
        modelAndView.setViewName("setNewPassAfterRegister");
        return modelAndView;
    }

    @PostMapping("/processNewPassAfterRegister")
    @ResponseBody
    public Map<String, String> processNewPassAfterRegister(@RequestBody NewPassAfterRegister newPassAfterRegister, HttpServletRequest request) {

        Map<String, String> response = new HashMap<>();

        User user = (User) request.getSession().getAttribute("authenticatedUser");

        if (newPassAfterRegister.getUsername() != null && newPassAfterRegister.getPassword() != null && newPassAfterRegister.getUsername().equals(user.getUsername())) {

            com.example.paneli.Services.UserServices.UserDetails userDetails = new com.example.paneli.Services.UserServices.UserDetails(user);

            List<Role> userRoles = user.getRole();
            List<String> stringList = new ArrayList<>();
            for (int i = 0; i < userRoles.size(); i++) {
                stringList.add(userRoles.get(i).getAuthority());
            }

            // Convert the list of role strings to a list of GrantedAuthority objects
            List<GrantedAuthority> authorities = stringList.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // Create an Authentication object with the user's roles
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

            // Set the Authentication object to the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Set new password
            forgotPasswordController.changeUserPassword(user, newPassAfterRegister.getPassword());

            response.put("status", "success");

            if (user.getTwoFA() == null || user.getTwoFA()) {
                response.put("twofa", "1");
            } else {
                response.put("twofa", "0");
            }
        } else {
            response.put("status", "wrong");
        }

        return response;
    }

    @GetMapping(value = "/userPropertyDetalis")
    public ModelAndView userPropertyDetails(
            HttpServletRequest request,
            ModelAndView modelAndView,
            @RequestParam(value = "id") Long id){

        User user = userRepository.findByUsername(request.getUserPrincipal().getName());
        Property property = propertyRepository.findById(id).get();

        if (property.getRoles().get(0).getUsers().get(0)==user){
            modelAndView.addObject("user", user);
            modelAndView.addObject("property", property);
            int propSize = user.getRole().size()/2;
            modelAndView.addObject("propSize", propSize);
            modelAndView.addObject("thirrje", request);
            modelAndView.setViewName("ROLE_USER/index");
        }else {
            modelAndView.setViewName("loginandregister/login");
        }

        return modelAndView;

    }



    @ModelAttribute("citiesinpanel")
    public List<City> getAllCities(){
        return cityRepository.findAll();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countyList")
    public ModelAndView shihQark(HttpServletRequest request, ModelAndView modelAndView,
                                 /*@RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String search,*/
                                 @RequestParam(required = false) Long id){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.addObject("county", countyRepository.findAll());
            modelAndView.addObject("id",id);
            modelAndView.setViewName("ROLE_ADMIN/county/countyList");
        }
        return modelAndView;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/countryList")
    public ModelAndView shihShtet(HttpServletRequest request, ModelAndView modelAndView
                                 /*, @RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(required = false) String search*/) {
        if (request.isUserInRole("ROLE_ADMIN")){
            //ketu pagination nuk nevojitet
           /* Pageable pageable = PageRequest.of(page - 1, size);
            List<Country> countries;
            if (search != null && !search.isEmpty()) {
                countries = countryRepository.findByCountry_nameContainingIgnoreCase(search);
            } else {
                countries = countryRepository.findAllCountries();
            }*/

            modelAndView.addObject("country", countryRepository.findAll());
            /*modelAndView.addObject("search", search);*/
            modelAndView.setViewName("ROLE_ADMIN/country/countryList");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/propertyAmenities")
    public ModelAndView hotelfacility(

            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String search,
            ModelAndView modelAndView) {

        modelAndView.addObject("id",id);
        modelAndView.addObject("search", search);
        modelAndView.setViewName("ROLE_ADMIN/hotelFacility/facilityHotelList");

        return modelAndView;
    }

    @Autowired
    private MessageSource messageSource;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/search/amenitiesLists")
    public List<Map<String, Object>> facilityLists(@RequestParam(required = false) Long id,
                                                   @RequestParam(required = false) String search) {
        List<FacilityHotelProjection> facilityHotelProjections;

        if (id != null) {
            facilityHotelProjections = hotelFacilityRepository.getProjectionById(id);
        } else if (StringUtils.hasText(search)) {
            facilityHotelProjections = hotelFacilityRepository.getFacilityBySearch(search);
        } else {
            facilityHotelProjections = hotelFacilityRepository.getAll();
        }

        Locale currentLocale = LocaleContextHolder.getLocale();
        return facilityHotelProjections.stream()
                .map(facility -> {
                    String originalName = facility.getName();
                    String translationKey = "client." + originalName.replace(" ", "");
                    String translatedName = messageSource.getMessage(translationKey, null, originalName, currentLocale);
                    Map<String, Object> facilityMap = new HashMap<>();
                    facilityMap.put("id", facility.getId());
                    facilityMap.put("photo", facility.getPhoto());
                    facilityMap.put("name", translatedName);
                    facilityMap.put("description", facility.getDescription());
                    facilityMap.put("city", facility.getCity());
                    facilityMap.put("rooms", facility.getRooms());
                    facilityMap.put("status", facility.getStatus());
                    facilityMap.put("promote", facility.getPromote());
                    facilityMap.put("seasonal", facility.getSeasonal());

                    return facilityMap;
                })
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/propertytype")
    public ModelAndView hoteltype(HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/hotelType/typeList");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/propertytypeDTO")
    public ResponseEntity<List<PropertyTypeDto>> getHotelTypes(HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            List<PropertyTypeDto> hotelTypeList = hotelTypeRepository.findAllPropertyTypes();
            return ResponseEntity.ok(hotelTypeList);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

}

