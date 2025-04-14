package com.example.paneli.Controllers;


import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.ReviewsTab;
import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.ReviewsTabCrudRepository;
import com.example.paneli.Repositories.ReviewsTabRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ReviewsController {

    @Autowired
    ReviewsTabRepository reviewsTabRepository;
    @Autowired
    PropertyRepository propertyRepository;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/showReviews")
    public ModelAndView ratePlane(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            Property property = propertyRepository.findById(id).get();
            List<ReviewsTab> reviewsTabs = reviewsTabRepository.findAllReviewsByProperty(property);
            modelAndView.addObject("revju",property.getReviewsTabList());
            modelAndView.addObject("property", property);
            modelAndView.addObject("revjusa", reviewsTabs);
            modelAndView.setViewName("ROLE_ADMIN/Property/reviewsList");
        }
        return modelAndView;
    }





    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/showReviews")
    @ResponseBody
    public ResponseEntity<String> toggleReviewStatus(@RequestParam(value = "id") Long id) {
        Optional<ReviewsTab> optionalReview = reviewsTabRepository.findById(id);
        if (!optionalReview.isPresent()) {
            return ResponseEntity.badRequest().body("Review not found");
        }
        ReviewsTab reviewsTab = optionalReview.get();
        Integer currentStatus = reviewsTab.getStatus();

        if (currentStatus == null || currentStatus == 0) {
            reviewsTab.setStatus(1);
            reviewsTabRepository.saveAndFlush(reviewsTab);
            return ResponseEntity.ok("Review hidden successfully");
        } else {
            reviewsTab.setStatus(0);
            reviewsTabRepository.saveAndFlush(reviewsTab);
            return ResponseEntity.ok("Review shown publicly");
        }
    }


    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping("/showReviewsUser")
    @ResponseBody
    public ResponseEntity<String> statusExpiredUser(@RequestParam(value = "id") Long id) {
        Optional<ReviewsTab> optionalReview = reviewsTabRepository.findById(id);
        if (!optionalReview.isPresent()) {
            return ResponseEntity.badRequest().body("Review not found");
        }
        ReviewsTab reviewsTab = optionalReview.get();
        Integer currentStatus = reviewsTab.getStatus();

        if (currentStatus == null || currentStatus == 0) {
            reviewsTab.setStatus(1);
            reviewsTabRepository.saveAndFlush(reviewsTab);
            return ResponseEntity.ok("Review hidden successfully");
        } else {
            reviewsTab.setStatus(0);
            reviewsTabRepository.saveAndFlush(reviewsTab);
            return ResponseEntity.ok("Review shown publicly");
        }
    }

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @GetMapping(value = "/showListReviews")
    public ModelAndView reviewList(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_USER") || request.isUserInRole("ROLE_GROUP_ACCOUNT")) {
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

                modelAndView.setViewName("ROLE_USER/Property/reviewsList");
                List<ReviewsTab> reviewsTabs = reviewsTabRepository.findAllReviewsByProperty(property);
                modelAndView.addObject("revjusat", property.getReviewsTabList());
                modelAndView.addObject("property", property);
                modelAndView.addObject("revjusa", reviewsTabs);
            }
        }
        return modelAndView;
    }



    @Autowired
    ReviewsTabCrudRepository reviewsTabCrudRepository;
    @Autowired
    ReviewService reviewService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/deleteReview")
    @ResponseBody
    public String deleteReview(HttpServletRequest request,@RequestParam(value = "id") Long id ){

        reviewService.deleteReviewsTab(id);
        return "true";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_GROUP_ACCOUNT')")
    @PostMapping(value = "/deleteReviewUser")
    @ResponseBody
    public String deleteReviewUser(HttpServletRequest request,@RequestParam(value = "id") Long id ){

        reviewService.deleteReviewsTab(id);
        return "true";
    }

}
