package com.example.paneli.Models;


import com.example.paneli.Models.Contract.Agreement;
import com.example.paneli.Models.Contract.AgreementRequest;
import com.example.paneli.Models.PanelUsers.Role;
import com.example.paneli.Models.RoomAmenities.Amenity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "property")
@Transactional
public class  Property {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "version")
    private int version;
    @Column(name = "stars")
    private int stars;
    @Column(name = "number_of_rooms")
    private int number_of_rooms;
    @Column(name = "hotel_logo")
    private String hotel_logo;
    @Column(name = "checked_out")
    private Boolean checked_out;
    @Column(name = "name")
    private String name;
    @Column(name = "offset_id")
    private Long offsetId;
    @Column(name = "country", columnDefinition="LONGTEXT")
    private String country;
    @Column(name = "show_property", nullable = false)
    private int showProperty;
    @Column(name = "promote", nullable = false)
    private boolean promote;

    @Column(name = "vat_registration_date")
    private Date registationDate;

    @Column(name = "tax_included",columnDefinition = "TINYINT(1) NOT NULL DEFAULT 1") //by default city tax is included
    private boolean taxIncluded;

    @Column(name = "private_host", nullable = false)
    private boolean privateHost;

    @Column(name = "professional_host", nullable = false)
    private boolean professionalHost;

    @Column(name = "status",nullable = false, columnDefinition = "VARCHAR(36) DEFAULT 'incomplete'")
    private String status;

    @Column(name = "submission_date")
    private LocalDate submissionDate;

    @Column(name = "username")
    private String username;


    @Column(name = "name_change", nullable = false)
    private boolean nameChange;

    @Column(name ="tax_extract_file_Name" )
    private String taxExtractFileName;

    public String getTaxExtractFileName() {
        return taxExtractFileName;
    }

    public void setTaxExtractFileName(String taxExtractFileName) {
        this.taxExtractFileName = taxExtractFileName;
    }

    public boolean isPropertyChange() {
        return nameChange;
    }

    public void setPropertyChange(boolean propertyChange) {
        this.nameChange = propertyChange;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(LocalDate submissionDate) {
        this.submissionDate = submissionDate;
    }

    public boolean isPrivateHost() {
        return privateHost;
    }

    public void setPrivateHost(boolean privateHost) {
        this.privateHost = privateHost;
    }

    public boolean isProfessionalHost() {
        return professionalHost;
    }

    public void setProfessionalHost(boolean professionalHost) {
        this.professionalHost = professionalHost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "property_amenity",
            joinColumns = { @JoinColumn(name = "property_id")},
            inverseJoinColumns = { @JoinColumn (name = "amenity_id")})
    private List<Amenity> amenities;

    @ManyToMany
    @JoinTable(
            name = "property_role",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonBackReference
    private List<Role> roles;

    @ManyToMany
    @JoinTable(name = "property_hotel_facility",
            joinColumns = { @JoinColumn(name = "property_hotel_facility_id")},
            inverseJoinColumns = { @JoinColumn (name = "hotel_facility_id")})
    @JsonManagedReference
    private List<HotelFacility> hotel_facility;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "address_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonBackReference
    private Address address;

    @OneToOne
    @JoinColumn(name = "hotelierId_id")
    @JsonBackReference
    private HotelierId hotelierId  ;

    @ManyToOne
    @JoinTable(name = "property_hotel_type",
            joinColumns = { @JoinColumn(name = "property_id")},
            inverseJoinColumns = { @JoinColumn (name = "hotel_type_id")})
    @JsonBackReference
    private Hotel_Type hotel_type;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "hotel_time_id")
    @JsonManagedReference
    private HotelTime hotel_time;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "property_hotel_attribute",
            joinColumns = { @JoinColumn(name = "property_hotel_attribute_id")},
            inverseJoinColumns = { @JoinColumn (name = "hotel_attribute_id")})
    @JsonManagedReference
    private List<HotelAttribute> hotel_attribute = new ArrayList<>();

    @OneToMany(mappedBy = "property")
    @JsonManagedReference
    List<PropertyPointsOfInterest> propertyPointsOfInterestList;

    @Column(name = "accept_card")
    private Boolean acceptCard;

    public Boolean getAcceptCard() {
        return acceptCard != null ? acceptCard : false;
    }

    public void setAcceptCard(Boolean acceptCard) {
        this.acceptCard = acceptCard;
    }

    @OneToMany(mappedBy = "property",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<HotelPhoto> hotelPhotos;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @JsonManagedReference
    private HotelStatus hotel_status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "preferred_language_id")
    @JsonManagedReference
    private Language language;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "property_language",
            joinColumns = { @JoinColumn(name = "property_language_id")},
            inverseJoinColumns = { @JoinColumn (name = "language_id")})
    @JsonManagedReference
    private List<Language> languages;

    public HotelierId getHotelierId() {
        return hotelierId;
    }

    public void setHotelierId(HotelierId hotelierId) {
        this.hotelierId = hotelierId;
    }

    @OneToOne(mappedBy = "property")
    @JsonManagedReference
    private Description description;

    @OneToMany(mappedBy = "property")
    @JsonManagedReference
    private List<ToDo> todoList;

    public List<ToDo> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<ToDo> todoList) {
        this.todoList = todoList;
    }

    @OneToOne(mappedBy = "property")
    @JsonManagedReference
    private AgreementRequest agreementRequest;

    @OneToOne(mappedBy = "property")
    @JsonManagedReference
    private Agreement agreement;

    @OneToMany(mappedBy="property")
    @JsonManagedReference
    private List<ReviewsTab> reviewsTabList;

    @OneToMany(mappedBy="property")
    @JsonManagedReference
    private List<Promotion> promotions;

    @OneToOne(mappedBy = "property")
    private AddressChange addressChange;

    private String firstName;

    private String lastName;

    private String NUIS;

    private String taxname;

    @OneToOne(mappedBy = "property")
    private PropertyChange propertyChange;

    public PropertyChange getPropertyChange() {
        return propertyChange;
    }

    public void setPropertyChange(PropertyChange propertyChange) {
        this.propertyChange = propertyChange;
    }

    public String getTaxname() {
        return taxname;
    }

    public void setTaxname(String taxname) {
        this.taxname = taxname;
    }

    private LocalDate dateofbirth;

    private int realId;

    private boolean seasonalDeals;

    public AddressChange getAddressChange() {
        return addressChange;
    }

    public void setAddressChange(AddressChange addressChange) {
        this.addressChange = addressChange;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Long getOffsetId() {
        return offsetId;
    }

    public void setOffsetId(Long offsetId) {
        this.offsetId = id+2654435l;
    }

    public Long getLoginId(){
        return this.id + 2654435L;
    }

    public Property() {

    }

    public Property(int version, int stars, int number_of_rooms, Boolean checked_out, String name, Long offsetId, String country, Hotel_Type hotel_type, HotelTime hotel_time, List<Role> roles, HotelStatus hotel_status, Language language, boolean seasonalDeals, String firstName, String lastName, String NUIS, boolean promote  ) {
        this.version = version;
        this.stars = stars;
        this.number_of_rooms = number_of_rooms;
        this.checked_out = checked_out;
        this.name = name;
        this.offsetId = offsetId;
        this.country = country;
        this.hotel_type = hotel_type;
        this.hotel_time = hotel_time;
        this.roles = roles;
        this.hotel_status = hotel_status;
        this.language = language;
        this.seasonalDeals = seasonalDeals;
        this.firstName = firstName;
        this.lastName = lastName;
        this.NUIS = NUIS;
        this.promote = promote;
     }
    public Property(int version, int stars, int number_of_rooms, Boolean checked_out, String name, Long offsetId, String country, Hotel_Type hotel_type, HotelTime hotel_time, Boolean acceptCard, List<Role> roles, HotelStatus hotel_status, Language language, boolean seasonalDeals, String firstName, String lastName, String NUIS, boolean promote) {
        this.version = version;
        this.stars = stars;
        this.number_of_rooms = number_of_rooms;
        this.checked_out = checked_out;
        this.name = name;
        this.offsetId = offsetId;
        this.country = country;
        this.hotel_type = hotel_type;
        this.hotel_time = hotel_time;
        this.acceptCard = acceptCard;
        this.roles = roles;
        this.hotel_status = hotel_status;
        this.language = language;
        this.seasonalDeals = seasonalDeals;
        this.firstName = firstName;
        this.lastName = lastName;
        this.NUIS = NUIS;
        this.promote = promote;
    }

    public boolean isPromote() {
        return promote;
    }

    public void setPromote(boolean promote) {
        this.promote = promote;
    }

    public int getShowProperty() {
        return showProperty;
    }

    public void setShowProperty(int showProperty) {
        this.showProperty = showProperty;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getNumber_of_rooms() {
        return number_of_rooms;
    }

    public void setNumber_of_rooms(int number_of_rooms) {
        this.number_of_rooms = number_of_rooms;
    }

    public String getHotel_logo() {
        return hotel_logo;
    }

    public void setHotel_logo(String hotel_logo) {
        this.hotel_logo = hotel_logo;
    }

    public boolean getChecked_out() {
        return checked_out;
    }

    public void setChecked_out(boolean checked_out) {
        this.checked_out = checked_out;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public List<HotelAttribute> getHotel_attribute() {
        return hotel_attribute;
    }

    public void setHotel_attribute(List<HotelAttribute> hotel_attribute) {
        this.hotel_attribute = hotel_attribute;
    }

    public List<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    public boolean isSeasonalDeals() {
        return seasonalDeals;
    }

    public void setSeasonalDeals(boolean seasonalDeals) {
        this.seasonalDeals = seasonalDeals;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public HotelStatus getHotel_status() {
        return hotel_status;
    }

    public void setHotel_status(HotelStatus hotel_status) {
        this.hotel_status = hotel_status;
    }

    public int getStars() {
        return stars;
    }

    public void setHotelPhotos(List<HotelPhoto> hotelPhotos) {
        this.hotelPhotos = hotelPhotos;
    }

    public HotelTime getHotel_time() {
        return hotel_time;
    }

    public void setHotel_time(HotelTime hotel_time) {
        this.hotel_time = hotel_time;
    }

    public int getRealId() {
        return realId;
    }

    public void setRealId(int realId) {
        this.realId = realId;
    }

    public Hotel_Type getHotel_type() {
        return hotel_type;
    }

    public void setHotel_type(Hotel_Type hotel_type) {
        this.hotel_type = hotel_type;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<HotelPhoto> getHotelPhotos() {
        return hotelPhotos;
    }

    public List<HotelFacility> getHotel_facility() {
        return hotel_facility;
    }

    public void setHotel_facility(List<HotelFacility> hotel_facility) {
        this.hotel_facility = hotel_facility;
    }

    public HotelPhoto getPrimaryImage(){
        List<HotelPhoto> hotelPhotos = this.getHotelPhotos();
        List<HotelPhoto> hotelPhotos1 = hotelPhotos.stream().filter(x->x.isSet_primary()==true).collect(Collectors.toList());

        if (hotelPhotos1.stream().count()>0){
            return hotelPhotos1.get(0);
        } else if(hotelPhotos1.stream().count()==0){
            return null;
        }

        else return hotelPhotos.get(0);
    }

    public HotelPhoto getPrimaryImage1(List<HotelPhoto> hotelPhotos) {
        List<HotelPhoto> primaryPhotos = hotelPhotos.stream()
                .filter(HotelPhoto::isSet_primary)
                .collect(Collectors.toList());

        if (!primaryPhotos.isEmpty()) {
            return primaryPhotos.get(0);
        }
        else if (!hotelPhotos.isEmpty()) {
            return hotelPhotos.get(0);
        }

        return null;
    }


    public String getLatitude(){
        String num = this.getAddress().getOn_map();
        if (num==null){
            return "0";
        }else {
            String str[] = num.split(",");
            return str[0];
        }
    }
    public String getLangitude(){
        String num = this.getAddress().getOn_map();
        if (num==null){
            return "0";
        }else if (num.contains(",")==true) {
            String str[] = num.split(",");
            if (str[1]==null || str[1]==""){
                return "";
            }else {
                return str[1];
            }
        }else return "0";
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChecked_out(Boolean checked_out) {
        this.checked_out = checked_out;
    }

    public String getCity() {
        if (this.address.getCity() != null) {
            return this.address.getCity().getFull_name();
        } else {
            return "City not available";
        }
    }

    public List<PropertyPointsOfInterest> getPropertyPointsOfInterestList() {
        return propertyPointsOfInterestList;
    }

    public void setPropertyPointsOfInterestList(List<PropertyPointsOfInterest> propertyPointsOfInterestList) {
        this.propertyPointsOfInterestList = propertyPointsOfInterestList;
    }

    public List<PropertyPointsOfInterest> getByPointInterests(PointsOfInterest pointsOfInterest){
        List<PropertyPointsOfInterest> propertyPointsOfInterestList = this.getPropertyPointsOfInterestList().stream().filter(x->x.getPointsOfInterest()==pointsOfInterest).collect(Collectors.toList());

        return propertyPointsOfInterestList;
    }

    public AgreementRequest getAgreementRequest() {
        return agreementRequest;
    }

    public void setAgreementRequest(AgreementRequest agreementRequest) {
        this.agreementRequest = agreementRequest;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<ReviewsTab> getReviewsTabList() {
        return reviewsTabList;
    }

    public void setReviewsTabList(List<ReviewsTab> reviewsTabList) {
        this.reviewsTabList = reviewsTabList;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getNameFormated(){
        String formatedName = this.getName().replaceAll(" ", "-");
//        formatedName = formatedName.replaceAll("---", "-");
        return formatedName;
    }

    public String getNUIS() {
        return NUIS;
    }

    public void setNUIS(String NUIS) {
        this.NUIS = NUIS;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegistationDate() { return registationDate; }

    public void setRegistationDate(Date registationDate) { this.registationDate = registationDate; }

    public boolean getTaxIncluded() { return taxIncluded; }

    public void setTaxIncluded(boolean taxIncluded) { this.taxIncluded = taxIncluded; }

    public Property(int version, int stars, int number_of_rooms, Boolean checked_out, String name, Long offsetId, String country, Hotel_Type hotel_type, HotelTime hotel_time, List<Role> roles, HotelStatus hotel_status, Language language, boolean seasonalDeals, String firstName, String lastName, String NUIS,LocalDate dateofbirth,String taxname, boolean promote  ) {
        this.version = version;
        this.stars = stars;
        this.number_of_rooms = number_of_rooms;
        this.checked_out = checked_out;
        this.name = name;
        this.offsetId = offsetId;
        this.country = country;
        this.hotel_type = hotel_type;
        this.hotel_time = hotel_time;
        this.roles = roles;
        this.hotel_status = hotel_status;
        this.language = language;
        this.seasonalDeals = seasonalDeals;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateofbirth=dateofbirth;
        this.taxname=taxname;
        this.NUIS = NUIS;
        this.promote = promote;
    }

    public Property(int version, int stars, int number_of_rooms, Boolean checked_out, String name, Long offsetId, String country, Hotel_Type hotel_type, HotelTime hotel_time, HotelStatus hotel_status, Language language, boolean seasonalDeals, String firstName, String lastName, String NUIS,LocalDate dateofbirth,String taxname, boolean promote  ) {
        this.version = version;
        this.stars = stars;
        this.number_of_rooms = number_of_rooms;
        this.checked_out = checked_out;
        this.name = name;
        this.offsetId = offsetId;
        this.country = country;
        this.hotel_type = hotel_type;
        this.hotel_time = hotel_time;
        this.hotel_status = hotel_status;
        this.language = language;
        this.seasonalDeals = seasonalDeals;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateofbirth=dateofbirth;
        this.taxname=taxname;
        this.NUIS = NUIS;
        this.promote = promote;
    }


}
