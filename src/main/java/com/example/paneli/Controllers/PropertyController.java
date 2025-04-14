package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.Auth.UserRequest;
import com.example.paneli.DataObjects.Property.FacilityHotelProjection;
import com.example.paneli.Models.*;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.*;
import com.example.paneli.DataObjects.*;
import com.example.paneli.Repositories.UserPanel.RoleRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.AuthorizationService;
import com.example.paneli.Services.Mail.JavaMailService;
import com.example.paneli.Services.Number.NumberService;
import com.example.paneli.Services.Property.PropertyEncodeName;
import com.example.paneli.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PropertyController {

    @Autowired
    Hotel_TypeRepository hotel_typeRepository;
    @Autowired
    HotelFacilityRepository hotelFacilityRepository;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    LanguageRepository languageRepository;
    @Autowired
    HotelStatusRepository hotelStatusRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    CountyRepository countyRepository;
    @Autowired
    AddressRepostitory addressRepostitory;
    @Autowired
    ZanaTimeZoneRepository zanaTimeZoneRepository;
    @Autowired
    HotelTimeRepository hotelTimeRepository;
    @Autowired
    HotelAttributeRepository hotelAttributeRepository;
    @Autowired
    PropertyEncodeName propertyEncodeName;
    @Autowired
    AgreementRepository agreementRepository;
    @Autowired
    AddressChangeRepository addressChangeRepository;
    @Autowired
    JavaMailService javaMailService;
    @Autowired
    private HotelierRepository hotelierRepository;
    @Autowired
    private HotelPhotoRepository hotelPhotoRepository;
    @Autowired
    private PropertyChangeRepository propertyChangeRepository;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/showHotelType")
    public ModelAndView showHotelType(@RequestParam(value ="typeId") Long typeId, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.addObject("type", hotel_typeRepository.findById(Math.toIntExact(typeId)).get());
            System.out.println(hotel_typeRepository.findById(Math.toIntExact(typeId)).get().getType());
            modelAndView.setViewName("ROLE_ADMIN/hotelType/showHotelType");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/showHotelFacility")
    public ModelAndView showHotelFacility(@RequestParam(value ="facilityId") Long facilityId, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.addObject("hotelFacility", hotelFacilityRepository.findById(facilityId).get());
            modelAndView.setViewName("ROLE_ADMIN/hotelFacility/showHotelFacility");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editPropertyType")
    public ModelAndView editPropertyTypeGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")) {
            Optional<Hotel_Type> hotelTypeOptional = hotel_typeRepository.findById(Math.toIntExact(id));
            if (hotelTypeOptional.isPresent()) {
                Hotel_Type hotelType = hotelTypeOptional.get();
                modelAndView.setViewName("ROLE_ADMIN/hotelType/editHotelType");
                modelAndView.addObject("hotelType", hotelType);
                modelAndView.addObject("imagePath", "/uploads/" + hotelType.getFile_name()); // Assuming the image is stored in a directory named "uploads"
            }
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/promoteProperty")
    public ModelAndView promoteProperty(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Property not found"));

            boolean isPromoted = property.isPromote();
            if (!isPromoted) {
                property.setPromote(true);
                propertyRepository.save(property);
            } else {
                property.setPromote(false);
                property.setShowProperty(0);
                propertyRepository.save(property);
            }

            modelAndView.setViewName("redirect:/propertylist");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/propertylist")
    public ModelAndView shihPronat(
            HttpServletRequest request,
            ModelAndView modelAndView,
            HttpSession session,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long id
    ) {
        if (request.isUserInRole("ROLE_ADMIN")) {
             Pageable pageable = PageRequest.of(page - 1, size);

            Page<Property> propertyPage;
            if (StringUtils.hasText(search)) {
                long startTime1 = System.currentTimeMillis();
                propertyPage = propertyRepository.findByNameContainingIgnoreCase(search, pageable);
                long endTime1 = System.currentTimeMillis();
                double durationInSeconds1 = (endTime1 - startTime1) / 1000.0; // konvertohet në sekonda
                System.out.println("Koha e ekzekutimit1: " + durationInSeconds1 + " sekonda");
            } else if (id != null) {
                long startTime2 = System.currentTimeMillis();
                Long NewId = id - 2654435;
                propertyPage = propertyRepository.findById(NewId, pageable);
                long endTime2 = System.currentTimeMillis();
                double durationInSeconds2 = (endTime2 - startTime2) / 1000.0; // konvertohet në sekonda
                System.out.println("Koha e ekzekutimit2: " + durationInSeconds2 + " sekonda");
            } else {
                long startTime3 = System.currentTimeMillis();
//                propertyPage = propertyRepository.findAll(pageable);
                propertyPage = propertyRepository.findAllProperties(pageable);

                long endTime3 = System.currentTimeMillis();
                double durationInSeconds3 = (endTime3 - startTime3) / 1000.0; // konvertohet në sekonda
                System.out.println("Koha e ekzekutimit3: " + durationInSeconds3 + " sekonda");
            }

            session.setAttribute("page" , page);
            modelAndView.addObject("properties", propertyPage.getContent());
            modelAndView.addObject("page", propertyPage);
            modelAndView.addObject("search", search);
            modelAndView.addObject("id", id);

            modelAndView.setViewName("ROLE_ADMIN/Property/propertylist");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @GetMapping("/lists/Property")
    public List<FacilityHotelProjection> propertyLists(){
        long timestart = System.currentTimeMillis(),timeend;
        List<FacilityHotelProjection> projection =propertyRepository.getPropertyProjection();
        timeend =  System.currentTimeMillis() - timestart;
        System.out.println("time to get propertyList\t"+ timeend);
        return projection;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editPropertyType")
    public ModelAndView editPropertyType(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, Hotel_Type hotel_type,
                                         @RequestParam("file") MultipartFile file) throws IOException{
        if (request.isUserInRole("ROLE_ADMIN")) {
            try {
                Hotel_Type existingHotelType = hotel_typeRepository.findById(hotel_type.getId()).orElse(null);
                if (existingHotelType != null) {
                    String oldFileName = existingHotelType.getFile_name();

                    // Update hotel type details
                    existingHotelType.setType(hotel_type.getType());
                    existingHotelType.setDescription(hotel_type.getDescription());

                    if (!file.isEmpty()) {
                        // Delete old file
                        if (oldFileName != null && !oldFileName.isEmpty()) {
                            Path oldFilePath = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/" + oldFileName);
                            Files.deleteIfExists(oldFilePath);
                        }

                        String fileName = file.getOriginalFilename();
                        String sanitizedFileName = fileName.replaceAll("\\s+", "");

                        existingHotelType.setFile_name(sanitizedFileName);

                        Path file1 = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/");

                        if (!Files.exists(file1)) {
                            Files.createDirectory(file1);
                        }

                        byte[] bytes = file.getBytes();
                        Path path = Paths.get(file1 + "/" + sanitizedFileName);
                        Files.write(path, bytes);
                        System.out.println(path);
                    }

                    hotel_typeRepository.saveAndFlush(existingHotelType);
                    modelAndView.setViewName("redirect:/propertytype");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/delete_propertyType/{typeId}", method = RequestMethod.GET)
    public ModelAndView handleDeletePropertyType(@PathVariable int typeId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            Optional<Hotel_Type> hotel_typeOptional = hotel_typeRepository.findById(typeId);
            if(hotel_typeOptional.isPresent()){
                Hotel_Type hotel_type = hotel_typeOptional.get();

                // Delete associated photo
                if (hotel_type.getFile_name() != null && !hotel_type.getFile_name().isEmpty()) {
                    Path photoPath = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/propertytype/" + hotel_type.getFile_name());
                    try {
                        Files.deleteIfExists(photoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                hotel_typeRepository.delete(hotel_type);
                modelAndView.setViewName("redirect:/propertytype");
            } else {
                modelAndView.setViewName("redirect:/error");
            }
        }
        return modelAndView;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editPropertyAmenity")
    public ModelAndView editHotelFacilityGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            Optional<HotelFacility> hotelFacilityOptional = hotelFacilityRepository.findById(id);
            if(hotelFacilityOptional.isPresent()) {
                HotelFacility hotelFacility = hotelFacilityOptional.get();
                modelAndView.addObject("hotelFacility", hotelFacility);
                modelAndView.addObject("imagePath", "/uploads/facility/" + hotelFacility.getFile_name());
                modelAndView.setViewName("ROLE_ADMIN/hotelFacility/editHotelFacility");
            }
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editPropertyAmenity")
    public ModelAndView editHotelFacility(@RequestParam(value = "id") Long id,
                                          @RequestParam(value = "file", required = false) MultipartFile file,
                                          HttpServletRequest request,
                                          ModelAndView modelAndView,
                                          HotelFacility hotelFacility){
        if (request.isUserInRole("ROLE_ADMIN")) {
            try {
                Optional<HotelFacility> optionalHotelFacility = hotelFacilityRepository.findById(id);
                if (optionalHotelFacility.isPresent()) {
                    HotelFacility existingFacility = optionalHotelFacility.get();
                    String oldFileName = existingFacility.getFile_name();

                    //Update facility details
                    existingFacility.setName(hotelFacility.getName());
                    existingFacility.setDescription(hotelFacility.getDescription());

                    if (file != null && !file.isEmpty()) {
                        System.out.println("File is not empty");

                        //Delete old file
                        if (oldFileName != null && !oldFileName.isEmpty()) {
                            Path oldFilePath = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/facility/" + oldFileName);
                            Files.deleteIfExists(oldFilePath);
                        }

                        // Update file name only if a new file is uploaded
                        existingFacility.setFile_name(file.getOriginalFilename());

                        //Save file
                        Path file1 = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/facility/");
                        if (!Files.exists(file1)) {
                            Files.createDirectory(file1);
                        }

                        byte[] bytes = file.getBytes();
                        Path path = Paths.get(file1 + "/" + file.getOriginalFilename());
                        Files.write(path, bytes);
                        System.out.println("File saved to: " + path.toString());
                    } else {
                        System.out.println("No file uploaded or file is empty");
                    }

                    hotelFacilityRepository.saveAndFlush(existingFacility);

                    syncWithSecondProject(existingFacility);

                    modelAndView.setViewName("redirect:/propertyAmenities");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return modelAndView;
    }

    @PostMapping("/deletePropertyAmenity")
    @ResponseBody
    public Map<String, String> deleteHotelAmenity(@RequestParam("id") Long id, HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();

        if (request.isUserInRole("ROLE_ADMIN")) {
            try {
                Optional<HotelFacility> optionalHotelFacility = hotelFacilityRepository.findById(id);
                if (optionalHotelFacility.isPresent()) {
                    HotelFacility hotelFacility = optionalHotelFacility.get();

                    // Check if the facility is in use
                    if (!hotelFacility.getProperties().isEmpty()) {
                        response.put("status", "error");
                        response.put("message", "Cannot delete a facility that is in use.");
                    } else {
                        String fileName = hotelFacility.getFile_name();

                        // Delete the associated file if it exists
                        if (fileName != null && !fileName.isEmpty()) {
                            Path filePath = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/facility/", fileName);
                            Files.deleteIfExists(filePath);
                        }

                        // Delete the facility from the repository
                        hotelFacilityRepository.delete(hotelFacility);
                        hotelFacilityRepository.flush();
                        System.out.println("4444444444444444444");

                        // Sinkronizo me ndryshimet me Join-Allbookers
                        syncDeleteWithSecondProject(id);

                        response.put("status", "success");
                        response.put("message", "Hotel facility deleted successfully!");
                    }
                } else {
                    response.put("status", "error");
                    response.put("message", "Hotel facility not found.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                response.put("status", "error");
                response.put("message", "An error occurred while deleting the hotel facility.");
            }
        } else {
            response.put("status", "error");
            response.put("message", "You do not have permission to delete this hotel facility.");
        }

        return response;
    }

    private void syncDeleteWithSecondProject(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String secondProjectApiUrl = "https://join.allbookers.com/api/hotel-facility/delete";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("id", id);

        HttpEntity<Map<String, Long>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(secondProjectApiUrl, HttpMethod.POST, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Hotel facility deletion synced successfully with the second project.");
            } else {
                System.out.println("Failed to sync hotel facility deletion with the second project.");
            }
        } catch (Exception e) {
            System.out.println("Error syncing hotel facility deletion: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/addPropertyType")
    public ModelAndView addPropertyTypeGet(HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/hotelType/addHotelType");

            NewHotelType newHotelType = new NewHotelType();
            modelAndView.addObject("newHotelType", newHotelType);
            modelAndView.addObject("county", hotel_typeRepository.findAll());
        }

        return modelAndView;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/type/addPropertyType")
    public ModelAndView addPropertyType(HttpServletRequest request, NewHotelType newHotelType,@RequestParam("file") MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            Path file1 = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/propertytype/");

            if (!Files.exists(file1)){
                Files.createDirectory(file1);
            }
            try {
                // Check if a file is uploaded
                if (file != null && !file.isEmpty()) {
                    // Get the file and save it somewhere
                    byte[] bytes = file.getBytes();
                    Hotel_Type hotel_type = new Hotel_Type(11,newHotelType.getType(),newHotelType.getDescription(), file.getOriginalFilename());
                    System.out.println(file.getOriginalFilename());
                    hotel_typeRepository.saveAndFlush(hotel_type);
                    Path path = Paths.get(file1+ "/" + file.getOriginalFilename());
                    Files.write(path,bytes);
                    System.out.println(file1);

                    // Construct the URL for the saved file
                    String savedFilePath = "/uploads/propertytype/" + file.getOriginalFilename(); // Change this according to your server URL
                    // Pass the saved file path to the view
                    modelAndView.addObject("savedFilePath", savedFilePath);
                }else {
                    // Save other data
                    // For example, save the hotel type data even if no file is uploaded
                    Hotel_Type hotel_type = new Hotel_Type(11, newHotelType.getType(), newHotelType.getDescription(), null);
                    hotel_typeRepository.saveAndFlush(hotel_type);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            modelAndView.addObject("newHotelType", new NewHotelType());
            modelAndView.setViewName("redirect:/propertytype");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addPropertyAmenity")
    public ModelAndView addHotelFacilityGet(HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){

            modelAndView.setViewName("ROLE_ADMIN/hotelFacility/addHotelFacility");
            NewHotelFacility newHotelFacility = new NewHotelFacility();
            modelAndView.addObject("newHotelFacility", newHotelFacility);

        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addPropertyAmenity")
    public ModelAndView addHotelFacility(HttpServletRequest request,
                                         NewHotelFacility newHotelFacility,
                                         @RequestParam(value="file", required = false) MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView();

        if (request.isUserInRole("ROLE_ADMIN")) {
            Path file1 = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/facility/");

            if (!Files.exists(file1)) {
                Files.createDirectory(file1);
            }

            try {
                String fileName = null;
                // Kontrollo nëse një skedar është ngarkuar
                if (file != null && !file.isEmpty()) {
                    // Merr skedarin dhe ruaje diku
                    byte[] bytes = file.getBytes();
                    fileName = file.getOriginalFilename();
                    Path path = Paths.get(file1 + "/" + fileName);
                    Files.write(path, bytes);
                    System.out.println("File saved to: " + path.toString());
                }
                HotelFacility hotelFacility = new HotelFacility(0, newHotelFacility.getName(), newHotelFacility.getDescription(), fileName);
                hotelFacilityRepository.saveAndFlush(hotelFacility);

                // Bëj sinkronizimin me projektin e dytë
                syncWithSecondProject(hotelFacility);

            } catch (IOException e) {
                e.printStackTrace();
            }

            modelAndView.setViewName("redirect:/propertyAmenities");
        }
        return modelAndView;
    }

//  This method serves to transfer the new facilities to the Join-Allbookers database
    private void syncWithSecondProject(HotelFacility hotelFacility) {
        RestTemplate restTemplate = new RestTemplate();
        String secondProjectApiUrl = "https://join.allbookers.com/api/hotel-facility/sync";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HotelFacility> request = new HttpEntity<>(hotelFacility, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(secondProjectApiUrl, HttpMethod.POST, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Hotel facility synced successfully with the second project.");
            } else {
                System.out.println("Failed to sync hotel facility with the second project.");
            }
        } catch (Exception e) {
            System.out.println("Error syncing hotel facility: " + e.getMessage());
        }
    }

    @Autowired
    ReviewService reviewService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/propertyDashboard")
    public ModelAndView getPropertyDashboard(@RequestParam(value = "id") Long id, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {

            long startTime = System.currentTimeMillis();
            Property property = propertyRepository.findById(id).get();

            long endTime = System.currentTimeMillis();
            double durationInSeconds = (endTime - startTime) / 1000.0; // konvertohet në sekonda
            System.out.println("Koha e ekzekutimit: " + durationInSeconds + " sekonda");
            long startTime1 = System.currentTimeMillis();
            User user = userRepository.findByUsername(request.getUserPrincipal().getName());
            int propSize = user.getRole().size();
            modelAndView.addObject("propSize", propSize);
            modelAndView.addObject("user", user);
            long endTime1 = System.currentTimeMillis();
            double durationInSeconds1 = (endTime1 - startTime1) / 1000.0; // konvertohet në sekonda
            System.out.println("Koha e ekzekutimit1: " + durationInSeconds1 + " sekonda");
            long startTime2 = System.currentTimeMillis();
            modelAndView.addObject("mes", reviewService.getReviewSum(property));
            Map<Integer, Double> ratingPercentages = reviewService.getRatingPercentages(property);
            modelAndView.addObject("ratingPercentages", ratingPercentages);
            long endTime2 = System.currentTimeMillis();
            double durationInSeconds2 = (endTime2 - startTime2) / 1000.0; // konvertohet në sekonda
            System.out.println("Koha e ekzekutimit2: " + durationInSeconds2 + " sekonda");
            modelAndView.addObject("property", property);

        }
        modelAndView.setViewName("ROLE_ADMIN/Property/propertyDashboard");
        return modelAndView;
    }

    @PostMapping("/refused")
        public String refused(@RequestParam (value="id") Long id) throws MessagingException {
        Property property = propertyRepository.findById(id).get();
        property.setStatus("refused");
        property.setSubmissionDate(null);
        if(property.isProfessionalHost() == true){
            property.setNUIS(null);
            property.setTaxname(null);
            property.setUsername(null);
            property.setStatus(null);
            property.setProfessionalHost(false);
        } else if (property.isPrivateHost() == true){
                property.getHotelierId().setDateofbirth(null);
                property.getHotelierId().setIdCard(null);
                property.getHotelierId().setLogo(null);
                property.getHotelierId().setProperty(null);
                property.setHotelierId(null);
                property.setUsername(null);
                property.setStatus(null);
                property.setPrivateHost(false);
        }

        propertyRepository.save(property);
        javaMailService.refusedKYPform(property);

        return "redirect:/propertyDashboard?id="+property.getId();
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/propertyDetails")
    public ModelAndView getPropertyDetails(@RequestParam(value = "id") Long id, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.setViewName("ROLE_ADMIN/Property/propertyDetails");
            Property property = propertyRepository.findById(id).get();
            modelAndView.addObject("property", property);
            modelAndView.addObject("countries", countryRepository.findAllCountryNames());
            modelAndView.addObject("languagesall", languageRepository.findAllIdsAndNames());
            modelAndView.addObject("hotelTypes", hotel_typeRepository.findAllIdsAndTypesAndVersion(10));
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping (value = "/propertyDetails")
    public String editPropertyBasic(@RequestParam(value = "id") Long id, HttpServletRequest request, Property property){
        if (request.isUserInRole("ROLE_ADMIN")){

            System.out.println(property.getName());
            Property dbProperty = propertyRepository.findById(id).get();
            File file1 = new File("/home/allbookersusr/home/BookersDesk/data/klienti/"+propertyEncodeName.encodeNameForServerPath(dbProperty));

            for (int i = 0; i<property.getLanguages().size();i++){
                System.out.println(property.getLanguages().get(i).getName());
            }
            System.out.println(property.getHotel_status().getStatus());

            dbProperty.setName(property.getName());
            dbProperty.setNumber_of_rooms(property.getNumber_of_rooms());
            dbProperty.setCountry(property.getCountry());

            Hotel_Type hotel_type = hotel_typeRepository.findById(property.getRealId()).get();
            dbProperty.setHotel_type(hotel_type);
            dbProperty.setLanguage(languageRepository.findById(property.getLanguage().getId()).get());

            dbProperty.setLanguages(property.getLanguages());
            dbProperty.setHotel_status(property.getHotel_status());
            property.setStars(property.getStars() <= 0 ? 1 : property.getStars());
            property.setStars(property.getStars() >= 6 ? 5 : property.getStars());
            dbProperty.setStars(property.getStars());

            propertyRepository.save(dbProperty);


            File file2=new File("/home/allbookersusr/home/BookersDesk/data/klienti/"+propertyEncodeName.encodeNameForServerPath(dbProperty));
            file1.renameTo(file2);

        }
        return "redirect:/propertyDetails?id=" + id;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/propertyFacilities")
    public ModelAndView getFacilitiesDetails(@RequestParam(value = "id") Long id, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.setViewName("ROLE_ADMIN/Property/propertyFacilities");
            Property property = propertyRepository.findById(id).get();
            modelAndView.addObject("property", property);
            modelAndView.addObject("hotelFacilites", hotelFacilityRepository.findAll());
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/propertyFacilities")
    public String FacilitiesDetails(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, Property property){
        if (request.isUserInRole("ROLE_ADMIN")){
            Property dbProperty = propertyRepository.findById(id).get();

            List<HotelFacility> hotelFacilities = property.getHotel_facility();
            for (int i=0;i<hotelFacilities.size();i++){
                System.out.println(hotelFacilities.get(i).getName());
            }

            property.setHotel_facility(property.getHotel_facility());
            dbProperty.setHotel_facility(property.getHotel_facility());
            propertyRepository.save(dbProperty);
        }

        return "redirect:/propertyFacilities?id=" + id;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/propertyAttribute")
    public ModelAndView getAttributeDetails(@RequestParam(value = "id") Long id, HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.setViewName("ROLE_ADMIN/Property/propertyAttribute");
            Property property = propertyRepository.findById(id).get();
            modelAndView.addObject("property", propertyRepository.findById(id).get());
            modelAndView.addObject("propertyAttribute", hotelAttributeRepository.findAll());
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/propertyAttribute")
    public ModelAndView AttributeDetails(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, Property property){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/Property/propertyAttribute");
            Property dbProperty = propertyRepository.findById(id).get();


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



    //    address edit
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/editAddress")
    public ModelAndView editAddressData(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/Property/addressEdit");
            Property property = propertyRepository.findById(id).get();
            modelAndView.addObject("property", property);
            modelAndView.addObject("address", property.getAddress());
            modelAndView.addObject("cities", cityRepository.findCityIdAndName());
        }

        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/editAddress")
    public String submitEditAddress(@RequestParam(value = "id") Long id, HttpServletRequest request, @ModelAttribute Property property) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property dbProperty = propertyRepository.findById(id).orElseThrow(() -> new RuntimeException("Property not found"));
            Address objAddress = property.getAddress();
            Address address = addressRepostitory.findByProperty(dbProperty);

            address.setAddress_city(objAddress.getAddress_city());
            address.setOn_map(objAddress.getOn_map());
            address.setStreet(objAddress.getStreet());
            address.setCity(cityRepository.findById(objAddress.getCity().getId()).orElseThrow(() -> new RuntimeException("City not found")));
            address.setZip_code(objAddress.getZip_code());
            address.setWebsite(objAddress.getWebsite());

            addressRepostitory.save(address);
        }
        return "redirect:/editAddress?id=" + id;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/respondToRequestedChanges")
    @ResponseBody
    public Map<String, Object> respondToRequestedChanges(@RequestParam(name = "accepted") Boolean accepted, @RequestParam(name = "id") Long propertyId) throws MessagingException {

        Map<String, Object> response = new HashMap<>();

        Property property = propertyRepository.findById(propertyId).get();
        AddressChange addressChange = property.getAddressChange();
        Address address = property.getAddress();

        if (accepted) {
            address.setStreet(addressChange.getStreetName());
            address.setCity(cityRepository.findById(addressChange.getCityId()).get());
            address.setZip_code(addressChange.getZipCode());

            response.put("accepted", true);
            javaMailService.requestAccepted(property, addressChange);
        } else {
            response.put("accepted", false);
            javaMailService.requestRefused(property, addressChange);
        }

        property.setAddressChange(null);
        addressChange.setProperty(null);

        addressChangeRepository.deleteById(addressChange.getId());
        propertyRepository.save(property);
        addressRepostitory.save(address);

        return response;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/respondToRequestedChanges1")
    @ResponseBody
    public boolean respondToRequestedChanges1( @RequestParam(name = "id") Long propertyId,@RequestBody RequestChanges requestChanges) throws MessagingException {
        System.out.println(" po ");

        Property property = propertyRepository.findById(propertyId).get();

        // Gjej rolin special duke filtruar ID-të e padëshiruara
        Role specialRole = property.getRoles().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse roli special ka user-a dhe filtro ata që kanë `is_admin` të vërtetë
        Optional<User> adminUser = specialRole.getUsers().stream()
                .filter(User::isIs_admin)
                .findFirst();

        PropertyChange propertyChange = property.getPropertyChange();
        System.out.println(requestChanges.getPropertyName() + " property name from request ");

        if (adminUser.isPresent()) {
            javaMailService.requestPropertyNameAccepted(property, propertyChange , adminUser);
        }
        property.setName(propertyChange.getPropertyName());
        property.setPropertyChange(false);
        propertyChange.setPropertyName(null);
        propertyChangeRepository.deleteById(propertyChange.getId());
        propertyRepository.save(property);
        propertyChangeRepository.save(propertyChange);

        return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/refuseToRequestedChanges")
    @ResponseBody
    public boolean refuseToRequestedChanges( @RequestParam(name = "id") Long propertyId,@RequestBody RequestChanges requestChanges) throws MessagingException {
        Property property = propertyRepository.findById(propertyId).get();
        PropertyChange propertyChange = property.getPropertyChange();

        // Gjej rolin special duke filtruar ID-të e padëshiruara
        Role specialRole = property.getRoles().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        // Kontrollo nëse roli special ka user-a dhe filtro ata që kanë `is_admin` të vërtetë
        Optional<User> adminUser = specialRole.getUsers().stream()
                .filter(User::isIs_admin)
                .findFirst();

        System.out.println(requestChanges.getPropertyName() + " property name from request ");
        javaMailService.requestPropertyNameRefused(property, propertyChange, adminUser);
        property.setPropertyChange(null);
        property.setPropertyChange(false);
        propertyChange.setPropertyName(null);
        propertyChangeRepository.deleteById(propertyChange.getId());
        propertyRepository.save(property);
        propertyChangeRepository.save(propertyChange);
        return true;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/contactDetails")
    public ModelAndView editContactDetails(@RequestParam(value = "id") Long id, ModelAndView modelAndView, HttpServletRequest request){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/Property/contactDetails");
            long startTime1 = System.currentTimeMillis();
            Property property = propertyRepository.findById(id).get();
            long endTime1 = System.currentTimeMillis();
            double durationInSeconds1 = (endTime1 - startTime1) / 1000.0; // konvertohet në sekonda
            System.out.println("Koha e ekzekutimit1: " + durationInSeconds1 + " sekonda");
            modelAndView.addObject("property", property);
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/contactDetails")
    public String editContactDetailsPost(@RequestParam(value = "id") Long id, HttpServletRequest request, Property property){
        if (request.isUserInRole("ROLE_ADMIN")){
            Property dbProperty = propertyRepository.findById(id).get();

            Address dbAddress = dbProperty.getAddress();
            Address objAddress = property.getAddress();

            dbAddress.setEmail(objAddress.getEmail());
            dbAddress.setTelephone(objAddress.getTelephone());
            addressRepostitory.save(dbAddress);

            // Update email in user table
            // User user = dbProperty.getRole().getUsers().get(0);
            // user.setEmail(objAddress.getEmail());
            // userRepository.saveAndFlush(user);

            dbProperty.setFirstName(property.getFirstName());
            dbProperty.setLastName(property.getLastName());
            propertyRepository.saveAndFlush(dbProperty);
        }
        return "redirect:/contactDetails?id=" + id;
    }

    @Autowired
    UserRepository userRepository;
    @Autowired
    NumberService numberService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PhotoCrudRepository photoCrudRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/seasonalDeals")
    public ModelAndView addSeasonalDeals(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView) {
        if (request.isUserInRole("ROLE_ADMIN")) {

            Optional<Property> propertyOptional = propertyRepository.findById(id);
            if (propertyOptional.isPresent()) {
                Property property = propertyOptional.get();
                property.setSeasonalDeals(!property.isSeasonalDeals());
                propertyRepository.save(property);
            }

            modelAndView.setViewName("redirect:/propertylist");
        }
        return modelAndView;
    }

    @Autowired
    AuthorizationService authorizationService;

    @PostMapping(value = "/deleteProperty")
    @ResponseBody
    public String delete(HttpServletRequest request, @RequestParam(value = "id") Long id, @RequestBody UserRequest userRequest) {

        Boolean userFound = authorizationService.checkIfUserDetailsAreCorrect(userRequest.getUsername(), userRequest.getPassword());

        User user = new User();
        Role role = new Role();
        System.out.println(userFound + "dvs51vsd");
        System.out.println(userRequest.getUsername().equals("superadmin"));
        System.out.println(userRequest.getUsername());

        if (userFound && userRequest.getUsername().equals("superadmin")) {
            Property property = propertyRepository.findById(id).get();
            HotelStatus hotelStatus = hotelStatusRepository.findById(3).get();
            property.setHotel_status(hotelStatus);
            propertyRepository.save(property);

            return "true";
        }
        return "false";
    }

//    @Autowired
//    NumberService numberService;

    @GetMapping(value = "testing")
    @ResponseBody
    public void testForUsername(HttpServletRequest request,
                                @RequestParam(value = "u") String u){

        System.out.println(numberService.generateThreedigitNumber());
        System.out.println(userRepository.findAllByUsername(u).size());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/rankProperty")
    public ModelAndView rankProperty(@RequestParam(value = "id") Long id,
                                     @RequestParam(value = "unlist", required = false) String unlist,
                                     HttpServletRequest request,
                                     ModelAndView modelAndView,
                                     Property property) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property dbProperty = propertyRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Property not found"));

            if ("true".equals(unlist)) {
                dbProperty.setShowProperty(0); // unlist -> showProperty = 0
                propertyRepository.save(dbProperty);
            } else {
                dbProperty.setShowProperty(property.getShowProperty()); // vendos renditjen
                propertyRepository.save(dbProperty);
            }

            modelAndView.addObject("property", dbProperty);
            modelAndView.setViewName("redirect:/propertylist");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/rankProperty")
    public ModelAndView rankPropertyGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Property not found"));
            modelAndView.addObject("property", property);
            List<Boolean> existStatus = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                List<Property> propertiesWithGivenShowPropertyValue = propertyRepository.findByShowPropertyValue(i);
                existStatus.add(!propertiesWithGivenShowPropertyValue.isEmpty());
            }
            modelAndView.addObject("existStatus", existStatus);
            modelAndView.setViewName("ROLE_ADMIN/Property/rankproperty");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/uploadPropertyImages")
    public ModelAndView uploadPropertyImages(@RequestParam(value = "id") Long id, ModelAndView modelAndView, HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(id).get();
            int photoNR = hotelPhotoRepository.countByProperty(property);
            modelAndView.addObject("photoNR", photoNR);
            modelAndView.addObject("property", property);
            modelAndView.addObject("hophoto", new HotelPhoto());
            List<HotelPhoto> nonPrimaryPhotos = hotelPhotoRepository.findNonPrimaryPhotosByPropertyId(id);
            modelAndView.addObject("propertyImages", nonPrimaryPhotos);

            modelAndView.setViewName("ROLE_ADMIN/Property/photosProperty");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT') || hasRole('ROLE_ADMIN')")
    @PostMapping("/changestatusPrimary")
    @ResponseBody
    public void accountLocked(HttpServletRequest httpServletRequest, @RequestParam(value = "id") Long id, @RequestParam(value = "isChecked", required = false) Boolean isChecked) {


        Optional<HotelPhoto> hotelPhotoOptional = hotelPhotoRepository.findById(id);
        if (!hotelPhotoOptional.isPresent()) {
            return;
        }
        HotelPhoto hotelPhoto = hotelPhotoOptional.get();

        Property property = hotelPhoto.getProperty();

        if (isChecked) {
            List<HotelPhoto> hotelPhotos = property.getHotelPhotos().stream()
                    .filter(HotelPhoto::isSet_primary)
                    .collect(Collectors.toList());

            for (HotelPhoto photo : hotelPhotos) {
                photo.setSet_primary(false);
                hotelPhotoRepository.save(photo);
            }

            hotelPhoto.setSet_primary(true);
        } else {
            hotelPhoto.setSet_primary(false);
        }
        hotelPhotoRepository.save(hotelPhoto);
    }




    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT') or hasRole('ROLE_ADMIN')")
    @PostMapping(value = "uploadPropertyImages")
    @ResponseBody
    public void uploadImages(@RequestParam(value = "id") Long id, @RequestPart List<MultipartFile> file) throws IOException {


        Property property = propertyRepository.findById(id).get();

        // Kontrolloni numrin aktual të fotove të kësaj prone
        int currentPhotoCount = hotelPhotoRepository.countByProperty(property);

        if (currentPhotoCount + file.size() > 150) {
            throw new IllegalArgumentException("This property has reached the limit of 50 photos");
        }


        Path uploadPath = Paths.get("/home/allbookersusr/home/BookersDesk/data/klienti/"+propertyEncodeName.encodeNameForServerPath(property));

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }




        for (int i =0 ;i<file.size();i++){
            try (InputStream inputStream = file.get(i).getInputStream()) {


                HotelPhoto hotelPhoto = new HotelPhoto(propertyRepository.findById(id).get(), 0, file.get(i).getOriginalFilename());
                hotelPhotoRepository.save(hotelPhoto);
                String filename = propertyEncodeName.encodeImageName(hotelPhoto, file.get(i));

                Files.copy(file.get(i).getInputStream(), uploadPath.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);

                Path filePath = uploadPath.resolve(filename);


                hotelPhoto.setFile_name(filename);
                hotelPhotoRepository.save(hotelPhoto);


                System.out.println(file.get(i).getOriginalFilename());

            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + file.get(i).getOriginalFilename(), ioe);
            }
        }

        System.out.println("Fotoja u ngarkua me sukses");
    }

    @PostMapping(value = "moveImages")
    private ModelAndView moveImagesFromOne(@RequestParam(value = "id") Long id) throws IOException {

        Property property = propertyRepository.findById(id).get();

        List<HotelPhoto> hotelPhotos = property.getHotelPhotos();

        File destFile = new File("/home/allbookersusr/home/BookersDesk/data/klienti/"+ propertyEncodeName.encodeNameForServerPath(property)+"/");

        if (!destFile.exists()){
            Files.createDirectories(destFile.toPath());
        }

        for (int i=0;i<hotelPhotos.stream().count();i++){

            File fromFile = new File("/home/allbookersusr/home/BookersDesk/data/uploads/live/"+hotelPhotos.get(i).getFile_name());

            File newName = new File(destFile+"/"+propertyEncodeName.encodeOneImage(hotelPhotos.get(i)));
            hotelPhotos.get(i).setFile_name(propertyEncodeName.encodeOneImage(hotelPhotos.get(i)));
            hotelPhotoRepository.save(hotelPhotos.get(i));
            System.out.println("u editua fotoja me id:            "+hotelPhotos.get(i).getId());

            fromFile.renameTo(newName);

        }

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("property", property);
        modelAndView.addObject("hophoto", new HotelPhoto());
        modelAndView.addObject("propertyImages", property.getHotelPhotos().stream().filter(x-> !x.isSet_primary()).collect(Collectors.toList()));

        modelAndView.setViewName("ROLE_ADMIN/Property/photosProperty");
        return modelAndView;
    }

    @PostMapping(value = "changeFileNames")
    @ResponseBody
    public void changeFileNames(@RequestParam(value = "id") Long id){
        Property property = propertyRepository.findById(id).get();
        List<HotelPhoto> hotelPhotos = property.getHotelPhotos();
        for (int i=0;i<hotelPhotos.size();i++){
            hotelPhotos.get(i).setFile_name(propertyEncodeName.encodeOneImage(hotelPhotos.get(i)));
            hotelPhotoRepository.save(hotelPhotos.get(i));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT') || hasRole('ROLE_ADMIN')")
    @PostMapping(value = "deleteImages")
    @ResponseBody
    public void deleteImages(HttpServletRequest request,@RequestBody List<Long> photoId) throws IOException {

        if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT") || request.isUserInRole("ROLE_ADMIN")) {

            System.out.println(photoId.get(0));
            Long photoIdd = 0L;

            for (int i = 0; i < photoId.size(); i++) {
                photoIdd = photoId.get(i);
                Property property = hotelPhotoRepository.findById(photoIdd).get().getProperty();
                String hotelPhoto = hotelPhotoRepository.findById(photoIdd).get().getFile_name();
                System.out.println(photoIdd);
                photoCrudRepository.deleteById(photoIdd);
                File file = new File("/home/allbookersusr/home/BookersDesk/data/klienti/" + propertyEncodeName.encodeNameForServerPath(property) + "/" + hotelPhoto);
                if (file.exists()) {
                    Files.delete(Paths.get("/home/allbookersusr/home/BookersDesk/data/klienti/" + propertyEncodeName.encodeNameForServerPath(property) + "/" + hotelPhoto));
                }
            }

        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER', 'ROLE_GROUP_ACCOUNT')")
    @PostMapping("/deletePropertyImage")
    @ResponseBody
    public void deletePropertyImage(@RequestParam(value = "photoId") Long photoId, @RequestParam(value = "propertyId") Long propertyId ,HttpServletRequest request) throws IOException {
        if (request.isUserInRole("ROLE_ADMIN") || request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT")){
            photoCrudRepository.deleteById(photoId);
            Files.deleteIfExists(Paths.get("/home/allbookersusr/home/BookersDesk/data/klienti/"+propertyEncodeName.encodeNameForServerPath(propertyRepository.findById(propertyId).get())+"/"+hotelPhotoRepository.findById(photoId).get().getFile_name()));
        }
    }

}
