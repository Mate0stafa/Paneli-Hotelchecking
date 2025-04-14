package com.example.paneli.Controllers;

import com.example.paneli.DataObjects.CityDto;
import com.example.paneli.Models.*;
import com.example.paneli.Models.Contract.Agreement;
import com.example.paneli.Models.client.UserClient;
import com.example.paneli.Repositories.*;
import com.example.paneli.DataObjects.NewCity;
import com.example.paneli.Repositories.clientRepositories.UserClientRepository;
import com.example.paneli.Services.CountyService;
import com.example.paneli.Services.DeleteCityService;
import com.example.paneli.Services.Property.PropertyEncodeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
public class CityController {
    @Autowired
    CountyRepository countyRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    CityRepository cityRepository;
    @Autowired
    AddressRepostitory addressRepository;
    @Autowired
    private CountyService countyService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deleteCityImage")
    @ResponseBody
    public void deleteCityimage(@RequestParam(value = "photoId") Long photoId ,HttpServletRequest request) throws IOException {
        if (request.isUserInRole("ROLE_ADMIN")){
            cityPhotoRepository.deleteById(photoId);

            Files.deleteIfExists(Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/qytete/"+cityRepository.findById(photoId).get().getFull_name()));
        }
    }




    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/uploadCityImages")
    public ModelAndView uploadCityImages(@RequestParam(value = "id") Long id, ModelAndView modelAndView, HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {

            modelAndView.addObject("city", cityRepository.findById(id).get());
            modelAndView.setViewName("ROLE_ADMIN/city/editCity");
        }
        return modelAndView;
    }

    @Autowired
    CityPhotoRepository cityPhotoRepository;
    @Autowired
    PropertyEncodeName propertyEncodeName;



    @PostMapping(value = "/uploadCityImages")
    @ResponseBody
    public void uploadCityImages(@RequestParam(value = "id") Long id, @RequestPart List<MultipartFile> file) throws IOException {

        City city = cityRepository.findById(id).get();

        Path uploadPath = Paths.get("/home/allbookersusr/home/BookersDesk/data/uploads/qytete/");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        for (int i =0 ;i<file.size();i++){
            try (InputStream inputStream = file.get(i).getInputStream()) {


                CityPhoto cityPhoto = new CityPhoto(0,file.get(i).getOriginalFilename(),cityRepository.findById(id).get());
                cityPhotoRepository.save(cityPhoto);
//                HotelPhoto hotelPhoto = new HotelPhoto(propertyRepository.findById(id).get(), 0, file.get(i).getOriginalFilename());
//                hotelPhotoRepository.save(hotelPhoto);
                String filename = propertyEncodeName.encodeImageNames(cityPhoto, file.get(i));

                Files.copy(file.get(i).getInputStream(), uploadPath.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);

                Path filePath = uploadPath.resolve(filename);


                cityPhoto.setFile_name(filename);
                cityPhotoRepository.save(cityPhoto);
//                hotelPhoto.setFile_name(filename);
//                hotelPhotoRepository.save(hotelPhoto);


                System.out.println(file.get(i).getOriginalFilename());

            } catch (IOException ioe) {
                throw new IOException("Could not save image file: " + file.get(i).getOriginalFilename(), ioe);
            }
        }


    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editCity")
    public ModelAndView editCityGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            City city = cityRepository.findById(id).get();
            modelAndView.addObject("city", city);
            Country country = city.getCounty().getCountry();
            modelAndView.addObject("counties", countyRepository.findAllByCountry(country));
            modelAndView.setViewName("ROLE_ADMIN/city/editCity");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editCity")
    public String editCity(@RequestParam(value = "id") Long id,
                           @RequestParam(name="cityDescription", required = false) String description,
                           @RequestParam(name="cityName", required = false) String newName,
                           @RequestParam(name = "county", required = false) Long countyId,
                           @RequestParam(name = "cityTax", required = false)Float citytax,
                           HttpServletRequest request) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            City dbCity = cityRepository.findById(id).orElseThrow(() -> new RuntimeException("City not found"));
            County dbCounty = countyRepository.findById(dbCity.getCounty().getId()).orElseThrow(() -> new RuntimeException("County not found"));
            //mund te ndryshohet secila fushe pavaresisht nese fushat e tjera kane vlera apo jo
            dbCity.setFull_name(dbCity.getFull_name());
            dbCity.setCityDescription(description);
            dbCity.setCounty(dbCounty);
            dbCity.setCityTax(citytax);
            cityRepository.save(dbCity);
        }

        return "redirect:/cityList";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/rankCity")
    public ModelAndView rankCity(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, City city){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/city/rankCity");
            City dbCity = cityRepository.findById(id).get();
            dbCity.setShow_city(city.getShow_city());
            cityRepository.save(dbCity);

            List<Boolean> existStatus = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                List<City> citiesWithGivenShowCityValue = cityRepository.findByShowCityValue(i);
                existStatus.add(!citiesWithGivenShowCityValue.isEmpty());
            }
            modelAndView.addObject("existStatus", existStatus);

            modelAndView.addObject("city", dbCity);
            modelAndView.setViewName("redirect:/cityList");

        }
        return modelAndView;
    }

    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    UserClientRepository userClientRepository;


    @Autowired
    EntityManager entityManager;


    @Autowired
    private DeleteCityService deleteCityService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deleteCity")
    public ResponseEntity<Map<String, Object>> deleteCity(@RequestParam(value = "id") Long id) {
        Map<String, Object> response = deleteCityService.deleteCitySync(id);
        return ResponseEntity.ok(response);
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/promoteCity")
    public ModelAndView rankCity(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("City not found"));
            int promoteCities = cityRepository.countByPromote(true);
            boolean isPromoted = city.getPromote() != null && city.getPromote();
            if ((promoteCities < 10)&&(!isPromoted)) {
                city.setPromote(true);
                cityRepository.save(city);
            } else if (isPromoted) {
                city.setPromote(false);
                city.setShow_city(0);
                cityRepository.save(city);
            } else {
                System.out.println("Promote error: Maximum limit reached");
            }
            modelAndView.setViewName("redirect:/cityList");
        }
        return modelAndView;
    }

    @Autowired
    private MessageSource messageSource;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/cityList")
    public ModelAndView shihQyetet(HttpServletRequest request, ModelAndView modelAndView,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String search,
                                   @RequestParam(required = false) Long id ,
                                   Locale locale) {
        if (request.isUserInRole("ROLE_ADMIN")) {
            List<City> cityPage;

            if (StringUtils.hasText(search)) {
                cityPage = cityRepository.findByFull_nameContainingIgnoreCase(search);
            } else {
                long a = System.currentTimeMillis();
                cityPage = cityRepository.findAll();
                System.out.println(System.currentTimeMillis() - a + " MS");
            }

            int cityPromotionCount = cityRepository.countByPromote(true);
            if (cityPromotionCount >= 10) {
                String message = messageSource.getMessage("msg.city.promotion.limit", null, locale);
                modelAndView.addObject("msg", message);
            }
            modelAndView.addObject("maxPromotions", cityPromotionCount);
            modelAndView.addObject("cities", cityPage);
            modelAndView.addObject("page", cityPage);
            modelAndView.addObject("currentPage", page);
            modelAndView.addObject("search", search);
            modelAndView.addObject("id", id);
            modelAndView.setViewName("ROLE_ADMIN/city/cityList");
        }
        return modelAndView;
    }






    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/rankCity")
    public ModelAndView rankCityget(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.addObject("city", cityRepository.findById(id).get());

            List<Boolean> existStatus = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                List<City> citiesWithGivenShowCityValue = cityRepository.findByShowCityValue(i);
                existStatus.add(!citiesWithGivenShowCityValue.isEmpty());
            }
            modelAndView.addObject("existStatus", existStatus);

            modelAndView.setViewName("ROLE_ADMIN/city/rankCity");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/showCity")
    public ModelAndView showCity(
            @RequestParam(value = "cityId") Long cityId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "search", required = false) String search,
            HttpServletRequest request,
            ModelAndView modelAndView) {

        if (request.isUserInRole("ROLE_ADMIN")) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Address> addresses;

            if (search != null && !search.isEmpty()) {
                addresses = addressRepository.findByCityIdAndPropertyNameContaining(cityId, search, pageable);
            } else {
                addresses = addressRepository.findByCityId(cityId, pageable);
            }
            int totalPages = addresses.getTotalPages();
            int currentPage = addresses.getNumber() + 1;
            int startPage = Math.max(1, currentPage - 2);
            int endPage = Math.min(startPage + 4, totalPages);
            modelAndView.addObject("addresses", addresses.getContent());
            modelAndView.addObject("page", addresses);
            modelAndView.addObject("search", search);
            modelAndView.addObject("startPage", startPage);
            modelAndView.addObject("endPage", endPage);
            modelAndView.addObject("city", cityRepository.findById(cityId).get());
            modelAndView.setViewName("ROLE_ADMIN/city/showCity");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addCity")
    public ModelAndView addCityGet(HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            NewCity newCity = new NewCity();
            modelAndView.addObject("counties", countyRepository.findAll());
            modelAndView.addObject("countries", countryRepository.findAll());
            //System.out.println("111111111 " + getCountiesByCountry("Albania"));
            modelAndView.addObject("newCity", newCity);
            modelAndView.setViewName("ROLE_ADMIN/city/addCity");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/cityListPage")
    @ResponseBody
    public List<CityDto> cityListPage(@RequestParam(value = "search",required = false) String search) {
        System.out.println("ye");

        List<CityDto> cityDtoList = new ArrayList<>();
        List<City> cityPage;
        if (StringUtils.hasText(search)) {
            cityPage = cityRepository.findByFull_nameContainingIgnoreCase(search);
        } else {
            cityPage = cityRepository.findAll();
        }

        for (City city : cityPage) {
            System.out.println(city.getFull_name() + "ssss");
            CityDto cityDto = new CityDto();
            cityDto.setId(city.getId());
            cityDto.setName(city.getFull_name());
            cityDto.setDescription(city.getCityDescription());
            cityDto.setCityTax(city.getCityTax());
            cityDto.setRegion(city.getCc_fips());
            cityDto.setPromote(city.getPromote());
            cityDtoList.add(cityDto);
        }

        System.out.println(cityDtoList.size() + "lista");
        return cityDtoList;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getCountiesByCountry")
    @ResponseBody
    public List<County> getCountiesByCountryid(@RequestParam("countryId") Long countryid) {
        return countyService.getCountyByCountryId(countryid);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getCountiesByCountryName")
    @ResponseBody
    public List<County> getCountiesByCountryName(@RequestParam("countryName") String countryName) {
        return countyService.getCountyByCountryName(countryName);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/city/addCity")
    public ModelAndView addCity(HttpServletRequest request,
                                @RequestParam(required = false) Long id,
                                @RequestParam(name = "country")Long countryId,
                                @RequestParam(name = "county")Long countyId,
                                @RequestParam(name = "cityName")String cityName,
                                @RequestParam(name = "cityTax")Float cityTax,
                                @RequestParam(name = "description") String description,
                                Locale locale) {
        ModelAndView modelAndView = new ModelAndView();

        if (request.isUserInRole("ROLE_ADMIN")) {
            City city = new City(0,
                    countryRepository.findById(countryId).get().getId_iso(),
                    cityName,
                    countryRepository.findById(countryId).get().getId_fips(),
                    0,
                    countyRepository.findById(countyId).orElse(null),
                    false,
                    cityTax,
                    description);

            cityRepository.save(city);

            modelAndView.setViewName("redirect:/cityList");

        }

        return modelAndView;
    }

    @GetMapping("/helloo")
    @ResponseBody
    public String hii() {

       return "hello";


    }



}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
class GetcoutryName {
    private String countyName;
    private Long id;
}