package com.example.paneli.DataObjects;

import java.util.List;

public class PhotoTagDto {

    private Long id;
    private List<Long> tagIds;

    public PhotoTagDto() {
    }

    public PhotoTagDto(Long id, List<Long> tagIds) {
        this.id = id;
        this.tagIds = tagIds;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}
