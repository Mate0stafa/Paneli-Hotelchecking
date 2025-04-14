package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.NewProperty;
import com.example.paneli.Models.*;
import com.example.paneli.Models.Contract.Agreement;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.*;
import com.example.paneli.Repositories.UserPanel.RoleRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.DateService;
import com.example.paneli.Services.Mail.JavaMailService;
import com.example.paneli.Services.Number.NumberService;
import com.example.paneli.Services.ReviewService;
import com.example.paneli.Services.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class GroupAccountController {

    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    DateService dateService;
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
    AgreementRepository agreementRepository;
    @Autowired
    AddressRepostitory addressRepostitory;
    @Autowired
    PropertyCrudRepository propertyCrudRepository;
    @Autowired
    NumberService numberService;
    @Autowired
    HotelierRepository hotelierRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    HotelTimeRepository hotelTimeRepository;
    @Autowired
    HotelStatusRepository hotelStatusRepository;
    @Autowired
    HotelFacilityRepository hotelFacilityRepository;
    @Autowired
    ReviewService reviewService;
    @Autowired
    JavaMailService javaMailService;
    @Autowired
    ForgotPasswordController forgotPasswordController;
    @Autowired
    ToDoRepository todorepository;

    @PreAuthorize("hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/reviews")
    public ModelAndView reviews(@RequestParam(required = false) String datedOnes, HttpServletRequest request, ModelAndView modelAndView) throws ParseException {
        String dateFrom = "";
        String dateTo = "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        if (Objects.nonNull(datedOnes)) {
            List<String> splitedDate = Arrays.stream(datedOnes.split("-")).collect(Collectors.toList());
            dateFrom = splitedDate.get(0).trim();
            dateTo = splitedDate.get(1).trim();
        } else {
            LocalDate today = LocalDate.now();
            LocalDate oneMonthAgo = today.minusMonths(1);
            dateFrom = oneMonthAgo.format(formatter);
            dateTo = today.format(formatter);
        }
        modelAndView.addObject("dateFrom", dateFrom);
        modelAndView.addObject("dateTo", dateTo);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse(dateFrom);
        Date endDate = dateFormat.parse(dateTo);

        if (request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(authenticatedUser.getUsername());

            Role specialRolelogin = null;
            for (Role role : user.getRole()) {
                if (role.getId() != 1L && role.getId() != 2L && role.getId() != 3L) {
                    specialRolelogin = role;
                }
            }

            List<Property> properties = specialRolelogin.getProperties();
            Set<ReviewsTab> filteredReviewsTabs = new HashSet<>();
            modelAndView.addObject("reviewsTabs", filteredReviewsTabs);
            modelAndView.addObject("datedOnes", datedOnes);
            modelAndView.setViewName("GROUP_ACCOUNT/reviews");
        }

        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping("/addNewPropertyfromGroupAccount")
    public ModelAndView addNewPropertyfromGroupAccount(HttpServletRequest request, ModelAndView modelAndView) {

        if (request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(authenticatedUser.getUsername());

            Role specialRolelogin = null;
            for (Role role : user.getRole()) {
                if (role.getId() != 1L && role.getId() != 2L && role.getId() != 3L) {
                    specialRolelogin = role;
                }
            }

            NewProperty newProperty =new NewProperty();

            //Krijojme nje list e cila permban username-et ekzistues
            List<String> usernames = userRepository.findAllUsernames();

            // Set default values for latitude and longitude
            newProperty.setPropertyLongitude(String.valueOf(19.8187));
            newProperty.setPropertyLatitude(String.valueOf(41.3275));

            //Vendosim si defualt timezone Europe/Tirana
            ZanaTimeZone defaultTimezone = zanaTimeZoneRepository.findById(474L).orElse(null);
            if (defaultTimezone != null) {
                newProperty.setPropertyTimeZone(defaultTimezone.getName());
            }
            newProperty.setPropertyCountry("Albania");
            newProperty.setPropertyCounty("Tirana");
            newProperty.setPropertyCity("Tirane");
            newProperty.setPropertyZipCode("1001");
            List<Country> countries = countryRepository.findAll();



            modelAndView.addObject("usernames", usernames);
            modelAndView.addObject("specialRolelogin", specialRolelogin);
            modelAndView.addObject("user", user);
            modelAndView.addObject("hotelierId", new HotelierId());
            modelAndView.addObject("property", newProperty);
            modelAndView.addObject("categories", hotelTypeRepository.findAll());
            modelAndView.addObject("languages", languageRepository.findAll());
            modelAndView.addObject("timezone", zanaTimeZoneRepository.findAll());
            modelAndView.addObject("countries", countryRepository.findAll());
            modelAndView.addObject("cities", cityRepository.findAll());
            modelAndView.addObject("counties", countyRepository.findAll());
            modelAndView.setViewName("GROUP_ACCOUNT/addNewProperty");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/addNewPropertyfromGroupAccount")
    public String addNewPropertyfromGroupAccountPost(
            @RequestParam(value = "photoInput", required = false) MultipartFile photoInput,
            @RequestParam(value = "taxExtractInput", required = false) MultipartFile taxExtractInput,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            NewProperty property) {
        if (request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            //  Gjejme userin dhe rolin te akaundit te indentifikuar
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(authenticatedUser.getUsername());

            Role specialRolelogin = null;
            for (Role role : user.getRole()) {
                if (role.getId() != 1L && role.getId() != 2L && role.getId() != 3L) {
                    specialRolelogin = role;
                }
            }
            System.out.println("specialRolelogin : " + specialRolelogin);
            // ------------------------------------------------------

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

            property.setPropertyStars(Math.max(1, Math.min(5, property.getPropertyStars())));
            int numberOfStars = property.getPropertyStars();
            System.out.println("nrstars " + numberOfStars);

            Language language = languageRepository.findByName(property.getPropertyLanguage());
            String checkIN = property.getPropertyCheckIn();
            String checkOut = property.getPropertyCheckOut();


            ZanaTimeZone timeZone = zanaTimeZoneRepository.findByName(property.getPropertyTimeZone());
            Country country = countryRepository.findByCountry_name(property.getPropertyCountry());
            String city = property.getPropertyCity();
            String street = property.getPropertyStreet() != null ? property.getPropertyStreet() : "";
            String zipCode = property.getPropertyZipCode();
            String onMap = property.getPropertyLatitude() + " , " + property.getPropertyLongitude();
            String web = property.getPropertyWebAddress();
            String phone = property.getPropertyPhoneNumber();
            String hotelierId = property.getIdCard();
            String username = user.getUsername();
            String email = user.getEmail();

            try {
                // Create new property
                HotelTime hotelTime = new HotelTime(1, checkIN, checkOut, timeZone);
                hotelTimeRepository.save(hotelTime);
                HotelStatus hotelStatus = hotelStatusRepository.findById(1).get();
                Property dbProperty = new Property(
                        1, numberOfStars, numberOfRooms, true, propertyName, 0L, country.getCountry_name(),
                        hotel_type, hotelTime, hotelStatus, language, true,
                        property.getFirstName(), property.getLastName(), property.getNUIS(),
                        property.getDateofbirth(), property.getTaxname(), false
                );
                dbProperty.setAcceptCard(true);
                propertyCrudRepository.save(dbProperty);

                // Save HotelierId photo
                String uploadDir = "/home/allbookersusr/home/BookersDesk/data/uploads/hotelierId/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                if (photoInput != null && !photoInput.isEmpty()) {
                    String fileName = photoInput.getOriginalFilename().replaceAll("\\s+", "");
                    Path filePath = uploadPath.resolve(fileName);
                    Files.write(filePath, photoInput.getBytes());
                    HotelierId ht = new HotelierId(hotelierId, fileName, dbProperty);
                    hotelierRepository.save(ht);
                    dbProperty.setHotelierId(ht);
                    propertyRepository.save(dbProperty);
                    redirectAttributes.addFlashAttribute("savedFilePath", "/uploads/hotelierId/" + fileName);
                } else {
                    System.out.println("photoInput is null or empty");
                }

                // Save Tax Extract photo directly in Property
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

                ////creating address

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
                dbProperty.setChecked_out(true);
                propertyRepository.save(dbProperty);


                if (dbProperty.getAgreement() == null && agreementRepository.findByProperty(dbProperty) == null) {
                    Agreement agreement = new Agreement(
                            0,
                            address.getStreet() + ", " + address.getAddress_city() + ", " + property.getPropertyCounty() + ", " + property.getPropertyCountry(),
                            property.getFirstName(), property.getPropertyName(), property.getLastName(),
                            property.getPropertyAdminEmail(), property.getPropertyPhoneNumber(), property.getNUIS(),
                            new Date(), address.getCity(), dbProperty, property.getPropertyStreet(), property.getPropertyZipCode()
                    );
                    agreementRepository.save(agreement);
                }

                //  Set role for new property from group account
                if (specialRolelogin != null) {
                    if (dbProperty.getRoles() == null) {
                        dbProperty.setRoles(new ArrayList<>());
                    }
                    dbProperty.getRoles().add(specialRolelogin);
                    propertyRepository.save(dbProperty);
                    System.out.println("Prona e re u shtua me rolin special të caktuar!");
                } else {
                    System.out.println("Roli special nuk u gjet për këtë përdorues.");
                }

                Agreement agreement = agreementRepository.findByProperty(dbProperty);
                javaMailService.sendemailwhengroupaccountAddnewProperty(user, dbProperty, specialRolelogin, agreement);
                System.out.println("Email per pronen e re u dergua tek pronari ");
                javaMailService.sendemailwhengroupaccountAddnewPropertyAdmin(dbProperty, specialRolelogin);
                System.out.println("Admin notification");

                return "redirect:/";
            } catch (Exception e) {
                System.out.println("Error: " + e.getLocalizedMessage());
                return "redirect:/error";
            }
        }
        return "redirect:/";
    }

    @PreAuthorize("hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "addExistingPropertytoGroupAccount")
    public ModelAndView addExistingPropertytoGroupAccount(HttpServletRequest request, ModelAndView modelAndView) {

        if (request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(authenticatedUser.getUsername());
            modelAndView.addObject("user", user);
            modelAndView.setViewName("GROUP_ACCOUNT/AddExistingProperty");
        }
        return modelAndView;

    }

    @PreAuthorize("hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "foundProperty")
    public ModelAndView foundProperty(HttpServletRequest request, ModelAndView modelAndView,
                                      @RequestParam(value = "propertyId") Long propertyId) {
        if (request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(authenticatedUser.getUsername());
            modelAndView.addObject("user", user);

            Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
            if (optionalProperty.isPresent()) {
                Property property = optionalProperty.get();
                modelAndView.addObject("property", property);
            } else {
                propertyId = propertyId - 2654435L;
                optionalProperty = propertyRepository.findById(propertyId);
                if (optionalProperty.isPresent()) {
                    Property property = optionalProperty.get();
                    modelAndView.addObject("property", property);
                }else {
                    modelAndView.setViewName("GROUP_ACCOUNT/index");
                    System.out.println("Prona nuk u gjet");
                }
            }
            System.out.println("Property id : " + propertyId);
            modelAndView.setViewName("GROUP_ACCOUNT/foundProperty");
        }
        return modelAndView;
    }

    @PostMapping(value = "addPropertyToMyPrimaryAccount")
    @ResponseBody
    public ResponseEntity<String> addPropertyToMyPrimaryAccount(HttpServletRequest request,
                                                                @RequestParam(value = "propertyId") Long propertyId) {
        try {
            User currentLogedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());
            Property newproperty = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

            System.out.println("Login Username : " + currentLogedInUser.getUsername());

            // Gjej rolet e përdoruesit të loguar
            Role basicRolelogin = null;
            Role specialRolelogin = null;

            for (Role role : currentLogedInUser.getRole()) {
                if (role.getId() == 1L || role.getId() == 2L || role.getId() == 3L) {
                    basicRolelogin = role;
                } else {
                    specialRolelogin = role;
                }
            }

            // Kontrollo nëse prona ka përdorues me rolin bazik GROUP_ACCOUNT
            boolean hasGroupAccountUser = newproperty.getRoles()
                    .stream()
                    .flatMap(role -> role.getUsers().stream())
                    .flatMap(user -> user.getRole().stream())
                    .anyMatch(role -> role.getId() == 3L);

            if (hasGroupAccountUser) {
                return ResponseEntity.badRequest().body("The property already has a user with the GROUP_ACCOUNT role.");
            }

            if (basicRolelogin != null && specialRolelogin != null &&
                    "ROLE_GROUP_ACCOUNT".equals(basicRolelogin.getAuthority())) {

                if (newproperty.getRoles() == null) {
                    newproperty.setRoles(new ArrayList<>());
                }

                newproperty.getRoles().add(specialRolelogin);
                propertyRepository.save(newproperty);

                javaMailService.sendemailwhengroupaccountAddExistingProperty(currentLogedInUser, newproperty, specialRolelogin );
                System.out.println("Emaili u dergua me sukses ne userin e indentifikuar =>  " + currentLogedInUser.getEmail());
                javaMailService.sendemailwhengroupaccountAddExistingPropertyAdmin(newproperty, specialRolelogin);
                System.out.println("Admin notification");

                return ResponseEntity.ok("Property added successfully.");
            } else {
                return ResponseEntity.badRequest().body("Required roles not found or insufficient permissions.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while adding the property: " + e.getMessage());
        }
    }

    @PostMapping(value = "removePropertyFromMyPrimaryAccount")
    @ResponseBody
    public ResponseEntity<String> removePropertyFromMyPrimaryAccount(HttpServletRequest request,
                                                                     @RequestParam(value = "propertyId") Long propertyId) {
        try {
            User currentLogedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());
            Property propertyToRemove = propertyRepository.findById(propertyId)
                    .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

            Boolean hasUser = propertyToRemove.getRoles()
                    .stream()
                    .flatMap(role -> role.getUsers().stream())
                    .flatMap(userr -> userr.getRole().stream())
                    .anyMatch(role -> role.getId() == 1L);

            if (!hasUser) {
                return ResponseEntity.badRequest().body("You need to create a basic user for this property to proceed.");
            }

            // Gjej rolet e përdoruesit të loguar
            Role specialRoleLogin = null;
            for (Role role : currentLogedInUser.getRole()) {
                if (role.getId() != 1L && role.getId() != 2L && role.getId() != 3L) {
                    specialRoleLogin = role;
                    break;
                }
            }

            if (specialRoleLogin == null) {
                return ResponseEntity.badRequest().body("User does not have a special role to remove property.");
            }

            // Kontrollo nëse prona ka rolin special të përdoruesit
            boolean hasSpecialRoleOnProperty = propertyToRemove.getRoles().contains(specialRoleLogin);

            if (!hasSpecialRoleOnProperty) {
                return ResponseEntity.badRequest().body("The property does not have the special role assigned to this user.");
            }

            // Heq rolin special nga prona
            propertyToRemove.getRoles().remove(specialRoleLogin);
            propertyRepository.save(propertyToRemove);

            javaMailService.sendemailwhengroupaccountRemovedProperty(currentLogedInUser, propertyToRemove,specialRoleLogin);
            System.out.println("Emaili u dergua me sukses ne userin e identifikuar =>  " + currentLogedInUser.getEmail());
            javaMailService.sendemailwhengroupaccountRemovedPropertyAdmin(propertyToRemove, specialRoleLogin);
            System.out.println("Admin notification");

            return ResponseEntity.ok("Property removed successfully from the group account.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while removing the property: " + e.getMessage());
        }
    }

    @Autowired
    private ToDoService toDoService;

    @PreAuthorize("hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/propertydashboard")
    public ModelAndView getPropertyDashboard(@RequestParam(value = "propertyId") Long propertyId,
                                             HttpServletRequest request,
                                             ModelAndView modelAndView) throws ParseException {
        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

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

        if (hasAccess) {
            long startTime = System.currentTimeMillis();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(authenticatedUser.getUsername());

            Role role = new Role();

            for (int i = 0; i < user.getRole().size(); i++) {
                if (user.getRole().get(i).getProperties() != null) {
                    role = user.getRole().get(i);
                }
            }

            Map<Integer, Double> ratingPercentages = reviewService.getRatingPercentages(property);
            modelAndView.addObject("ratingPercentages", ratingPercentages);

            modelAndView.addObject("user", user);
            modelAndView.addObject("mes", reviewService.getReviewSum(property));
            modelAndView.addObject("property", property);
            int propSize = user.getRole().size() / 2;
            modelAndView.addObject("propSize", propSize);
            modelAndView.addObject("thirrje", request);
            List<ToDo> todos = toDoService.getToDoByProperty(property.getId());
            modelAndView.addObject("todos", todos);
            long endTime = System.currentTimeMillis();
            double durationInSeconds = (endTime - startTime) / 1000.0; // konvertohet në sekonda
            System.out.println("Koha e ekzekutimit: " + durationInSeconds + " sekonda");

            modelAndView.setViewName("ROLE_USER/index");
        }
        return modelAndView;
    }
}
