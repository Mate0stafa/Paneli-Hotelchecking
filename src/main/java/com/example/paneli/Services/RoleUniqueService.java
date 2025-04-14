package com.example.paneli.Services;

import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RoleUniqueService {

    @Autowired
    UserRepository userRepository;

    public Role getUniqueRole(HttpServletRequest request){

        Role role = new Role();
        User user = userRepository.findByUsername(request.getUserPrincipal().getName());
        List<Role> roleList = user.getRole();

        for (int i=0;i<roleList.size();i++){
            if (roleList.get(i).getAuthority()!="ROLE_USER"){
                role = roleList.get(i);
            }
        }

        return role;

    }

    public Role getUniqueRoleByUser(User user){

        List<Role> roleList = user.getRole();
        Role role = new Role();

        for (int i=0;i<roleList.size();i++){
            if (roleList.get(i).getAuthority()!="ROLE_USER"){
                role = roleList.get(i);
            }
        }
        return role;
    }



}
