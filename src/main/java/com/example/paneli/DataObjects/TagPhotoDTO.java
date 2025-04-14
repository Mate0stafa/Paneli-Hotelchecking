package com.example.paneli.DataObjects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagPhotoDTO {
    private Long id;
    private String name;

    public TagPhotoDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
