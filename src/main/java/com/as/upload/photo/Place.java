package com.as.upload.photo;

import java.util.ArrayList;
import java.util.List;

public class Place {

    private int id;
    private double longitude;
    private double latitude;
    private String description;
    private String note;
    private String address;
    private String qyteti;
    private double zoneId;
    private List<Photo> photos=new ArrayList<Photo>();
    private User user;
    private  int userID;

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", description='" + description + '\'' +
                ", note='" + note + '\'' +
                ", address='" + address + '\'' +
                ", qyteti='" + qyteti + '\'' +
                ", zoneId=" + zoneId +
                ", photos=" + photos +
                ", user=" + user +
                ", userID=" + userID +
                '}';
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQyteti() {
        return qyteti;
    }

    public void setQyteti(String qyteti) {
        this.qyteti = qyteti;
    }

    public double getZoneId() {
        return zoneId;
    }

    public void setZoneId(double zoneId) {
        this.zoneId = zoneId;
    }
}
