package com.as.upload.photo.server;

import com.as.upload.photo.Photo;
import com.as.upload.photo.Place;
import com.as.upload.photo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class InfoDao {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public User authenticateUser(String mobileNo) {


        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("mobileNo", mobileNo);
        User user = jdbcTemplate.queryForObject("Select * from user where mobileNo=:mobileNo", paramMap, new ParameterizedRowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user1 = new User();

                int id = rs.getInt("id");
                String email = rs.getString("email");
                String mobileNo1 = rs.getString("mobileNo");
                String name = rs.getString("name");
                String surname = rs.getString("surname");

                user1.setEmail(email);
                user1.setMobileNo(mobileNo1);
                user1.setId(id);
                user1.setName(name);
                user1.setSurname(surname);
                System.out.println("user1 = " + user1);
                return user1;


            }
        });

        return user;
    }

    public void register() {

        jdbcTemplate.query("select * from user", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                System.out.println("rs = " + rs);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public List<Place> getAllPlaces() {

        return jdbcTemplate.query("Select * from place", new HashMap<String, Object>(), new RowMapper<Place>() {
            @Override
            public Place mapRow(ResultSet rs, int rowNum) throws SQLException {

                Place place = new Place();
                int id = rs.getInt("id");
                String description = rs.getString("description");
                String note = rs.getString("note");
                String address = rs.getString("address");
                String qyteti = rs.getString("qyteti");
                double zoneID = rs.getDouble("zoneID");
                double longitude = rs.getDouble("longitude");
                double latitude = rs.getDouble("latitude");
                int userId = rs.getInt("userId");
                place.setId(id);
                place.setDescription(description);
                place.setNote(note);
                place.setAddress(address);
                place.setQyteti(qyteti);
                place.setZoneId(zoneID);
                place.setLongitude(longitude);
                place.setLatitude(latitude);
                place.setUserID(userId);

                return place;
            }
        });
    }

    public Place addPlace(Place place) {


        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        place.setUserID(place.getUser().getId());
        jdbcTemplate.update("INSERT INTO place(address,qyteti,description,note,latitude,longitude,userID,zoneId)" +
                " VALUES(:address,:qyteti,:description,:note,:latitude,:longitude,:userID,:zoneId)", new BeanPropertySqlParameterSource(place), generatedKeyHolder);
        Number key = generatedKeyHolder.getKey();
        place.setId(key.intValue());
        addPhotosToPlace(place.getPhotos(), place.getId());


        return place;
    }

    public Place updatePlace(Place place) {
        jdbcTemplate.update("UPDATE  place SET note=:note,description=:description,latitude=:latitude,longitude=:longitude,address=:address,qyteti=:qyteti,zoneID=:zoneId where id=:id", new BeanPropertySqlParameterSource(place));
        return place;
    }

    public List<Photo> getPhotosByPlaceId(int placeID) {

        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("placeID", placeID);
        List<Photo> photos = jdbcTemplate.query("Select * from photo where placeID=:placeID", paramMap, new RowMapper<Photo>() {
            @Override
            public Photo mapRow(ResultSet rs, int rowNum) throws SQLException {
                Blob photo1 = rs.getBlob("photo");
                Photo photo = new Photo(rs.getInt("id"), photo1.getBytes(1, (int) photo1.length()), rs.getInt("placeID"));
                return photo;
            }
        });


        return photos;
    }

    public void addPhotosToPlace(List<Photo> photos, int placeID) {

        List<BeanPropertySqlParameterSource> propertySqlParameterSources = new ArrayList<BeanPropertySqlParameterSource>();
        for (Photo photo : photos) {
            photo.setPlaceId(placeID);

            propertySqlParameterSources.add(new BeanPropertySqlParameterSource(photo));


        }
        BeanPropertySqlParameterSource[] b = new BeanPropertySqlParameterSource[propertySqlParameterSources.size()];
        jdbcTemplate.batchUpdate("insert into photo(photo,placeID) values(:photoBytes,:placeId) ", propertySqlParameterSources.toArray(b));
    }

    public void deletePhotoById(String photoId) {

        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("id", photoId);
        jdbcTemplate.update("delete  from photo where id=:id", paramMap);

    }
}
