package com.example.paneli.Controllers;

import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.Property;
import com.example.paneli.Models.RoomAmenities.Amenity;
import com.example.paneli.Repositories.AmenityRepository;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AmenityController {

    @Autowired
    private AmenityRepository amenityRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "roomAmenities")
    public ModelAndView createNewAmenity(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            ModelAndView modelAndView) {

        if (request.isUserInRole("ROLE_ADMIN")) {

            List<Amenity> amenityPage;

            if (StringUtils.hasText(search)) {
                amenityPage = amenityRepository.findByNameContainingIgnoreCase(search);
            } else {
                amenityPage = amenityRepository.findAll();
            }

//            int totalPages = amenityPage.getTotalPages();
//            int currentPage = amenityPage.getNumber() + 1;
//            int startPage = Math.max(1, currentPage - 2);
//            int endPage = Math.min(startPage + 4, totalPages);

            modelAndView.addObject("page", amenityPage);
            modelAndView.addObject("amenityList", amenityPage );
            modelAndView.addObject("search", search);
//            modelAndView.addObject("startPage", startPage);
//            modelAndView.addObject("endPage", endPage);
            modelAndView.setViewName("ROLE_ADMIN/Property/Amenity/amenityList");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "newRoomAmenity")
    public ModelAndView newAmenity(HttpServletRequest request, ModelAndView modelAndView){
        if(request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.setViewName("ROLE_ADMIN/Property/Amenity/createNewAmenity");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "amenityList")
    public ModelAndView amenityList(HttpServletRequest request, ModelAndView modelAndView, @RequestParam(value = "propertyId") Long propertyId) {
        Property property = propertyRepository.findById(propertyId).get();
        User user = userRepository.findByUsername(request.getUserPrincipal().getName());
        System.out.println(user.getRole().size() + "tetstststs");
        Role role = user.getRole().stream().filter(x -> x.getAuthority() != "ROLE_USER").collect(Collectors.toList()).get(0);

        if (request.isUserInRole("ROLE_ADMIN")) {
            List<Amenity> amenityList = amenityRepository.findAllByAmenityType("Top Amenities");
            List<Amenity> amenityLists = amenityRepository.findAllByAmenityType("Bathroom");
            List<Amenity> amenityListss = amenityRepository.findAllByAmenityType("Room amenities");
            List<Amenity> amenityListsss = amenityRepository.findAllByAmenityType("Media & Technology");
            List<Amenity> amenityListssss = amenityRepository.findAllByAmenityType("Food & Drink");
            List<Amenity> amenityLista = amenityRepository.findAllByAmenityType("Services & Extras");
            List<Amenity> amenityListaa = amenityRepository.findAllByAmenityType("Outdoor & View");
            List<Amenity> amenityListaaa = amenityRepository.findAllByAmenityType("Accessibility");
            List<Amenity> amenityListu = amenityRepository.findAllByAmenityType("Entertainment & Family Services");
            List<Amenity> amenityListuu = amenityRepository.findAllByAmenityType("Safety & security");
            List<Amenity> amenityListuuu = amenityRepository.findAllByAmenityType("Cleanliness & disinfection");

            modelAndView.addObject("amenityList", amenityList);
            modelAndView.addObject("amenityLists", amenityLists);

            modelAndView.addObject("amenityListss", amenityListss);
            modelAndView.addObject("amenityListsss", amenityListsss);
            modelAndView.addObject("amenityListssss", amenityListssss);
            modelAndView.addObject("amenityLista", amenityLista);
            modelAndView.addObject("amenityListaa", amenityListaa);
            modelAndView.addObject("amenityListaaa", amenityListaaa);
            modelAndView.addObject("amenityListu", amenityListu);
            modelAndView.addObject("amenityListuu", amenityListuu);
            modelAndView.addObject("amenityListuuu", amenityListuuu);


            modelAndView.addObject("property", property);
            modelAndView.setViewName("ROLE_ADMIN/Property/Amenity/selectAmenities");
        }
            return modelAndView;
    }

    @GetMapping("/editRoomAmenity")
    public ModelAndView editAmenityGet(@RequestParam("id") Long id, HttpServletRequest request, ModelAndView modelAndView) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Optional<Amenity> amenityOptional = amenityRepository.findById(id);
            if (amenityOptional.isPresent()) {
                Amenity amenity = amenityOptional.get();
                System.out.println("File name from DB: " + amenity.getFileName());
                System.out.println("Setting imagePath to: /uploads/amenity/" + amenity.getFileName());
                modelAndView.addObject("amenityList", amenity);
                modelAndView.addObject("imagePath", "/uploads/amenity/" + amenity.getFileName());
                modelAndView.setViewName("ROLE_ADMIN/Property/Amenity/editAmenity");
            }
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editRoomAmenity")
    public ModelAndView editAmenity(@RequestParam(value = "id") Long id,
                                    @RequestParam(value = "file", required = false) MultipartFile file,
                                    HttpServletRequest request,
                                    Amenity amenity) {
        ModelAndView modelAndView = new ModelAndView();

        if (request.isUserInRole("ROLE_ADMIN")) {
            try {
                Optional<Amenity> optionalAmenity = amenityRepository.findById(id);
                if (optionalAmenity.isPresent()) {
                    Amenity existingAmenity = optionalAmenity.get();
                    String oldFileName = existingAmenity.getFileName();

                    // Update amenity details
                    existingAmenity.setAmenityName(amenity.getAmenityName());
                    existingAmenity.setAmenityType(amenity.getAmenityType());
                    existingAmenity.setAmenityDescription(amenity.getAmenityDescription());

                    if (file != null && !file.isEmpty()) {
                        System.out.println("File is not empty");

                        // Delete old file
                        if (oldFileName != null && !oldFileName.isEmpty()) {
                            Path oldFilePath = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/amenity/" + oldFileName);
                            Files.deleteIfExists(oldFilePath);
                        }

                        // Update file name only if a new file is uploaded
                        existingAmenity.setFileName(file.getOriginalFilename());

                        // Save file
                        Path file1 = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/amenity/");
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

                    amenityRepository.saveAndFlush(existingAmenity);
                    syncWithSecondProject(existingAmenity);

                    modelAndView.setViewName("redirect:/roomAmenities");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return modelAndView;
    }

    private void syncWithSecondProject(Amenity amenity) {
        RestTemplate restTemplate = new RestTemplate();
        String secondProjectApiUrl = "https://join.allbookers.com/api/amenity/sync";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Amenity> request = new HttpEntity<>(amenity, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(secondProjectApiUrl, HttpMethod.POST, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Amenity synced successfully with the second project.");
            } else {
                System.out.println("Failed to sync amenity with the second project.");
            }
        } catch (Exception e) {
            System.out.println("Error syncing amenity: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deleteAmenity")
    @ResponseBody
    public ResponseEntity<?> deleteAmenity(@RequestParam Long id, HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Optional<Amenity> amenityOptional = amenityRepository.findById(id);
            if (amenityOptional.isPresent()) {
                Amenity amenity = amenityOptional.get();

                String fileName = amenity.getFileName();

                // Delete the file if it exists
                if (fileName != null && !fileName.isEmpty()) {
                    try {
                        // Path to the file
                        Path filePath = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/amenity/" + fileName);
                        Files.deleteIfExists(filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file.");
                    }
                }

                // Delete the amenity from the repository
                amenityRepository.deleteById(id);
                syncDeleteWithSecondProject(id);

                return ResponseEntity.ok("Amenity deleted successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }
    }

    private void syncDeleteWithSecondProject(Long id) {
        RestTemplate restTemplate = new RestTemplate();
        String secondProjectApiUrl = "https://join.allbookers.com/api/amenity/delete";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Long> requestBody = new HashMap<>();
        requestBody.put("id", id);

        HttpEntity<Map<String, Long>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(secondProjectApiUrl, HttpMethod.POST, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Amenity deletion synced successfully with the second project.");
            } else {
                System.out.println("Failed to sync amenity deletion with the second project.");
            }
        } catch (Exception e) {
            System.out.println("Error syncing amenity deletion: " + e.getMessage());
        }
    }


}















