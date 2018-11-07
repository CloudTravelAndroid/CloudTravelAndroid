package com.cloudtravel.cloudtravelandroid.main.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ScheduleDTO {

    private Integer id;
    private String locationName;
    private String locationAddress;
    private Date time;
    private Double latitude;
    private Double longitude;
    private String memo;
}
