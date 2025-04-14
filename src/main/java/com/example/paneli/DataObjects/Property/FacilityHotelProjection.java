package com.example.paneli.DataObjects.Property;



public interface FacilityHotelProjection {
    Long getId();
    String getPhoto();
    String getName();
    String getDescription();
    String getCity();
    String getRooms();
    String getStatus();
    Boolean getPromote();
    Boolean getSeasonal();

    String getAddressTelephone();
    String getAddressEmail();
}
