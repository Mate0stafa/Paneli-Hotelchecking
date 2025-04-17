package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.Auth.AuthKeyConfirm;
import com.example.paneli.Models.Auth.PropertyAddRequest;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.PropertyAddRequestRepository;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class GroupAccount {

    public GroupAccount(UserRepository userRepository, PropertyRepository propertyRepository,
                        PropertyAddRequestRepository propertyAddRequestRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.propertyAddRequestRepository = propertyAddRequestRepository;
    }

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyAddRequestRepository propertyAddRequestRepository;


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "addExistingProperty")
    public ModelAndView addExistingProperty(HttpServletRequest request, ModelAndView modelAndView, @RequestParam(value = "id") Long propertyId){
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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(authenticatedUser.getUsername());
            modelAndView.addObject("property", property);
            modelAndView.addObject("user", user);
            modelAndView.setViewName("ROLE_USER/Property/AddExistingProperty/ValidateUserRequest");
        }
        return modelAndView;

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "confirmEmailAccount")
    public ModelAndView confirmEmailAccount(HttpServletRequest request,
                                            ModelAndView modelAndView,
                                            @RequestParam(value = "requestId") Long requestId,
                                            @RequestParam(value = "propertyId") Long propertyId){
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
            PropertyAddRequest propertyAddRequest = propertyAddRequestRepository.findById(requestId).get();
            User user = userRepository.findByUsername(request.getUserPrincipal().getName());

            modelAndView.addObject("propertyAddRequest", propertyAddRequest);
            modelAndView.addObject("property", property);
            modelAndView.addObject("authKeyConfirm", new AuthKeyConfirm());

            modelAndView.setViewName("ROLE_USER/Property/AddExistingProperty/ConfirmEmail");
        }
        return modelAndView;
    }
    @PostMapping(value = "authConfirm")
    public ModelAndView authConfirm(HttpServletRequest request,
                                    ModelAndView modelAndView,
                                    AuthKeyConfirm authKeyConfirm){

        PropertyAddRequest propertyAddRequest = propertyAddRequestRepository.findById(authKeyConfirm.getAuthId()).get();
        System.out.println("Prona 1 : " + propertyAddRequest.getProperty_id());
        Property property = propertyRepository.findById(authKeyConfirm.getPropertyId()).get();
        System.out.println("Prona 2 : " + property.getId());

        User userRequest = userRepository.findByUsername(propertyAddRequest.getUser().getUsername());
        System.out.println("Username : " + userRequest.getUsername());

        // Gjej rolin special të përdoruesit
        Role specialRoleRequest = userRequest.getRole().stream()
                .filter(role1 -> role1.getId() != 1L && role1.getId() != 2L && role1.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));
        // Gjej pronat e lidhura me rolin special
        List<Property> linkedProperties = propertyRepository.findPropertiesByRole(specialRoleRequest);


        // Merr përdoruesin aktual
        User currentLoggedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());

        // Gjej rolin special të përdoruesit
        Role specialRole = currentLoggedInUser.getRole().stream()
                .filter(role -> role.getId() != 1L && role.getId() != 2L && role.getId() != 3L)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No special role found for the user."));

        if (request.isUserInRole(specialRole.getAuthority())) {
            if (authKeyConfirm.getAuthPin().equals(propertyAddRequest.getUniqueKey())) {

                boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                        .anyMatch(role -> role.getId() == 3L);
                modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
                modelAndView.addObject("specialRole", specialRole);
                modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);

                modelAndView.addObject("properties", linkedProperties);
                modelAndView.addObject("property", property);
                modelAndView.addObject("keyId", propertyAddRequest.getId());
                modelAndView.addObject("specialRole", specialRole);
                modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);

                modelAndView.setViewName("ROLE_USER/Property/AddExistingProperty/PropertyListToAdd");
            } else {

                boolean hasGroupAccountUser = currentLoggedInUser.getRole().stream()
                        .anyMatch(role -> role.getId() == 3L);
                modelAndView.addObject("hasGroupAccountUser", hasGroupAccountUser);
                modelAndView.addObject("specialRole", specialRole);
                modelAndView.addObject("currentLoggedInUser", currentLoggedInUser);

                modelAndView.addObject("authKeyConfirm", new AuthKeyConfirm());
                modelAndView.addObject("propertyAddRequest", propertyAddRequest);
                modelAndView.addObject("property", property);
                modelAndView.addObject("error", "Pin code incorrect!");
                modelAndView.setViewName("ROLE_USER/Property/AddExistingProperty/ConfirmEmail");
            }
        }
        return modelAndView;
    }

}
