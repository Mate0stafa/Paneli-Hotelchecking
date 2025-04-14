package com.example.paneli.Controllers;

import com.example.paneli.CrudRepositories.PropertyPointsOfInterestCrudRepository;
import com.example.paneli.Models.PointsOfInterest;
import com.example.paneli.Models.Property;
import com.example.paneli.Models.PropertyPointsOfInterest;
import com.example.paneli.Repositories.PointsOfInterestRepository;
import com.example.paneli.Repositories.PropertyPointsOfInterestRepository;
import com.example.paneli.Repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class PointOfInterestController {

    @Autowired
    PropertyRepository propertyRepository;
    @Autowired
    PointsOfInterestRepository pointsOfInterestRepository;
    @Autowired
    PropertyPointsOfInterestRepository propertyPointsOfInterestRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "PointsOfInterests")
    public ModelAndView PointsOfInterests(HttpServletRequest request, ModelAndView modelAndView){

        if (request.isUserInRole("ROLE_ADMIN")) {

            List<PointsOfInterest> pointsOfInterests = pointsOfInterestRepository.findAll();
            modelAndView.addObject("points", pointsOfInterests);
            modelAndView.setViewName("ROLE_ADMIN/PointOfInterests/PointsOfInterests");

        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "editPoint")
    public ModelAndView editpoint(HttpServletRequest request, ModelAndView modelAndView, @RequestParam(value = "id") Long id){

        if(request.isUserInRole("ROLE_ADMIN")) {

            PointsOfInterest pointsOfInterest = pointsOfInterestRepository.findById(id).get();
            modelAndView.addObject("point", pointsOfInterest);
            modelAndView.setViewName("ROLE_ADMIN/PointOfInterests/EditPoint");

        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "editPoint")
    @ResponseBody
    public ResponseEntity<?> editpointOfinterest(@RequestParam(value = "id") Long id, @RequestParam(value = "name") String name) {
        Optional<PointsOfInterest> pointOptional = pointsOfInterestRepository.findById(id);
        if (pointOptional.isPresent()) {
            PointsOfInterest pointsOfInterest = pointOptional.get();
            pointsOfInterest.setName(name);
            pointsOfInterestRepository.save(pointsOfInterest);
            return ResponseEntity.ok().body("/PointsOfInterests");
        } else {
            return ResponseEntity.badRequest().body("Point of interest not found");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "addNewPointOfInterest")
    public ModelAndView addNewPointOfInterest(HttpServletRequest request, ModelAndView modelAndView){

        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.setViewName("ROLE_ADMIN/PointOfInterests/AddNewPointOfInterests");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "addNewPointOfInterest")
    @ResponseBody
    public ResponseEntity<?> addNewPoint(HttpServletRequest request, @RequestParam(value = "name") String name) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            if (pointsOfInterestRepository.findAllByName(name).size() >= 1) {
                return ResponseEntity.badRequest().body("Point of interest already exists");
            } else {
                PointsOfInterest pointsOfInterest = new PointsOfInterest(name, 0, new Date(), new Date());
                pointsOfInterestRepository.save(pointsOfInterest);
                System.out.println(name);
                return ResponseEntity.ok().body("/PointsOfInterests");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "addNewPropertyPoint")
    public ModelAndView addNewPropertyPoint(HttpServletRequest request,
                                            ModelAndView modelAndView,
                                            @RequestParam(value = "propertyId") Long propertyId,
                                            @RequestParam(value = "pointId") Long pointId){

        if (request.isUserInRole("ROLE_ADMIN")) {

            Property property = propertyRepository.findById(propertyId).get();
            PointsOfInterest pointsOfInterest = pointsOfInterestRepository.findById(pointId).get();

            modelAndView.addObject("point", pointsOfInterest);
            modelAndView.addObject("property", property);
            modelAndView.setViewName("ROLE_ADMIN/PointOfInterests/AddNewPropertyPointOfInterest");

        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "addNewPropertyPoint")
    @ResponseBody
    public HttpStatus addNewPropertyPointPost(HttpServletRequest request,
                                              @RequestParam(value = "propertyId") Long propertyId,
                                              @RequestParam(value = "pointId") Long pointId,
                                              @RequestParam(value = "pointName") String pointName,
                                              @RequestParam(value = "pointDistance") Float pointDistance,
                                              @RequestParam(value = "distanceValue") String distanceValue){
        Property property = new Property();
        PointsOfInterest pointsOfInterest = new PointsOfInterest();

        if (propertyRepository.findById(propertyId).get()!=null&&pointsOfInterestRepository.findById(pointId).get()!=null){
            property = propertyRepository.findById(propertyId).get();
            pointsOfInterest = pointsOfInterestRepository.findById(pointId).get();

            PropertyPointsOfInterest propertyPointsOfInterest = new PropertyPointsOfInterest(pointName,
                    pointDistance,
                    0,
                    distanceValue,
                    pointsOfInterest,
                    property);
            propertyPointsOfInterestRepository.save(propertyPointsOfInterest);
            System.out.println(distanceValue);
            System.out.println(pointName);
            System.out.println(pointDistance);
            return HttpStatus.OK;
        }else return HttpStatus.BAD_REQUEST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "propertyPointOfInterest")
    public ModelAndView propertyPointOfInterest(HttpServletRequest request, ModelAndView modelAndView, @RequestParam(value = "id") Long id){

        if (request.isUserInRole("ROLE_ADMIN")) {

            List<PointsOfInterest> pointsOfInterests = pointsOfInterestRepository.findAll();
            modelAndView.addObject("points", pointsOfInterests);
            Property property = propertyRepository.findById(id).get();
            modelAndView.addObject(property);
            modelAndView.setViewName("ROLE_ADMIN/PointOfInterests/PropertyPointOfInterests");

        }
        return modelAndView;
    }

    @Autowired
    PropertyPointsOfInterestCrudRepository propertyPointsOfInterestCrudRepository;



    @PostMapping(value = "deletePoint")
    @ResponseBody
    public HttpStatus deletePoint(HttpServletRequest request,
                                  @RequestParam(value = "pointId") Long pointId){
        if (propertyPointsOfInterestRepository.findById(pointId).get()==null){
            return HttpStatus.BAD_REQUEST;
        }else {
            propertyPointsOfInterestCrudRepository.deleteById(pointId);
        }
        return HttpStatus.OK;
    }

    @GetMapping(value = "editPropertyPoint")
    public ModelAndView editPropertyPoint(HttpServletRequest request, ModelAndView modelAndView, @RequestParam(value = "id") Long id){
        PropertyPointsOfInterest propertyPointsOfInterest = propertyPointsOfInterestRepository.findById(id).get();
        Property property = propertyPointsOfInterest.getProperty();

        modelAndView.addObject("property", property);
        modelAndView.addObject("point", propertyPointsOfInterest);
        modelAndView.setViewName("ROLE_ADMIN/PointOfInterests/EditPropertyPoint");
        return modelAndView;
    }

    @PostMapping(value = "editPropertyPoint")
    @ResponseBody
    public HttpStatus addNewPropertyPointPost(HttpServletRequest request,
                                              @RequestParam(value = "pointId") Long pointId,
                                              @RequestParam(value = "pointName") String pointName,
                                              @RequestParam(value = "pointDistance") Float pointDistance,
                                              @RequestParam(value = "distanceValue") String distanceValue){
        if (propertyPointsOfInterestRepository.findById(pointId).get()!=null){
            PropertyPointsOfInterest propertyPointsOfInterest = propertyPointsOfInterestRepository.findById(pointId).get();

            propertyPointsOfInterest.setPointName(pointName);
            propertyPointsOfInterest.setPointDistance(pointDistance);
            propertyPointsOfInterest.setDistanceType(distanceValue);
            propertyPointsOfInterestRepository.save(propertyPointsOfInterest);
            return HttpStatus.OK;
        }else return HttpStatus.BAD_REQUEST;

    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deletePropertyPoint")
    @ResponseBody
    public void deletePropertyPoint(@RequestParam Long id, HttpServletRequest request){
        if (request.isUserInRole("ROLE_ADMIN")){
            Optional<PointsOfInterest> pointsOfInterestOptional = pointsOfInterestRepository.findById(id);
            if (pointsOfInterestOptional.isPresent()){
                pointsOfInterestRepository.deleteById(id);
            }
        }
    }

}