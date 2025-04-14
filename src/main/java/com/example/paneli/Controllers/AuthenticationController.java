package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.Auth.AuthKeyConfirm;
import com.example.paneli.DataObjects.Auth.UserDetailsObj;
import com.example.paneli.DataObjects.Auth.UserRequest;
import com.example.paneli.Models.Auth.PropertyAddRequest;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.PropertyAddRequestRepository;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.UserPanel.RoleRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import com.example.paneli.Services.AuthorizationService;
import com.example.paneli.Services.Mail.JavaMailService;
import com.example.paneli.Services.PropertyService;
import com.example.paneli.Services.RoleUniqueService;
import com.example.paneli.Services.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AuthenticationController {

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthorizationService authorizationService;
    @Autowired
    RoleUniqueService roleUniqueService;
    @Autowired
    PropertyAddRequestRepository propertyAddRequestRepository;
    @Autowired
    JavaMailService javaMailService;
    @Autowired
    UserRequestService userRequestService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PropertyService propertyService;

    @PostMapping(value = "getToken")
    @ResponseBody
    public UserDetailsObj authenticate(@RequestBody UserRequest userRequest) throws MessagingException {
            Boolean userFound = authorizationService.checkIfUserDetailsAreCorrect(userRequest.getUsername(), userRequest.getPassword());
            User user = new User();
            Role role = new Role();
            PropertyAddRequest propertyAddRequest = new PropertyAddRequest();

            if (userFound) {
                user = userRepository.findByUsername(userRequest.getUsername());

                    // Gjej rolin special të përdoruesit
                    Role specialRole = user.getRole().stream()
                            .filter(role1 -> role1.getId() != 1L && role1.getId() != 2L && role1.getId() != 3L)
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("No special role found for the user."));
                    // Gjej pronat e lidhura me rolin special
                    List<Property> linkedProperties = propertyRepository.findPropertiesByRole(specialRole);
                    for (Property propertyl : linkedProperties) {
                        System.out.println("------------- " + propertyl.getName());
                    }


                Property property = propertyRepository.findById(userRequest.getProperty_id()).get();
                System.out.println("Property ID: "+property.getId());
                for (int i = 0; i < user.getRole().size(); i++) {
                    if (user.getRole().get(i).getAuthority() != "ROLE_USER") {
                        role = user.getRole().get(i);
                    }
                }

                Random rnd = new Random();
                int min = 100000;
                int max = 999999;
                int number = rnd.nextInt(max - min + 1) + min;
                String uniqueKey = String.valueOf(number);
                System.out.println("Add existing properties code-> " + uniqueKey);

                propertyAddRequest = new PropertyAddRequest(
                        0,
                        0,
                        uniqueKey,
                        new Date(),
                        new Date(),
                        user,
                        property.getId());

                propertyAddRequestRepository.save(propertyAddRequest);

                javaMailService.sendMailPinCode(user, propertyAddRequest);

                UserDetailsObj userDetailsObj = new UserDetailsObj(true, user.getId(), propertyAddRequest.getId());
                return userDetailsObj;
            }

        return new UserDetailsObj(false, 0L, 0L);
    }

    @Autowired
    PropertyRepository propertyRepository;

    @PostMapping(value = "addPropertyToMyAccount")
    @ResponseBody
    public void addPropertyTomyAccount(HttpServletRequest request,
                                       @RequestParam(value = "propertyId") Long propertyId,
                                       @RequestParam(value = "key") Long key,
                                       @RequestParam(value = "acrualpropertyId") Long acrualpropertyId) throws MessagingException {
        Property property = propertyRepository.findById(propertyId).get();
        PropertyAddRequest propertyAddRequest = propertyAddRequestRepository.findById(key).get();
        User user = propertyAddRequest.getUser();
        User currentLogedInUser = userRepository.findByUsername(request.getUserPrincipal().getName());
        Property property2 = propertyRepository.findById(acrualpropertyId).get();

        System.out.println("Login Username : " + currentLogedInUser.getUsername());
        System.out.println("Login role1 : " + currentLogedInUser.getRole().get(0).getAuthority());
        System.out.println("Login role2 : " + currentLogedInUser.getRole().get(1).getAuthority());
        System.out.println("New Property Username : " + user.getUsername());
        System.out.println("New Property role1 : " + user.getRole().get(0).getAuthority());
        System.out.println("New Property role2 : " + user.getRole().get(1).getAuthority());


        //  Useri i indentifikuar
        Role basicRolelogin = null;  // Roli bazik (me ID = 1, që do të ndryshohet në 3)
        Role specialRolelogin = null;  // Roli unik për të përditësuar autoritetin

        List<Role> roleslogin = currentLogedInUser.getRole();
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

        //  Useri i prones qe do te shtohet ne groupaccount
        Role basicRole = null;  // Roli bazik (me ID = 1, që do të ndryshohet në 3)
        Role specialRole = null;  // Roli unik për të përditësuar autoritetin

        List<Role> rolesnewproperty = user.getRole();
        for (Role role1 : rolesnewproperty) {
            if (role1.getId() == 1L) {
                basicRole = role1;
            }else if (role1.getId() == 2L) {
                basicRolelogin = role1;
            } else if (role1.getId() == 3L) {
                basicRolelogin = role1;
            } else {
                specialRole = role1;
            }
        }

        if (basicRolelogin != null && specialRolelogin != null) {
            if (basicRolelogin.getAuthority().equals("ROLE_USER")){
                if (basicRole.getAuthority().equals("ROLE_USER")){
                    System.out.println("Fillon procesi per ndryshimin e rolit te uerit te indentifikuar .");
                    System.out.println("Fillon procesi per shtimin e ROLE_GROUP_ACCOUNT .");
                    Role newBasicRole = roleRepository.findById(3L).orElse(null);
                    if (newBasicRole != null) {
                        roleslogin.set(roleslogin.indexOf(basicRolelogin), newBasicRole);
                    }

                    String roleName = "GROUP_ACCOUNT_" + currentLogedInUser.getUsername() + "_" + UUID.randomUUID().toString();
                    Role newGroupRole = new Role();
                    newGroupRole.setAuthority(roleName);
                    newGroupRole.setVersion(0);
                    roleRepository.saveAndFlush(newGroupRole);

                    roleslogin.remove(specialRolelogin);
                    roleslogin.add(newGroupRole);
                    userRepository.save(currentLogedInUser);

                    System.out.println("Roli bazik u ndryshua nga ID=1 në ID=3, dhe u ndertua nje rol i ri per group account .");
                    System.out.println("Fillon procesi per ndryshimin e rolit te uerit qe i perket prones se re .");
                    if (newBasicRole != null) {
                        if(user.getId() == currentLogedInUser.getId()){
                            System.out.println("Ky kusht sherben per te menaxhuar rastet kur useri i loguar ka ftuar veten per te krijuar group acc");
                            System.out.println("Ne kete situate rolesnewproperty e ka mare vleren nga me siper .");
                        }else {
                            rolesnewproperty.set(rolesnewproperty.indexOf(basicRole), newBasicRole);
                        }
                    }
                    rolesnewproperty.remove(specialRole);
                    rolesnewproperty.add(newGroupRole);
                    userRepository.save(user);

                    propertyAddRequest.setStatus(1);
                    propertyAddRequestRepository.save(propertyAddRequest);

//                  Shtimi i rolit te group acc ne pronat perkatese
                    List<Role> rolesProperty1 = property.getRoles();
                    List<Role> rolesProperty2 = property2.getRoles();
                    for (Role role : rolesProperty1) {
                        System.out.println("4444 " + role.getId());
                    }
                    for (Role role : rolesProperty2) {
                        System.out.println("4444 " + role.getId());
                    }
                    if (!rolesProperty1.contains(newGroupRole)) {
                        System.out.println("1111 " + newGroupRole.getId());
                        System.out.println(property.getName());
                        rolesProperty1.add(newGroupRole);
                        propertyRepository.save(property);
                    }
                    if (!rolesProperty2.contains(newGroupRole)) {
                        System.out.println("2222 " + newGroupRole.getId());
                        System.out.println(property2.getName());
                        rolesProperty2.add(newGroupRole);
                        propertyRepository.save(property2);
                    }

                    System.out.println("Prona u shtua me sukses => 1 ");
                    javaMailService.sendemailwhengroupaccountiscreated(currentLogedInUser, property,newGroupRole);
                    System.out.println("Emaili u dergua me sukses ne userin e indentifikuar =>  " + currentLogedInUser.getEmail());
                    javaMailService.sendemailwhengroupaccountiscreated(user, property2,newGroupRole);
                    System.out.println("Emaili u dergua me sukses ne userin e prones se shtuar =>  " + user.getEmail());
                    javaMailService.sendemailwhengroupaccountiscreatedAdmin(property, property2, newGroupRole);
                    System.out.println("Admin notification");
                }else {
                    System.out.println("1- The property cannot be added because its role is not 'ROLE_USER'.");
                }
            }else{
                System.out.println("The property cannot be added");
            }
        } else {
            System.out.println("Njëri nga rolet nuk u gjet. Verifiko konfigurimin e roleve për përdoruesin aktual.");
        }
    }
}
