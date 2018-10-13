package com.cloudtravel.cloudtravelandroid.main.item;

/**
 * Created by Brandon on 2018/3/11.
 */

public class PlaceRcmdItem {
    private String title;
    private String city;
    private int imageId;
    private String url;

    public PlaceRcmdItem(String title, String city, int imageId) {
        this.title = title;
        this.city = city;
        this.imageId = imageId;
    }

    public PlaceRcmdItem(String title, String city, int imageId, String url) {
        this.title = title;
        this.city = city;
        this.imageId = imageId;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public int getImageId() {
        return imageId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
