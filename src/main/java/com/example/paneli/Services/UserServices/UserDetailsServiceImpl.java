package com.example.paneli.Services.UserServices;

import com.example.paneli.Models.PanelUsers.User;
import com.example.paneli.Repositories.PropertyRepository;
import com.example.paneli.Repositories.UserPanel.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
//        User user = new User();
//        if (id.equalsIgnoreCase("admin")){
//            user = userRepository.findById(1L).get();
//        }else {
//            Long userId = Long.valueOf(id) - 2654434l;
//            user = userRepository.findById(userId).get();
//        }
//        if (user == null){
//            throw new UsernameNotFoundException("User not found!");
//        }
//        return new com.example.paneli.Services.UserServices.UserDetails(user);
//    }

    @Autowired
    PropertyRepository propertyRepository;

    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = new User();

        System.out.println(id+"dkkdfdfkkfdk");
        if (userRepository.findByUsername(id)!=null){
            if (id.equalsIgnoreCase("admin")){
                user = userRepository.findById(1L).get();
            }else {

                user = userRepository.findByUsername(id);
            }
            if (user == null){
                throw new UsernameNotFoundException("User not found!");
            }
        }else {
            if (id.equalsIgnoreCase("admin")){
                user = userRepository.findById(1L).get();
            }else {
                Long userId = Long.valueOf(id) - 2654434l;
                user = userRepository.findById(userId).get();
            }
            if (user == null){
                throw new UsernameNotFoundException("User not found!");
            }
        }
        return new com.example.paneli.Services.UserServices.UserDetails(user);
    }



}
