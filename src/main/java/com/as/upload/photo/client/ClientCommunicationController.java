package com.as.upload.photo.client;

import com.as.upload.photo.Photo;
import com.as.upload.photo.Place;
import com.as.upload.photo.User;
import com.as.upload.photo.Util;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientCommunicationController {

    //for printing or not in client
    public static final boolean DEBUG = false;

    public static final String IP = "localhost";
    public static final int PORT = 9999;
    public static final String ACTIONS_PATTERN_UNDER_SECURITY = "/actions";
    public static final String AUTHENTICATE_URL = "http://" + IP + ":" + PORT + "/authenticate/";
    private static final String PLACES_URL = "http://" + IP + ":" + PORT + ACTIONS_PATTERN_UNDER_SECURITY + "/place";
    private static final String PHOTOS_URL = "http://" + IP + ":" + PORT + ACTIONS_PATTERN_UNDER_SECURITY + "/photo";
    private static ClientCommunicationController ourInstance = new ClientCommunicationController();
    private HttpClient client;
    private ObjectMapper mapper = new ObjectMapper();
    private User user;


    private ClientCommunicationController() {
    }

    public static ClientCommunicationController getInstance() {
        return ourInstance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private HttpClient getClient() {
        return client;
    }

    public void deletePhoto(Photo photo) {
//        isAuthenticated();
        HttpDelete deleteRequestWithVarParam = createDeleteRequestWithVarParam(PHOTOS_URL + "/", "" + photo.getId());
        try {
            HttpResponse execute = getClient().execute(deleteRequestWithVarParam);

            String s = readResponseContent(execute);
            printInfo(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HttpDelete createDeleteRequestWithVarParam(String url, String id) {
        client = HttpClientBuilder.create().build();
        String uri = null;
        try {

            uri = url + URLEncoder.encode(id, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Gabim ne krijimin e klientit me URL " + url, e);
        }

        HttpDelete httpDelete = new HttpDelete(uri);
        addSession(httpDelete);
        return httpDelete;
    }

    public void addPhotosToPlace(List<Photo> photos) {
        isAuthenticated();
        HttpPost postRequest = createPostRequest(PHOTOS_URL);
        JSONArray jsonArray = Util.toPhotoJson(photos);
        try {
            StringEntity se = new StringEntity(jsonArray.toString());

            se.setContentType("application/json");
            postRequest.setEntity(se);

            HttpResponse execute = client.execute(postRequest);

            String s = readResponseContent(execute);
            printInfo(s);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Photo> getPhotosByPlaceId(Place place) {

        isAuthenticated();
        place.setUser(getUser());

        HashMap<String, String> mapValues = new HashMap<String, String>();
        mapValues.put("placeID", "" + place.getId());
        HttpGet getRequest = createGetRequest(PHOTOS_URL, mapValues);
        try {
            HttpResponse execute = getClient().execute(getRequest);

            String s = readResponseContent(execute);
            printInfo(s);
            List<Photo> photos = new ArrayList<Photo>();
            List<Photo> arrayList = mapper.readValue(s, photos.getClass());
            return arrayList;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public List<Place> getAllPlaces() {
        isAuthenticated();
        HttpGet getRequest = createGetRequest(PLACES_URL, new HashMap<String, String>());
        try {

            HttpResponse execute = getClient().execute(getRequest);

            String s = readResponseContent(execute);
            printInfo(s);
            List<Place> places = new ArrayList<Place>();
            List<Place> list = mapper.readValue(s, places.getClass());
            return list;

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Place updatePlace(Place place) {
        isAuthenticated();
        place.setUser(getUser());
        HttpPut putRequest = createPutRequest(PLACES_URL);
        putRequest.setEntity(prepareEntityForHttp(place));
        try {
            HttpResponse execute = getClient().execute(putRequest);

            String s = readResponseContent(execute);
            printInfo(s);
            Place place1 = mapper.readValue(s, Place.class);
            return place1;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void isAuthenticated() {
        if (getUser() == null || getUser().getSessionId() == null || getUser().getSessionId().isEmpty()) {
            throw new RuntimeException("Not authenticated");
        }
    }

    public void closeSession() {
        if (getUser() != null) {
            getUser().setSessionId(null);

        }
    }

    public Place addPlace(Place place) {
        isAuthenticated();
        place.setUser(getUser());
        HttpPost postRequest = createPostRequest(PLACES_URL);
        StringEntity stringEntity = prepareEntityForHttp(place);

        postRequest.setEntity(stringEntity);
        try {
            HttpResponse execute = getClient().execute(postRequest);
            String s = readResponseContent(execute);
            printInfo(s);
            Place place1 = mapper.readValue(s, Place.class);
            return place1;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    private void printInfo(String s) {
        if (DEBUG) {
            System.out.println("Answer from server---->" + s);
        }
    }
    private StringEntity prepareEntityForHttp(Place place) {
        JSONObject jsonPlace = Util.toPlaceJson(place);
        JSONObject jsonUser = Util.toUserJson(getUser());
        JSONArray jsonArray = Util.toPhotoJson(place.getPhotos());

        return Util.prepareAllJSon(jsonPlace, jsonUser, jsonArray);
    }

    public User authenticate(String mobileNo) {
        HttpGet getRequest = createGetRequestWithVarParam(AUTHENTICATE_URL, mobileNo);

        try {
            HttpResponse execute = getClient().execute(getRequest);


            String s = readResponseContent(execute);
            printInfo(s);


            User user = null;
            try {
                user = mapper.readValue(s, User.class);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Gabim ne krijimin e objektit nga json", e);
            }
            setUser(user);
            return getUser();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private String readResponseContent(HttpResponse response) {
        String line = "";
        try {
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return "";
            }
            BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));

            String content = "";
            while ((content = rd.readLine()) != null) {

                line = line + content;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Gabim ne leximin e pergjigjes nga serveri", e);
        }
        return line;
    }

    //for insert
    private HttpPost createPostRequest(String url) {
        client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        addSession(post);
        return post;
    }

    //for update
    private HttpPut createPutRequest(String url) {
        client = HttpClientBuilder.create().build();
        HttpPut post = new HttpPut(url);
        addSession(post);
        return post;
    }

    private HttpGet createGetRequest(String url, HashMap<String, String> mapValues) {
        client = HttpClientBuilder.create().build();
        url = url + "?";
        for (String s : mapValues.keySet()) {
            try {
                url = url + s + "=" + URLEncoder.encode(mapValues.get(s), "utf-8") + "&&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        HttpGet get = new HttpGet(url);
        addSession(get);
        return get;
    }

    private HttpGet createGetRequestWithVarParam(String url, String paramValue) {
        client = HttpClientBuilder.create().build();
        String uri = null;
        try {

            uri = url + URLEncoder.encode(paramValue, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("Gabim ne krijimin e klientit me URL " + url, e);
        }
        HttpGet get = new HttpGet(uri);

        addSession(get);

        return get;
    }

    private void addSession(HttpRequestBase base) {

        if (getUser() != null)
            base.addHeader(new BasicHeader("Cookie", "JSESSIONID=" + getUser().getSessionId()));
    }
}
