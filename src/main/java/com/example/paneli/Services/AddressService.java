package com.example.paneli.Services;


import com.example.paneli.Models.Address;
import com.example.paneli.Repositories.AddressRepostitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AddressService")
public class AddressService {
    @Autowired
    private AddressRepostitory addressRepostitory;

    public List<Address> getAllAddresses(){
        return addressRepostitory.findAll();
    }
}
