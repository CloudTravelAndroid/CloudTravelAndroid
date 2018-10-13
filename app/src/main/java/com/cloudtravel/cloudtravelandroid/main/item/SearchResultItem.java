package com.cloudtravel.cloudtravelandroid.main.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class SearchResultItem implements Serializable{

    private String name;
    private String address;
    private String uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
