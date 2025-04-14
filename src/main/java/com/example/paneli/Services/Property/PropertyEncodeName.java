package com.example.paneli.Services.Property;

import com.example.paneli.Models.CityPhoto;
import com.example.paneli.Models.HotelPhoto;
import com.example.paneli.Models.Property;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class PropertyEncodeName {

    public String encodeNameForServerPath(Property property){
        return "property_id_" + getPropertyLongId(property);
    }

    public String getPropertyLongId(Property property){
        Long propId = property.getId()+ 2654435l;
        return propId.toString();
    }

    public String encodeImageName(HotelPhoto hotelPhoto, MultipartFile multipartFile){
        Long photoId = hotelPhoto.getId();
        String photoName = multipartFile.getOriginalFilename();
        return "img_id_" + photoId.toString() + photoName.substring(photoName.indexOf("."));
    }

    public String encodeImageNames(CityPhoto cityPhoto, MultipartFile multipartFile){
        Long photoId = cityPhoto.getId();
        String photoName = multipartFile.getOriginalFilename();

        System.out.println(photoName);
        String finalName = photoId.toString() + photoName.substring(photoName.indexOf("."));
        finalName.trim();
        System.out.println("shhdshdshd"+finalName);
        return finalName;
    }

    public String encodeOneImage(HotelPhoto hotelPhoto){
        Long photoId = hotelPhoto.getId();
        String finalName = photoId.toString() + hotelPhoto.getFile_name().substring(hotelPhoto.getFile_name().indexOf("."));
        finalName.trim();
        System.out.println("shhdshdshd"+finalName);
        return finalName;
    }

}
