package com.cloudtravel.cloudtravelandroid.main.dto;

import lombok.Data;

@Data
public class ProvinceDTO {

    private Integer id;
    private String name;

    public ProvinceDTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
