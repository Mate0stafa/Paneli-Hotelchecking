package com.example.paneli.DataObjects;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LanguageDTO {

    private Long id;
    private String name;
    private String englishname;

    public LanguageDTO(Long id, String name, String englishname) {
        this.id = id;
        this.name = name;
        this.englishname = englishname;
    }
}
