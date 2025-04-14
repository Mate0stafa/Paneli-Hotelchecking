package com.example.paneli.Repositories;

import com.example.paneli.Models.DescriptionChangeRequest;
import com.example.paneli.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescriptionChangeRequestRepository extends JpaRepository<DescriptionChangeRequest, Long> {
    List<DescriptionChangeRequest> findByPropertyAndStatus(Property property, DescriptionChangeRequest.RequestStatus status);
}