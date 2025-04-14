package com.example.paneli.Repositories;

import com.example.paneli.DataObjects.Property.FacilityHotelProjection;
import com.example.paneli.DataObjects.Property.PropertyNameAndEmailProjection;
import com.example.paneli.DataObjects.Property.SearchDto;
import com.example.paneli.DataObjects.Property.SearchPropertyDTO;
import com.example.paneli.Models.*;
import com.example.paneli.Models.PanelUsers.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("select p from Property p where LOWER(p.name) like LOWER(CONCAT('%', :name, '%'))")
    Page<Property> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("select  p.id AS id, p.name AS name, p.address.email AS email from Property p where LOWER(p.name) like LOWER(CONCAT('%', :name, '%'))")
    List<PropertyNameAndEmailProjection> findByNameContainingIgnoreCaseProjection(String name);

    Page<Property> findById(Long id, Pageable pageable);
    @Query("select p from Property p where p.name = ?1 and p.id = ?2")
    Property findByNameAndId(String name, Long id);

    @Query("select p from Property p where p.hotel_status = ?1")
    List<Property> findAllByHotel_status(HotelStatus hotelStatus);

    @Query("select p from Property p where p.id = ?1")
    List<Property> findAllById(Long id);

    @Query("select p from Property p where p.id < ?1")
    List<Property> findAllByIdBefore(Long num);



    @Query("select p from Property p where p.name like %?1%")
    List<Property> findAllByNameContaining(String name);

    @Query("select p from Property p where p.name like %?1%")
    Page<Property> findAllByNameContaining(String name, Pageable pageable);

    @Query("select p from Property p where p.address.city = ?1")
    List<Property> findAllByCity(City city);

    @Query("select p from Property p where p.name = ?1")
    Property findByName(String name);

    @Query("select p from Property p where p.hotel_type = ?1")
    List<Property> findByHotel_type(Hotel_Type hotel_type);

    @Query("select p from Property p where p.address.city = ?1")
    Page<Property> findAllByCity(City city, Pageable pageable);

    @Query("select p from Property p where p.agreement.id = ?1")
    Property findByAgreementId(Long agreement);

    @Query("select p from Property p where p.id = ?1")
    Optional<Property> findById(Long id);

    @Query("select p from Property p where p.id = ?1")
    Page<Property> findByIds(Long id, Pageable pageable);


    @Query("SELECT p.id FROM Property p")
    List<Long> findAllIds();

    @Query("SELECT COUNT(p) FROM Property p WHERE p.hotel_status.id = :hotelStatusId")
    Long countByHotelStatus(@Param("hotelStatusId") int hotelStatusId);


    @Query("SELECT new com.example.paneli.DataObjects.Property.SearchPropertyDTO(p.id, p.name, p.hotel_status.status, p.address.city.full_name) " +
            "FROM Property p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<SearchPropertyDTO> findAllByNameContainingIgnoreCase(String name);

    @Query("SELECT new com.example.paneli.DataObjects.Property.SearchPropertyDTO(p.id, p.name, p.hotel_status.status, p.address.city.full_name) FROM Property p WHERE p.id = :id")
    SearchPropertyDTO findByIdForSearchResult(Long id);

    @Query("SELECT new com.example.paneli.DataObjects.Property.SearchDto(p.id, p.name) FROM Property p WHERE p.id = :id")
    SearchDto findByIdForSearch(Long id);

    @Query("select  new com.example.paneli.DataObjects.Property.SearchDto(p.id, p.name, p.address.city.full_name, p.country) from Property p where p.name like %?1%")
    Page<SearchDto> findAllByNameContainingSearch(String name, Pageable pageable);

    @Query("SELECT p FROM Property p WHERE p.showProperty = :showPropertyValue")
    List<Property> findByShowPropertyValue(@Param("showPropertyValue") int showPropertyValue);


    @Query(value = "select p.id as id, p.name as name, p.address.address_city as city, " +
            "p.hotel_status.status as status, p.number_of_rooms as rooms, p.promote as promote, " +
            "p.seasonalDeals as seasonal, p.address.telephone as addressTelephone, " +
            "p.address.email as addressEmail " +
            "from Property p")
    List<FacilityHotelProjection> getPropertyProjection();

    @Query("select p.name from Property p")
    List<String> findAllPropertyNames();




    @Query("select count(p) > 0 from Property p where p.name = ?1")
    boolean existsByName(String name);

    @Query("select p from Property p where p.name = ?1")
    List<Property> existsByNamee(String name);

    @Query("SELECT p.id AS id, p.name AS name, a.city AS city, p.number_of_rooms AS numberOfRooms, hs.status AS status, " +
            "a.telephone AS phoneNumber, a.email AS email, p.promote AS promote, p.showProperty AS showProperty, p.seasonalDeals AS seasonalOffer " +
            "FROM Property p " +
            "LEFT JOIN p.address a " +
            "LEFT JOIN p.hotel_status hs ")
    Page<Property> findAllProperties(Pageable pageable);

    @Query("select p from Property p where p.name = ?1")
    List<Property> findAllByName(String name);


    @Query("SELECT p FROM Property p WHERE p.status = 'review' OR p.status = 'confirmed'")
    List<Property> findAllByStatusReview();

    @Query("select p from Property p where p.id = ?1")
     Property findByIdd(Long id  );

    @Query("SELECT p FROM Property p JOIN p.roles r WHERE r = :role")
    List<Property> findPropertiesByRole(@Param("role") Role role);

    // Search by name with projection
    @Query("SELECT p.id AS id, p.name AS name, p.address.email AS email " +
            "FROM Property p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PropertyNameAndEmailProjection> findByNameContainingIgnoreCase(String name);

    // Search by ID with projection
    @Query("SELECT p.id AS id, p.name AS name, p.address.email AS email " +
            "FROM Property p " +
            "WHERE p.id = :id")
    Optional<PropertyNameAndEmailProjection> findByID(Long id);

    // Search by both ID and name with projection
    @Query("SELECT p.id AS id, p.name AS name, p.address.email AS email " +
            "FROM Property p " +
            "WHERE p.id = :id AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<PropertyNameAndEmailProjection> findByIdAndNameContainingIgnoreCase(@Param("id") Long id, @Param("name") String name);

    // Fetch all properties with projection
    @Query("SELECT p.id AS id, p.name AS name, p.address.email AS email " +
            "FROM Property p")
    List<PropertyNameAndEmailProjection> findAllBy();
}


