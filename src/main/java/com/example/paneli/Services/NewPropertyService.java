package com.example.paneli.Services;

import com.example.paneli.Repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewPropertyService {
    @Autowired
    PropertyRepository propertyRepository;


}
