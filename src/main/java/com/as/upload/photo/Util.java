package com.as.upload.photo;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

public class Util {
    public static JSONObject toPlaceJson(Place place) {
        JSONObject jsonPlace = new JSONObject();
        jsonPlace.put("id", place.getId());
        jsonPlace.put("longitude", place.getLongitude());
        jsonPlace.put("latitude", place.getLatitude());
        jsonPlace.put("description", place.getDescription());
        jsonPlace.put("note", place.getNote());
        jsonPlace.put("address", place.getAddress());
        jsonPlace.put("qyteti", place.getQyteti());
        jsonPlace.put("zoneId", place.getZoneId());
        return jsonPlace;
    }

    public static StringEntity prepareAllJSon(JSONObject jsonObjectPlace, JSONObject jsonObjectUser, JSONArray jsonArrayPhotos) {

        jsonObjectPlace.put("photos", jsonArrayPhotos);
        jsonObjectPlace.put("user", jsonObjectUser);

        StringEntity se = null;
        try {
            se = new StringEntity(jsonObjectPlace.toString());
            se.setContentType("application/json");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return se;
    }

    public static JSONArray toPhotoJson(List<Photo> photos) {
        JSONArray jsonArray = new JSONArray();

        for (Photo photo : photos) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", -1);
            jsonObject.put("placeId", photo.getPlaceId());
            jsonObject.put("photoBytes", Util.fromByteToEncodedString(photo.getPhotoBytes()));
            jsonArray.put(jsonObject);

        }
        return jsonArray;
    }

    public static byte[] fromPathFileToByte(String fileName) {

        try {
            File file = new File(fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] b = new byte[(int) file.length()];
            fileInputStream.read(b);
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String fromByteToEncodedString(byte[] bFile) {

        byte[] bytes = Base64.encodeBase64(bFile);
        String s = new String(bytes);

        return s;
    }

    public static String fromPathFileToEncodedString(String fileName) {
        String s = fromByteToEncodedString(fromPathFileToByte(fileName));

        return s;
    }

    public static void toFile(byte[] bFile) {


        try {
            //convert file into array of bytes


            //convert array of bytes into file
            FileOutputStream fileOuputStream =
                    new FileOutputStream("C:\\testing2.png");
            fileOuputStream.write(bFile);
            fileOuputStream.close();

            System.out.println("Done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject toUserJson(User user) {
        JSONObject jsonUser = new JSONObject();
        jsonUser.put("id", user.getId());
        jsonUser.put("name", user.getName());
        jsonUser.put("surname", user.getSurname());
        jsonUser.put("email", user.getEmail());
        jsonUser.put("androidId", user.getAndroidId());
        jsonUser.put("mobileNo", user.getMobileNo());

        return jsonUser;
    }

}
