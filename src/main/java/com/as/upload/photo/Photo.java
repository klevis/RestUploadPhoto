package com.as.upload.photo;

public class Photo {
    private int id;
    private byte[] photoBytes;
    private int placeId;

    public Photo() {
    }

    public Photo(int id, byte[] photoBytes, int placeId) {
        this.id = id;
        this.photoBytes = photoBytes;
        this.placeId = placeId;
    }

    public Photo(byte[] bytes) {
        setPhotoBytes(bytes);

    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPhotoBytes() {
        return photoBytes;
    }

    public void setPhotoBytes(byte[] photoBytes) {
        this.photoBytes = photoBytes;
    }
}
