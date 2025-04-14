package com.example.paneli.DataObjects.GetUniqueProperty;

public class BodyDto {

    private Long propertyManagerId;
    private String propKey;

    public BodyDto() {
    }

    public BodyDto(Long propertyManagerId, String propKey) {
        this.propertyManagerId = propertyManagerId;
        this.propKey = propKey;
    }

    public Long getPropertyManagerId() {
        return propertyManagerId;
    }

    public void setPropertyManagerId(Long propertyManagerId) {
        this.propertyManagerId = propertyManagerId;
    }

    public String getPropKey() {
        return propKey;
    }

    public void setPropKey(String propKey) {
        this.propKey = propKey;
    }
}
