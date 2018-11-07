package com.cloudtravel.cloudtravelandroid.main.item;

import lombok.Data;

@Data
public class PlaceRcmdItem {
    private String title;
    private String city;
    private int imageId;
    private String url;

    public PlaceRcmdItem(String title, String city, int imageId, String url) {
        this.title = title;
        this.city = city;
        this.imageId = imageId;
        this.url = url;
    }
}
