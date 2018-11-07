package com.cloudtravel.cloudtravelandroid.main.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MomentsDTO {

    private Integer id;
    private String username;
    private String university;
    private Date time;
    private String content;
    private List<String> imageUrls;
}
