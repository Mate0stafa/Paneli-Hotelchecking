package com.example.paneli.DataObjects.Auth;

public class UserDetailsObj {
    private Boolean status;
    private Long id;
    private Long userAddId;

    public UserDetailsObj() {
    }


    public UserDetailsObj(Boolean status, Long id, Long propertyAddId) {
        this.status = status;
        this.id = id;
        this.userAddId = propertyAddId;
    }

    public Long getChangeRequestId() {
        return userAddId;
    }

    public void setChangeRequestId(Long propertyAddId) {
        this.userAddId = propertyAddId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
