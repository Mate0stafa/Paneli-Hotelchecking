package com.example.paneli.Services;

import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public Boolean checkIfUserDetailsAreCorrect(String username, String password){

        User user = new User();
        if (userRepository.findByUsername(username)!=null){
            if (username.equalsIgnoreCase("admin")){
                user = userRepository.findById(1L).get();
            }else {
                user = userRepository.findByUsername(username);
            }
            if (user == null){
                return false;
            }
        } else if (userRepository.findByUsername(username)==null) {
            return false;
        } else {
            if (username.equalsIgnoreCase("admin")){
                user = userRepository.findById(1L).get();
            }else {
                Long userId = Long.valueOf(username) - 2654434l;
                user = userRepository.findById(userId).get();
            }
            if (user == null){
                return false;
            }
        }

        if (bCryptPasswordEncoder.matches(password, user.getPassword())){
            return true;
        }
        return false;
    }
}
