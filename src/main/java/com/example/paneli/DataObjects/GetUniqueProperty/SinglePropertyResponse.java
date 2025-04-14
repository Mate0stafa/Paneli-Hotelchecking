package com.example.paneli.DataObjects.GetUniqueProperty;

import java.util.ArrayList;

public class SinglePropertyResponse {

    private String name;
    private String propId;
    private String propTypeId;
    private String ownerId;
    private String currency;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postcode;
    private String latitude;
    private String longitude;
    private String phone;
    private String mobile;
    private String fax;
    private String email;
    private String web;
    private String contactFirstName;
    private String contactLastName;
    private String cutOffHour;
    private String vatRate;
    private String controlPriority;
    private String template1;
    private String template2;
    private String template3;
    private String template4;
    private String template5;
    private String template6;
    private String template7;
    private String template8;
    private String notifyUrl;
    private String notifyData;
    private String notifyHeader;
    private String airbnbHost;
    private String airbnbPropertyCode;
    private String airbnbCurrency;
    private String airbnbRequests;
    private String airbnbMessaging;
    private String airbnbInvoicee;
    private String airbnbMultiplier;
    private String agodaComPropertyCode;
    private String bookingComPropertyCode;
    private String bookingComPriceImport;
    private String bookingComRateType;
    private String bookingComInvoiceeId;
    private String bookingComMultiplier;
    private String cTripPropertyCode;
    private String cTripCurrency;
    private String despegarComUsername;
    private String despegarComPassword;
    private String despegarComPropertyCode;
    private String despegarComCurrency;
    private String expediaComPropertyCode;
    private String expediaComCurrency;
    private String icalExportTokenSalt;
    private String icalExportDescription;
    private String icalImportOption;
    private String googleAdsProduct;
    private String odigeoPropertyCode;
    private String vrboInvoicee;
    private String vrboMultiplier;
    private ArrayList<SinglePropertyRoomResponse> roomTypes;

    public SinglePropertyResponse(String name, String propId, String propTypeId, String ownerId, String currency, String address, String city, String state, String country, String postcode, String latitude, String longitude, String phone, String mobile, String fax, String email, String web, String contactFirstName, String contactLastName, String cutOffHour, String vatRate, String controlPriority, String template1, String template2, String template3, String template4, String template5, String template6, String template7, String template8, String notifyUrl, String notifyData, String notifyHeader, String airbnbHost, String airbnbPropertyCode, String airbnbCurrency, String airbnbRequests, String airbnbMessaging, String airbnbInvoicee, String airbnbMultiplier, String agodaComPropertyCode, String bookingComPropertyCode, String bookingComPriceImport, String bookingComRateType, String bookingComInvoiceeId, String bookingComMultiplier, String cTripPropertyCode, String cTripCurrency, String despegarComUsername, String despegarComPassword, String despegarComPropertyCode, String despegarComCurrency, String expediaComPropertyCode, String expediaComCurrency, String icalExportTokenSalt, String icalExportDescription, String icalImportOption, String googleAdsProduct, String odigeoPropertyCode, String vrboInvoicee, String vrboMultiplier, ArrayList<SinglePropertyRoomResponse> roomTypes) {
        this.name = name;
        this.propId = propId;
        this.propTypeId = propTypeId;
        this.ownerId = ownerId;
        this.currency = currency;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postcode = postcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.mobile = mobile;
        this.fax = fax;
        this.email = email;
        this.web = web;
        this.contactFirstName = contactFirstName;
        this.contactLastName = contactLastName;
        this.cutOffHour = cutOffHour;
        this.vatRate = vatRate;
        this.controlPriority = controlPriority;
        this.template1 = template1;
        this.template2 = template2;
        this.template3 = template3;
        this.template4 = template4;
        this.template5 = template5;
        this.template6 = template6;
        this.template7 = template7;
        this.template8 = template8;
        this.notifyUrl = notifyUrl;
        this.notifyData = notifyData;
        this.notifyHeader = notifyHeader;
        this.airbnbHost = airbnbHost;
        this.airbnbPropertyCode = airbnbPropertyCode;
        this.airbnbCurrency = airbnbCurrency;
        this.airbnbRequests = airbnbRequests;
        this.airbnbMessaging = airbnbMessaging;
        this.airbnbInvoicee = airbnbInvoicee;
        this.airbnbMultiplier = airbnbMultiplier;
        this.agodaComPropertyCode = agodaComPropertyCode;
        this.bookingComPropertyCode = bookingComPropertyCode;
        this.bookingComPriceImport = bookingComPriceImport;
        this.bookingComRateType = bookingComRateType;
        this.bookingComInvoiceeId = bookingComInvoiceeId;
        this.bookingComMultiplier = bookingComMultiplier;
        this.cTripPropertyCode = cTripPropertyCode;
        this.cTripCurrency = cTripCurrency;
        this.despegarComUsername = despegarComUsername;
        this.despegarComPassword = despegarComPassword;
        this.despegarComPropertyCode = despegarComPropertyCode;
        this.despegarComCurrency = despegarComCurrency;
        this.expediaComPropertyCode = expediaComPropertyCode;
        this.expediaComCurrency = expediaComCurrency;
        this.icalExportTokenSalt = icalExportTokenSalt;
        this.icalExportDescription = icalExportDescription;
        this.icalImportOption = icalImportOption;
        this.googleAdsProduct = googleAdsProduct;
        this.odigeoPropertyCode = odigeoPropertyCode;
        this.vrboInvoicee = vrboInvoicee;
        this.vrboMultiplier = vrboMultiplier;
        this.roomTypes = roomTypes;
    }

    public SinglePropertyResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPropId() {
        return propId;
    }

    public void setPropId(String propId) {
        this.propId = propId;
    }

    public String getPropTypeId() {
        return propTypeId;
    }

    public void setPropTypeId(String propTypeId) {
        this.propTypeId = propTypeId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getCutOffHour() {
        return cutOffHour;
    }

    public void setCutOffHour(String cutOffHour) {
        this.cutOffHour = cutOffHour;
    }

    public String getVatRate() {
        return vatRate;
    }

    public void setVatRate(String vatRate) {
        this.vatRate = vatRate;
    }

    public String getControlPriority() {
        return controlPriority;
    }

    public void setControlPriority(String controlPriority) {
        this.controlPriority = controlPriority;
    }

    public String getTemplate1() {
        return template1;
    }

    public void setTemplate1(String template1) {
        this.template1 = template1;
    }

    public String getTemplate2() {
        return template2;
    }

    public void setTemplate2(String template2) {
        this.template2 = template2;
    }

    public String getTemplate3() {
        return template3;
    }

    public void setTemplate3(String template3) {
        this.template3 = template3;
    }

    public String getTemplate4() {
        return template4;
    }

    public void setTemplate4(String template4) {
        this.template4 = template4;
    }

    public String getTemplate5() {
        return template5;
    }

    public void setTemplate5(String template5) {
        this.template5 = template5;
    }

    public String getTemplate6() {
        return template6;
    }

    public void setTemplate6(String template6) {
        this.template6 = template6;
    }

    public String getTemplate7() {
        return template7;
    }

    public void setTemplate7(String template7) {
        this.template7 = template7;
    }

    public String getTemplate8() {
        return template8;
    }

    public void setTemplate8(String template8) {
        this.template8 = template8;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getNotifyData() {
        return notifyData;
    }

    public void setNotifyData(String notifyData) {
        this.notifyData = notifyData;
    }

    public String getNotifyHeader() {
        return notifyHeader;
    }

    public void setNotifyHeader(String notifyHeader) {
        this.notifyHeader = notifyHeader;
    }

    public String getAirbnbHost() {
        return airbnbHost;
    }

    public void setAirbnbHost(String airbnbHost) {
        this.airbnbHost = airbnbHost;
    }

    public String getAirbnbPropertyCode() {
        return airbnbPropertyCode;
    }

    public void setAirbnbPropertyCode(String airbnbPropertyCode) {
        this.airbnbPropertyCode = airbnbPropertyCode;
    }

    public String getAirbnbCurrency() {
        return airbnbCurrency;
    }

    public void setAirbnbCurrency(String airbnbCurrency) {
        this.airbnbCurrency = airbnbCurrency;
    }

    public String getAirbnbRequests() {
        return airbnbRequests;
    }

    public void setAirbnbRequests(String airbnbRequests) {
        this.airbnbRequests = airbnbRequests;
    }

    public String getAirbnbMessaging() {
        return airbnbMessaging;
    }

    public void setAirbnbMessaging(String airbnbMessaging) {
        this.airbnbMessaging = airbnbMessaging;
    }

    public String getAirbnbInvoicee() {
        return airbnbInvoicee;
    }

    public void setAirbnbInvoicee(String airbnbInvoicee) {
        this.airbnbInvoicee = airbnbInvoicee;
    }

    public String getAirbnbMultiplier() {
        return airbnbMultiplier;
    }

    public void setAirbnbMultiplier(String airbnbMultiplier) {
        this.airbnbMultiplier = airbnbMultiplier;
    }

    public String getAgodaComPropertyCode() {
        return agodaComPropertyCode;
    }

    public void setAgodaComPropertyCode(String agodaComPropertyCode) {
        this.agodaComPropertyCode = agodaComPropertyCode;
    }

    public String getBookingComPropertyCode() {
        return bookingComPropertyCode;
    }

    public void setBookingComPropertyCode(String bookingComPropertyCode) {
        this.bookingComPropertyCode = bookingComPropertyCode;
    }

    public String getBookingComPriceImport() {
        return bookingComPriceImport;
    }

    public void setBookingComPriceImport(String bookingComPriceImport) {
        this.bookingComPriceImport = bookingComPriceImport;
    }

    public String getBookingComRateType() {
        return bookingComRateType;
    }

    public void setBookingComRateType(String bookingComRateType) {
        this.bookingComRateType = bookingComRateType;
    }

    public String getBookingComInvoiceeId() {
        return bookingComInvoiceeId;
    }

    public void setBookingComInvoiceeId(String bookingComInvoiceeId) {
        this.bookingComInvoiceeId = bookingComInvoiceeId;
    }

    public String getBookingComMultiplier() {
        return bookingComMultiplier;
    }

    public void setBookingComMultiplier(String bookingComMultiplier) {
        this.bookingComMultiplier = bookingComMultiplier;
    }

    public String getcTripPropertyCode() {
        return cTripPropertyCode;
    }

    public void setcTripPropertyCode(String cTripPropertyCode) {
        this.cTripPropertyCode = cTripPropertyCode;
    }

    public String getcTripCurrency() {
        return cTripCurrency;
    }

    public void setcTripCurrency(String cTripCurrency) {
        this.cTripCurrency = cTripCurrency;
    }

    public String getDespegarComUsername() {
        return despegarComUsername;
    }

    public void setDespegarComUsername(String despegarComUsername) {
        this.despegarComUsername = despegarComUsername;
    }

    public String getDespegarComPassword() {
        return despegarComPassword;
    }

    public void setDespegarComPassword(String despegarComPassword) {
        this.despegarComPassword = despegarComPassword;
    }

    public String getDespegarComPropertyCode() {
        return despegarComPropertyCode;
    }

    public void setDespegarComPropertyCode(String despegarComPropertyCode) {
        this.despegarComPropertyCode = despegarComPropertyCode;
    }

    public String getDespegarComCurrency() {
        return despegarComCurrency;
    }

    public void setDespegarComCurrency(String despegarComCurrency) {
        this.despegarComCurrency = despegarComCurrency;
    }

    public String getExpediaComPropertyCode() {
        return expediaComPropertyCode;
    }

    public void setExpediaComPropertyCode(String expediaComPropertyCode) {
        this.expediaComPropertyCode = expediaComPropertyCode;
    }

    public String getExpediaComCurrency() {
        return expediaComCurrency;
    }

    public void setExpediaComCurrency(String expediaComCurrency) {
        this.expediaComCurrency = expediaComCurrency;
    }

    public String getIcalExportTokenSalt() {
        return icalExportTokenSalt;
    }

    public void setIcalExportTokenSalt(String icalExportTokenSalt) {
        this.icalExportTokenSalt = icalExportTokenSalt;
    }

    public String getIcalExportDescription() {
        return icalExportDescription;
    }

    public void setIcalExportDescription(String icalExportDescription) {
        this.icalExportDescription = icalExportDescription;
    }

    public String getIcalImportOption() {
        return icalImportOption;
    }

    public void setIcalImportOption(String icalImportOption) {
        this.icalImportOption = icalImportOption;
    }

    public String getGoogleAdsProduct() {
        return googleAdsProduct;
    }

    public void setGoogleAdsProduct(String googleAdsProduct) {
        this.googleAdsProduct = googleAdsProduct;
    }

    public String getOdigeoPropertyCode() {
        return odigeoPropertyCode;
    }

    public void setOdigeoPropertyCode(String odigeoPropertyCode) {
        this.odigeoPropertyCode = odigeoPropertyCode;
    }

    public String getVrboInvoicee() {
        return vrboInvoicee;
    }

    public void setVrboInvoicee(String vrboInvoicee) {
        this.vrboInvoicee = vrboInvoicee;
    }

    public String getVrboMultiplier() {
        return vrboMultiplier;
    }

    public void setVrboMultiplier(String vrboMultiplier) {
        this.vrboMultiplier = vrboMultiplier;
    }

    public ArrayList<SinglePropertyRoomResponse> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(ArrayList<SinglePropertyRoomResponse> roomTypes) {
        this.roomTypes = roomTypes;
    }
}
