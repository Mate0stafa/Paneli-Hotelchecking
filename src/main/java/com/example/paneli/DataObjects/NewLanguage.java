package com.example.paneli.DataObjects;

public class NewLanguage {


    private Long version;
    private String code;
    private String name;


    public NewLanguage() {
    }

    public NewLanguage(Long version, String code, String name) {
        this.version = version;
        this.code = code;
        this.name = name;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
