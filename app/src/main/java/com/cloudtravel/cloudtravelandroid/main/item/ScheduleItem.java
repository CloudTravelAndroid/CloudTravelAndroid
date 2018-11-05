package com.cloudtravel.cloudtravelandroid.main.item;

import android.os.Parcel;
import android.os.Parcelable;

public class ScheduleItem implements Parcelable {
    public static final Parcelable.Creator<ScheduleItem> CREATOR = new Creator<ScheduleItem>() {
        @Override
        public ScheduleItem createFromParcel(Parcel source) {
            return new ScheduleItem(source);
        }

        @Override
        public ScheduleItem[] newArray(int size) {
            return new ScheduleItem[size];
        }
    };
    private String time;
    private String placeName;
    private double latitude;
    private double longitude;
    private int typeIconId;
    private String address;
    private int scheduleId;
    private String memo;

    private ScheduleItem(Parcel in) {
        time = in.readString();
        placeName = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        typeIconId = in.readInt();
        address = in.readString();
        scheduleId = in.readInt();
        memo = in.readString();
    }

    public ScheduleItem() {

    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public int getTypeIconId() {
        return typeIconId;
    }

    public void setTypeIconId(int typeIconId) {
        this.typeIconId = typeIconId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(placeName);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(typeIconId);
        dest.writeString(address);
        dest.writeInt(scheduleId);
        dest.writeString(memo);
    }
}
