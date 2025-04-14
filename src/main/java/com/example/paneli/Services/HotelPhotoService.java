package com.example.paneli.Services;
import com.example.paneli.Models.HotelPhoto;
import com.example.paneli.Repositories.HotelPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class HotelPhotoService {

    @Autowired
    private HotelPhotoRepository hotelPhotoRepository;

    public List<HotelPhoto> getAllPhotos(){
        return hotelPhotoRepository.findAll();
    }
}
