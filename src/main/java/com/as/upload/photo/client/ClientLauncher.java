package com.as.upload.photo.client;

import com.as.upload.photo.Photo;
import com.as.upload.photo.Place;
import com.as.upload.photo.Util;

import java.util.ArrayList;
import java.util.List;

public class ClientLauncher {

    public static void main(String[] args) {

        ClientCommunicationController instance = ClientCommunicationController.getInstance();
        instance.authenticate("1234");
        Photo photo = new Photo();
        photo.setId(91);
        instance.deletePhoto(photo);

        Place place = new Place();
        place.setAddress("tyteyryrtytry");
        place.setQyteti("Vlore");
        place.setDescription("pershkrimmmmm");
        place.setNote("idaufhsdafjkhasjdf");
        place.setLatitude(56756);
        place.setLongitude(32);
        place.setZoneId(34234);

        byte[] bytes = Util.fromPathFileToByte("c:\\UploadPhoto\\logout.png");
        byte[] bytes2 = Util.fromPathFileToByte("c:\\UploadPhoto\\macback.png");

        Photo photo1 = new Photo(bytes);
        place.getPhotos().add(photo1);
        Photo photo2 = new Photo(bytes2);
        place.getPhotos().add(photo2);


        place = instance.addPlace(place);
        System.out.println("place = " + place);

        place.setDescription("11111111111111111111");
        place = instance.updatePlace(place);
        List<Photo> photosByPlaceId = instance.getPhotosByPlaceId(place);
        System.out.println("photosByPlaceId.size() = " + photosByPlaceId.size());

        List<Place> allPlaces = instance.getAllPlaces();
        System.out.println("allPlaces = " + allPlaces);


        System.out.println("place = " + 41);
        photo1.setPlaceId(41);
        photo2.setPlaceId(41);
        List<Photo> photos = new ArrayList<Photo>();
        photos.add(photo1);
        photos.add(photo1);
        photos.add(photo1);
        photos.add(photo2);
        photos.add(photo2);



        instance.addPhotosToPlace(photos);


    }
}
