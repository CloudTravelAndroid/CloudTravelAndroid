package com.cloudtravel.cloudtravelandroid.main.dto;

import lombok.Data;

@Data
public class SimpleUniversityDTO {

    private Integer id;
    private String name;
    private String city;

    public SimpleUniversityDTO(Integer id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }
}
