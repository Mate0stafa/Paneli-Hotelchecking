package com.example.paneli.Services;


import com.example.paneli.Models.Property;
import com.example.paneli.Repositories.CityRepository;
import com.example.paneli.Repositories.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("PropertyService")
public class PropertyService implements PropertyServiceNotImplemented{
    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
public List<Property> findAllProperties(){
        return propertyRepository.findAll();
}
    @Override
    public Property getPropertyId(long id) {
        return null;
    }

    public long getPropertySize(){
        return propertyRepository.findAll().size();
    }

    @Override
    public Page<Property> findPaginated(int pageNo, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNo-1, pageSize);
        return this.propertyRepository.findAll((org.springframework.data.domain.Pageable) pageable);
    }

    @Override
    public Page<Property> findPaginatedByNameContaining(String searchValue, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.propertyRepository.findAllByNameContaining(searchValue, pageable);
    }

    @Override
    public Page<Property> findPaginatedByCity(Long id, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return this.propertyRepository.findAllByCity(cityRepository.getById(id), pageable);
    }

    @Override
    public Page<Property> findPaginatedByCity(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return  this.propertyRepository.findAllByCity(cityRepository.findByFull_name(name), pageable);
    }




    public List<Property> searchProperty(String searchvalue) {

        List<Property> properties = new ArrayList<Property>();
        List<Property> propertiess = new ArrayList<Property>();

        if ((searchvalue!=null)&&(searchvalue!="")){
            if (searchvalue.contains(",")){
                String[] substrings =searchvalue.split(",");
                properties = propertyRepository.findAllByName(searchvalue);
            }else{
                properties = propertyRepository.findAllByName(searchvalue);
            }
        }
        return properties;
    }

    public List<String> findMatchingProperties(String input) {
        Set<String> uniquePropertyNames = new LinkedHashSet<>();

        List<Property> properties = propertyRepository.findAllByNameContaining(input);
        for (Property property : properties) {
            String propertyName = property.getName();
            if (!uniquePropertyNames.contains(propertyName)) {
                uniquePropertyNames.add(propertyName);
            }
        }

        return new ArrayList<>(uniquePropertyNames);
    }

    public boolean hasGroupAccountUser(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .map(property -> property.getRoles()
                        .stream()
                        .flatMap(role -> role.getUsers().stream())
                        .flatMap(user -> user.getRole().stream())
                        .anyMatch(role -> role.getId() == 3L))
                .orElse(false);
    }


}
