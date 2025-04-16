package com.example.paneli.Controllers;


import com.example.paneli.Models.*;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.*;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.RoleUniqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class PromotionController {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private PropertyRepository propertyRepository;
    @Autowired
    private PromotionTypeRepository promotionTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleUniqueService roleUniqueService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "promotions")
    public ModelAndView promotions(HttpServletRequest request, ModelAndView modelAndView, @RequestParam(value = "id") Long id) {

        // Kontrollo nëse prona ka rolin special të përdoruesit
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + id));

        if (request.isUserInRole("ROLE_ADMIN")) {
            List<PromotionType> promotionTypes = promotionTypeRepository.findAll();
            modelAndView.addObject("promotionTypes", promotionTypes);
            modelAndView.addObject("property", property);

            LocalDate todayDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String  today = todayDate.format(formatter);

            LocalDate oneMonthLater = todayDate.plusMonths(1);
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String  month = oneMonthLater.format(formatter1);

            modelAndView.addObject("todayDate",today);
            modelAndView.addObject("month",month);

            modelAndView.setViewName("ROLE_ADMIN/Property/Promotions/promotions");
        } else  if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT")){
            // Merr përdoruesin aktual
            User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

            // Gjej rolin special të përdoruesit
            Role specialRole = currentLoggedInUser.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));
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
                List<PromotionType> promotionTypes = promotionTypeRepository.findAll();
                modelAndView.addObject("promotionTypes", promotionTypes);
                modelAndView.addObject("property", property);
                LocalDate todayDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String today = todayDate.format(formatter);

                LocalDate oneMonthLater = todayDate.plusMonths(1);
                DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String month = oneMonthLater.format(formatter1);

                modelAndView.addObject("todayDate", today);
                modelAndView.addObject("month", month);

                modelAndView.setViewName("ROLE_USER/Property/Promotions/promotions");
            }
        }

        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "createNewPromotion")
    public ModelAndView createNewPromotion(HttpServletRequest request,
                                           ModelAndView modelAndView,
                                           @RequestParam(value = "propertyId") Long propertyId,
                                           @RequestParam(value = "promotionTypeId") Long promotionTypeId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));

        if (request.isUserInRole("ROLE_ADMIN")) {
            PromotionType promotionType = promotionTypeRepository.findById(promotionTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("PromotionType not found with ID: " + promotionTypeId));
            Promotion promotion = new Promotion();
            promotion.setPromotionType(promotionType);
            promotion.setStartDate(promotionType.getStartDate());
            promotion.setEndDate(promotionType.getEndDate());
            promotion.setPromotionName(promotionType.getPromotionName());
            promotion.setRecommendedPercentage(promotionType.getRecommendedPercentage());

            modelAndView.addObject("property", property);
            modelAndView.addObject("promotionType", promotionType);
            modelAndView.addObject("promotion", promotion);
            LocalDate todayDate = LocalDate.now();
            LocalDate oneMonthLater = todayDate.plusMonths(1);

            modelAndView.addObject("todayDate", todayDate.toString());
            modelAndView.addObject("month", oneMonthLater.toString());

            modelAndView.setViewName("ROLE_ADMIN/Property/Promotions/createNewPromotion");

        } else if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            // Merr përdoruesin aktual
            User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

            // Gjej rolin special të përdoruesit
            Role specialRole = currentLoggedInUser.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

            boolean hasAccess = property.getRoles().contains(specialRole);
            boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                    .anyMatch(role -> role.getId() == 3L);

            if (!hasAccess) {
                modelAndView.setViewName("/error");
                return modelAndView;
            }

            PromotionType promotionType = promotionTypeRepository.findById(promotionTypeId)
                    .orElseThrow(() -> new IllegalArgumentException("PromotionType not found with ID: " + promotionTypeId));

            Promotion promotion = new Promotion();
            promotion.setPromotionType(promotionType);
            promotion.setStartDate(promotionType.getStartDate());
            promotion.setEndDate(promotionType.getEndDate());
            promotion.setPromotionName(promotionType.getPromotionName());
            promotion.setRecommendedPercentage(promotionType.getRecommendedPercentage());

            modelAndView.addObject("property", property);
            modelAndView.addObject("promotionType", promotionType);
            modelAndView.addObject("promotion", promotion);
            LocalDate todayDate = LocalDate.now();
            LocalDate oneMonthLater = todayDate.plusMonths(1);

            modelAndView.addObject("todayDate", todayDate.toString());
            modelAndView.addObject("month", oneMonthLater.toString());
            modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
            modelAndView.addObject("specialRole", specialRole);
            modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);

            modelAndView.setViewName("ROLE_USER/Property/Promotions/createNewPromotion");
        } else {
            modelAndView.setViewName("/error");
        }

        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/createNewPromotion")
    public String createNewPromotionPost(HttpServletRequest request,
                                         @ModelAttribute Promotion promotion,
                                         @RequestParam(value = "propertyId") Long propertyId,
                                         @RequestParam(value = "promotionTypeId") Long promotionTypeId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));
        PromotionType promotionType = promotionTypeRepository.findById(promotionTypeId)
                .orElseThrow(() -> new IllegalArgumentException("PromotionType not found with ID: " + promotionTypeId));

        promotion.setPromotionType(promotionType);
        promotion.setCreatedDate(new Date());
        promotion.setProperty(property);
        promotion.setActive(true);

        if (request.isUserInRole("ROLE_ADMIN")) {
            promotionRepository.save(promotion);
            return "redirect:/yourpromotion?propertyId=" + propertyId;

        } else if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());
            Role specialRole = currentLoggedInUser.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

            boolean hasAccess = property.getRoles().contains(specialRole);

            if (!hasAccess) {
                return "redirect:/error";
            }

            promotionRepository.save(promotion);
            return "redirect:/yourPromotion?propertyId=" + propertyId;

        }

        return "redirect:/error";
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT') or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/editPromotion")
    public ModelAndView editPromotion(@RequestParam(value = "propertyId") Long propertyId,
                                      @RequestParam(value = "promotionId") Long promotionId,
                                      HttpServletRequest request,
                                      ModelAndView modelAndView) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found with ID: " + promotionId));

        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.addObject("promotion", promotion);
            modelAndView.addObject("property", property);
            modelAndView.addObject("todayDate", LocalDate.now().toString());
            modelAndView.setViewName("ROLE_ADMIN/Property/Promotions/editPromotion");

        } else if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            // Merr përdoruesin aktual
            User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

            // Gjej rolin special të përdoruesit
            Role specialRole = currentLoggedInUser.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

            boolean hasAccess = property.getRoles().contains(specialRole);

            if (!hasAccess) {
                modelAndView.setViewName("/error");
                return modelAndView;
            }

            boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                    .anyMatch(role -> role.getId() == 3L);

            modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
            modelAndView.addObject("specialRole", specialRole);
            modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);
            modelAndView.addObject("promotion", promotion);
            modelAndView.addObject("property", property);
            modelAndView.addObject("todayDate", LocalDate.now().toString());
            modelAndView.setViewName("ROLE_USER/Property/Promotions/editPromotion");

        } else {
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT') or hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/editPromotion")
    public String editPromotionpost(@RequestParam(value = "propertyId") Long propertyId,
                                    @RequestParam(value = "promotionId") Long promotionId,
                                    HttpServletRequest request,
                                    @ModelAttribute Promotion promotionFromForm) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found with ID: " + propertyId));
        Promotion promotionToUpdate = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found with ID: " + promotionId));

        promotionToUpdate.setRecommendedPercentage(promotionFromForm.getRecommendedPercentage());
        promotionToUpdate.setStartDate(promotionFromForm.getStartDate());
        promotionToUpdate.setEndDate(promotionFromForm.getEndDate());
        promotionToUpdate.setPromotionName(promotionFromForm.getPromotionName());
        promotionToUpdate.setCreatedDate(new Date());

        if (request.isUserInRole("ROLE_ADMIN")) {
            promotionRepository.save(promotionToUpdate);
            return "redirect:/yourpromotion?propertyId=" + propertyId;

        } else if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());
            Role specialRole = currentLoggedInUser.getRole().stream()
                    .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

            boolean hasAccess = property.getRoles().contains(specialRole);

            if (!hasAccess) {
                return "redirect:/error";
            }

            promotionRepository.save(promotionToUpdate);
            return "redirect:/yourPromotion?propertyId=" + propertyId;

        }

        return "redirect:/error";
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "promotionsAdmin")
    public ModelAndView promotionTypesGet(HttpServletRequest request, ModelAndView modelAndView) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            List<PromotionType> promotionTypeList = promotionTypeRepository.findAll();
            modelAndView.addObject("promotionList", promotionTypeList);
            modelAndView.setViewName("ROLE_ADMIN/Property/Promotions/promotionTypes");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "yourPromotion")
    public ModelAndView yourPromotion(HttpServletRequest request, @RequestParam(value = "propertyId") Long propertyId, ModelAndView modelAndView) {
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
        if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
            LocalDate  todayDate = LocalDate.now() ;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String  today = todayDate.format(formatter);
            modelAndView.addObject("todayDate", today);

            if (property != null) {
                LocalDate todayDatee = LocalDate.now();
                List<Promotion> promotions = property.getPromotions();
                System.out.println(promotions.size() + "PP");
                for (int i = 0 ; i < promotions.size() ; i ++ ) {
                    LocalDate endDate = LocalDate.parse(promotions.get(i).getEndDate().toString().substring(0, 10));
                    System.out.println(endDate + "enddate");
                    System.out.println(endDate.plusDays(1 ) + "endDate.plusDays1" ) ;

                    String  status;
                    if (todayDatee.isAfter(endDate)) {
                        status = "No";
                    } else
                        status = "Yes";
                    promotions.get(i).setStatus(status);
                    promotionRepository.save(promotions.get(i));
                }
            }
            modelAndView.addObject("property", property);
            modelAndView.setViewName("ROLE_USER/Property/Promotions/yourpromotions");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "yourpromotion")
    public ModelAndView yourpromotion(HttpServletRequest request, @RequestParam(value = "propertyId") Long propertyId, ModelAndView modelAndView) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(propertyId).orElse(null);
            modelAndView.addObject("property", property);
            if (property != null) {
                LocalDate todayDatee = LocalDate.now();
                List<Promotion> promotions = property.getPromotions();
                System.out.println(promotions.size() + "PP");
                for (int i = 0 ; i < promotions.size() ; i ++ ) {
                    LocalDate endDate = LocalDate.parse(promotions.get(i).getEndDate().toString().substring(0, 10));
                    System.out.println(endDate + "enddate");
                    System.out.println(endDate.plusDays(1 ) + "endDate.plusDays1" ) ;

                    String  status;
                    if (todayDatee.isAfter(endDate)) {
                         status = "No";
                    } else
                         status = "Yes";
                    promotions.get(i).setStatus(status);
                    promotionRepository.save(promotions.get(i));
                }
            }

            LocalDate  todayDate = LocalDate.now() ;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String  today = todayDate.format(formatter);
            modelAndView.addObject("todayDate", today);
            modelAndView.setViewName("ROLE_ADMIN/Property/Promotions/yourpromotions");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "addPromotionsAdmin")
    public ModelAndView addPromotionsTypeGet(HttpServletRequest request, ModelAndView modelAndView) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.setViewName("ROLE_ADMIN/Property/Promotions/addPromotionsTypes");
            PromotionType promotionType = new PromotionType();
            modelAndView.addObject("promotionsType", promotionType);
        }
        return modelAndView;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/savePromotionAdmin")
    public ModelAndView addPromotionsType(HttpServletRequest request,
                                          ModelAndView modelAndView,
                                          PromotionType promotionType,
                                          @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Path file1 = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/promotiontype");

            if (!Files.exists(file1)) {
                Files.createDirectory(file1);
            }

            try {
                String fileName = null;
                //Check if a file is uploaded
                if (file != null && !file.isEmpty()) {
                    //Get the file and save it somewhere
                    byte[] bytes = file.getBytes();
                    fileName = file.getOriginalFilename();
                    Path path = Paths.get(file1 + "/" + fileName);
                    Files.write(path, bytes);
                    System.out.println("File saved to: " + path.toString());
                }
                PromotionType newPromotionType = new PromotionType(
                        promotionType.getPromotionName(),
                        promotionType.getRecommendedPercentage(),
                        promotionType.getCategory(),
                        promotionType.getDescription(),
                        new Date(),
                        promotionType.getStartDate(),
                        promotionType.getEndDate(),
                        fileName,
                        true
                );
                promotionTypeRepository.saveAndFlush(newPromotionType);
            } catch (IOException e) {
                e.printStackTrace();
            }

            modelAndView.setViewName("redirect:/promotionsAdmin");

        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/editPromotionAdmin")
    public ModelAndView editPromotionTypeGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            PromotionType promotionType = promotionTypeRepository.findById(id).get();
            modelAndView.addObject("promotionsType", promotionType);
            modelAndView.addObject("imagePath","/uploads/promotiontype/" + promotionType.getFile_name());
            modelAndView.setViewName("ROLE_ADMIN/Property/Promotions/editPromotionType");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/changePromotionAdmin")
    public ModelAndView editPromotionType(@RequestParam(value = "id") Long id,
                                          @RequestParam(value = "file", required = false) MultipartFile file,
                                          HttpServletRequest request,
                                          ModelAndView modelAndView,
                                          PromotionType promotionType) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            try {
                PromotionType promotionTypeDTO = promotionTypeRepository.findById(id).get();
                String oldFileName = promotionTypeDTO.getFile_name();
                List<PromotionType> promotionTypeList = promotionTypeRepository.findAll();

                promotionTypeDTO.setPromotionName(promotionType.getPromotionName());
                promotionTypeDTO.setCategory(promotionType.getCategory());
                promotionTypeDTO.setDescription(promotionType.getDescription());
                promotionTypeDTO.setRecommendedPercentage(promotionType.getRecommendedPercentage());
                promotionTypeDTO.setStartDate(promotionType.getStartDate());
                promotionTypeDTO.setEndDate(promotionType.getEndDate());
                promotionTypeDTO.setActive(promotionType.getActive());

                if (file != null && !file.isEmpty()) {
                    System.out.println("File is not empty");

                    //Delete old file
                    if (oldFileName != null && !oldFileName.isEmpty()) {
                        Path oldFilePath = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/promotiontype/" + oldFileName);
                        Files.deleteIfExists(oldFilePath);
                    }

                    //Update file name only if a new file is uploaded
                    promotionTypeDTO.setFile_name(file.getOriginalFilename());

                    //Save file
                    Path file1 = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/promotiontype");
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

                promotionTypeRepository.saveAndFlush(promotionTypeDTO);
                modelAndView.setViewName("redirect:/promotionsAdmin");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return modelAndView;
    }

}


