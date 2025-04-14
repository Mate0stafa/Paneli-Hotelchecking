package com.example.paneli.Services;


import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@Service
public class UserRequestService {

    @Autowired
    UserRepository userRepository;

    public Property getUserProperty(HttpServletRequest request){
        User user = userRepository.findByUsername(request.getUserPrincipal().getName());
        Role role = getUserUniqueRole(user);
        Property property = role.getProperties().get(0);
        return property;
    }


    public Role getUserUniqueRole(User user){
        Role role = user.getRole().stream().filter(x -> x.getAuthority()!="ROLE_USER").collect(Collectors.toList()).get(0);

        return role;
    }

    public Role getRoleOfUser(User user){
        Role role = new Role();

        for (int i=0;i<user.getRole().size();i++){
            if (user.getRole().get(i).getAuthority()!="ROLE_USER"){
                role = user.getRole().get(i);
            }
        }
        return role;
    }

}
