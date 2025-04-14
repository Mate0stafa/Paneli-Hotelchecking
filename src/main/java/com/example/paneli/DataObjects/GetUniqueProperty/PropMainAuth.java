package com.example.paneli.DataObjects.GetUniqueProperty;

public class PropMainAuth {

    private PropertyAuth authentication;
    private boolean includeRooms;
    private boolean includeRoomUnits;
    private boolean includeAccountAccess;

    public PropMainAuth() {
    }

    public PropMainAuth(PropertyAuth authentication, boolean includeRooms, boolean includeRoomUnits, boolean includeAccountAccess) {
        this.authentication = authentication;
        this.includeRooms = includeRooms;
        this.includeRoomUnits = includeRoomUnits;
        this.includeAccountAccess = includeAccountAccess;
    }

    public PropertyAuth getAuthentication() {
        return authentication;
    }

    public void setAuthentication(PropertyAuth authentication) {
        this.authentication = authentication;
    }

    public boolean isIncludeRooms() {
        return includeRooms;
    }

    public void setIncludeRooms(boolean includeRooms) {
        this.includeRooms = includeRooms;
    }

    public boolean isIncludeRoomUnits() {
        return includeRoomUnits;
    }

    public void setIncludeRoomUnits(boolean includeRoomUnits) {
        this.includeRoomUnits = includeRoomUnits;
    }

    public boolean isIncludeAccountAccess() {
        return includeAccountAccess;
    }

    public void setIncludeAccountAccess(boolean includeAccountAccess) {
        this.includeAccountAccess = includeAccountAccess;
    }
}
