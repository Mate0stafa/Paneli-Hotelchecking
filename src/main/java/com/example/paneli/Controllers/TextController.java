package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.DescriptionDto;
import com.example.paneli.Models.Description;
import com.example.paneli.Models.DescriptionChangeRequest;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.DescriptionChangeRequestRepository;
import com.example.paneli.Repositories.DescriptionRepository;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.Mail.JavaMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class TextController {
    @Autowired
    JavaMailService javaMailService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    DescriptionRepository descriptionRepository;

    @Autowired
    private DescriptionChangeRequestRepository descriptionChangeRequestRepository;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/textOfProperty")
    public ModelAndView changeText(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView) {
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
        if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
            if (request.isUserInRole(specialRole.getAuthority()) || hasAccess) {
                modelAndView.addObject("property", property);
                modelAndView.setViewName("ROLE_USER/Property/propertyDescriptionUser");
            }
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/requestDescriptionChange")
    public ResponseEntity<?> requestDescriptionChange(@RequestBody DescriptionDto descriptionDto) {
        Property property = propertyRepository.findById(descriptionDto.getPropertyIdDto()).orElse(null);
        if (property == null) {
            return ResponseEntity.badRequest().body("Property not found");
        }

        DescriptionChangeRequest changeRequest = new DescriptionChangeRequest();
        changeRequest.setProperty(property);
        changeRequest.setRequestedDescription(descriptionDto.getDescriptionDto());
        changeRequest.setStatus(DescriptionChangeRequest.RequestStatus.PENDING);
        descriptionChangeRequestRepository.save(changeRequest);

        // Get the admin email
        String adminEmail = userRepository.findAdminEmail();

        // Get the current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(authenticatedUser.getUsername());

        // Send email to admin
        try {
            javaMailService.notifyDescriptionChangeRequest(user, property, changeRequest, adminEmail);
        } catch (MessagingException e) {
            System.err.println("Failed to send email notification: " + e.getMessage());
        }

        return ResponseEntity.ok("Description change request submitted successfully");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "propertyDescription")
    public ModelAndView propertyDescription(HttpServletRequest request, ModelAndView modelAndView, @RequestParam(value = "id") Long id) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(id).orElse(null);
            List<DescriptionChangeRequest> pendingRequests = descriptionChangeRequestRepository.findByPropertyAndStatus(property, DescriptionChangeRequest.RequestStatus.PENDING);
            modelAndView.addObject("property", property);
            modelAndView.addObject("pendingRequests", pendingRequests);
            modelAndView.setViewName("ROLE_ADMIN/Property/propertyDescription");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "approveDescriptionChange")
    public ResponseEntity<?> approveDescriptionChange(@RequestParam Long requestId) {
        DescriptionChangeRequest changeRequest = descriptionChangeRequestRepository.findById(requestId).orElse(null);
        if (changeRequest == null) {
            return ResponseEntity.badRequest().body("Change request not found");
        }

        Property property = changeRequest.getProperty();
        Description description = property.getDescription();
        if (description == null) {
            description = new Description();
            description.setProperty(property);
        }
        String newDescription = changeRequest.getRequestedDescription();
        description.setDescription(newDescription);
        descriptionRepository.save(description);

        changeRequest.setStatus(DescriptionChangeRequest.RequestStatus.APPROVED);
        descriptionChangeRequestRepository.save(changeRequest);

        try {
            javaMailService.notifyDescriptionChangeApproved(property, newDescription);
        } catch (MessagingException e) {
            System.err.println("Failed to send approval notification email: " + e.getMessage());
        }

        return ResponseEntity.ok("Description change approved and applied");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "propertyDescriptionPost")
    public ResponseEntity<?> newDescriptionRequestPost(HttpServletRequest request, @RequestBody DescriptionDto descriptionDto) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            Property property = propertyRepository.findById(descriptionDto.getPropertyIdDto()).orElse(null);
            if (property != null) {
                if (property.getDescription() != null) {
                    Description description = property.getDescription();
                    description.setDescription(descriptionDto.getDescriptionDto());
                    description.setProperty(property);
                    descriptionRepository.save(description);
                } else {
                    Description description = new Description();
                    description.setDescription(descriptionDto.getDescriptionDto());
                    description.setProperty(property);
                    descriptionRepository.save(description);
                }
            }
        }
        return ResponseEntity.ok("success");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "rejectDescriptionChange")
    public ResponseEntity<?> rejectDescriptionChange(@RequestParam Long requestId) {
        DescriptionChangeRequest changeRequest = descriptionChangeRequestRepository.findById(requestId).orElse(null);
        if (changeRequest == null) {
            return ResponseEntity.badRequest().body("Change request not found");
        }

        changeRequest.setStatus(DescriptionChangeRequest.RequestStatus.REJECTED);
        descriptionChangeRequestRepository.save(changeRequest);

        try {
            javaMailService.notifyDescriptionChangeRejected(changeRequest.getProperty(), changeRequest.getRequestedDescription());
        } catch (MessagingException e) {
            System.err.println("Failed to send rejection notification email: " + e.getMessage());
        }

        return ResponseEntity.ok("Description change rejected successfully");
    }


}