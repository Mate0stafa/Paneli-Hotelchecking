package com.example.paneli.Controllers;

import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
public class PropertyFinanceController {


    @Autowired
    PropertyRepository propertyRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/financeProperty")
    public ModelAndView financeProperty(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, HttpSession session){
        if (request.isUserInRole("ROLE_ADMIN")){
            Property property = propertyRepository.findById(id).get();
            modelAndView.addObject("property", property);
            modelAndView.setViewName("ROLE_ADMIN/Property/propertyFinance");
        }
        return modelAndView;
    }
}
