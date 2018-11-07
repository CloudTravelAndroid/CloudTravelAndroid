package com.cloudtravel.cloudtravelandroid.main.item;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MomentsItem {

    private int momentId;
    private int username;
    private Date time;
    private String content;
    private String university;
    private List<String> imageUrlList;
}
