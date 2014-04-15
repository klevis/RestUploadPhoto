package com.as.upload.photo.server;

import com.as.upload.photo.Photo;
import com.as.upload.photo.Place;
import com.as.upload.photo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/")
public class InfoController {

    @Autowired
    InfoService infoService;

    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "hello";
    }

    @RequestMapping(value = "authenticate/{mobileNo}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    User authenticateUser(@PathVariable("mobileNo") String mobileNo, HttpServletRequest request) {


        User user = infoService.authenticateUser(mobileNo);

        String id = request.getSession().getId();
        user.setSessionId(id);

        return user;

    }

    @RequestMapping(value = "actions/place", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<Place> getAllPlaces() {

        return infoService.getAllPlaces();
    }

    @RequestMapping(value = "actions/place", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")

    public
    @ResponseBody
    Place addPlace(@RequestBody Place place) {


        Place place1 = infoService.addPlace(place);
        place1.getPhotos().clear();
        return place1;
    }

    @RequestMapping(value = "actions/place", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")

    public
    @ResponseBody
    Place updatePlace(@RequestBody Place place) {


        System.out.println("place = " + place);
        Place place1 = infoService.updatePlace(place);
        place1.getPhotos().clear();
        return place1;

    }

    @RequestMapping(value = "actions/photo", method = RequestMethod.GET,  produces = "application/json")
    public @ResponseBody List<Photo> getPhotosByPlaceId(@RequestParam int placeID) {

      return  infoService.getPhotosByPlaceId(placeID);
    }

    @RequestMapping(value = "actions/photo", method = RequestMethod.POST,  produces = "application/json",consumes = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPhotosToPlace(@RequestBody List<Photo> photos) {

          infoService.addPhotosToPlace(photos);
    }

    @RequestMapping(value = "actions/photo/{photoId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhotoById( @PathVariable("photoId") String photoId) {

        infoService.deletePhotoById(photoId);
    }

}