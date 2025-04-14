package com.example.paneli.DataObjects;

public class NewHotelType {
    private String type;
    private String description;
    private String file_name;

    public NewHotelType() {
    }

    public NewHotelType(String type,String description,String file_name) {
        this.type = type;
        this.description = description;
        this.file_name = file_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }
}
