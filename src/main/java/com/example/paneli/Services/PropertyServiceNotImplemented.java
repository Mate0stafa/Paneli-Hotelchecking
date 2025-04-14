package com.example.paneli.Services;


import com.example.paneli.Models.Property;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PropertyServiceNotImplemented {
    List<Property> getAllProperties();
    Property getPropertyId(long id);

    Page<Property> findPaginated(int pageNo, int pageSize);
    Page<Property> findPaginatedByNameContaining(String searchValue, int pageNo, int pageSize);
    Page<Property> findPaginatedByCity(Long id, int pageNo, int pageSize);
    Page<Property> findPaginatedByCity(String name, int pageNo, int pageSize);

}
