package com.as.upload.photo.server;

import com.as.upload.photo.Photo;
import com.as.upload.photo.Place;
import com.as.upload.photo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Service
public class InfoService {
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private InfoDao infoDao;


    public User authenticateUser(final String mobileNO) {

        return transactionTemplate.execute(new TransactionCallback<User>() {
            @Override
            public User doInTransaction(TransactionStatus transactionStatus) {

                try {
                    return infoDao.authenticateUser(mobileNO);
                } catch (RuntimeException e) {
                    e.printStackTrace();

                    throw e;
                }
            }
        });
    }

    public List<Place> getAllPlaces() {
        return transactionTemplate.execute(new TransactionCallback<List<Place>>() {
            @Override
            public List<Place> doInTransaction(TransactionStatus transactionStatus) {
                return infoDao.getAllPlaces();
            }
        });
    }

    public Place addPlace(final Place place) {
        return transactionTemplate.execute(new TransactionCallback<Place>() {
            @Override
            public Place doInTransaction(TransactionStatus transactionStatus) {


                return infoDao.addPlace(place);

            }
        });
    }

    public Place updatePlace(final Place place) {
        return transactionTemplate.execute(new TransactionCallback<Place>() {
            @Override
            public Place doInTransaction(TransactionStatus transactionStatus) {


                return infoDao.updatePlace(place);

            }
        });
    }

    public List<Photo> getPhotosByPlaceId(final int placeID) {
        return transactionTemplate.execute(new TransactionCallback<List<Photo>>() {
            @Override
            public List<Photo> doInTransaction(TransactionStatus transactionStatus) {
                return infoDao.getPhotosByPlaceId(placeID);
            }
        });
    }

    public void addPhotosToPlace(final List<Photo> photos) {

        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {

                if (photos != null && photos.isEmpty() == false) {
                    int placeId = photos.get(0).getPlaceId();
                    System.out.println("placeId SSSSSSSS= " + placeId);
                    infoDao.addPhotosToPlace(photos, placeId);
                }
                return null;
            }
        });
    }

    public void deletePhotoById(final String photoId) {
        transactionTemplate.execute(new TransactionCallback<Void>() {
            @Override
            public Void doInTransaction(TransactionStatus transactionStatus) {

                infoDao.deletePhotoById(photoId);
                return null;
            }
        });
    }
}
