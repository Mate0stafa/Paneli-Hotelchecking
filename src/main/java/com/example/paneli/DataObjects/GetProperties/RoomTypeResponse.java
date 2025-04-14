package com.example.paneli.DataObjects.GetProperties;

public class RoomTypeResponse {

    private String name;
    private String qty;
    private String roomId;
    private String minPrice;
    private String maxPeople;
    private String maxAdult;
    private String maxChildren;
    private String unitAllocationPerGuest;
    private String unitNames;

    public RoomTypeResponse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(String maxPeople) {
        this.maxPeople = maxPeople;
    }

    public String getMaxAdult() {
        return maxAdult;
    }

    public void setMaxAdult(String maxAdult) {
        this.maxAdult = maxAdult;
    }

    public String getMaxChildren() {
        return maxChildren;
    }

    public void setMaxChildren(String maxChildren) {
        this.maxChildren = maxChildren;
    }

    public String getUnitAllocationPerGuest() {
        return unitAllocationPerGuest;
    }

    public void setUnitAllocationPerGuest(String unitAllocationPerGuest) {
        this.unitAllocationPerGuest = unitAllocationPerGuest;
    }

    public String getUnitNames() {
        return unitNames;
    }

    public void setUnitNames(String unitNames) {
        this.unitNames = unitNames;
    }

    public RoomTypeResponse(String name, String qty, String roomId, String minPrice, String maxPeople, String maxAdult, String maxChildren, String unitAllocationPerGuest, String unitNames) {
        this.name = name;
        this.qty = qty;
        this.roomId = roomId;
        this.minPrice = minPrice;
        this.maxPeople = maxPeople;
        this.maxAdult = maxAdult;
        this.maxChildren = maxChildren;
        this.unitAllocationPerGuest = unitAllocationPerGuest;
        this.unitNames = unitNames;
    }
}
