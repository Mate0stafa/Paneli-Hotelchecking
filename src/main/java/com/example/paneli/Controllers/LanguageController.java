package com.example.paneli.Controllers;

import com.example.paneli.Models.Language;
import com.example.paneli.Repositories.LanguageRepository;
import com.example.paneli.DataObjects.NewLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LanguageController {


    @Autowired
    LanguageRepository languageRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/languageList")
    public ModelAndView languageList(HttpServletRequest request, ModelAndView modelAndView, HttpSession session){
        if (request.isUserInRole("ROLE_ADMIN")){

            modelAndView.addObject("languages", languageRepository.findAll());
            modelAndView.setViewName("ROLE_ADMIN/languages/languageList");
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/showLanguage")
    public ModelAndView showCity(@RequestParam(value ="languageId") Long languageId, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.addObject("language", languageRepository.findById(languageId).get());
            modelAndView.setViewName("ROLE_ADMIN/languages/showLanguage");
        }
        return modelAndView;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addLanguage")
    public ModelAndView addLanguageGet(HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/languages/addLanguage");
            NewLanguage newLanguage = new NewLanguage();
            modelAndView.addObject("newLanguage", newLanguage);
        }

        return modelAndView;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/addLanguage")
    public ModelAndView addLanguage(HttpServletRequest request, NewLanguage newLanguage) {
        ModelAndView modelAndView = new ModelAndView();

        if (request.isUserInRole("ROLE_ADMIN")) {
            modelAndView.addObject("newLanguage", new NewLanguage());

            Language language = new Language(0, newLanguage.getCode(), newLanguage.getName());

            languageRepository.save(language);
            modelAndView.setViewName("ROLE_ADMIN/languages/addLanguage");

        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/editLanguage")
    public ModelAndView editLanguageGet(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView){
        if (request.isUserInRole("ROLE_ADMIN")){
            modelAndView.setViewName("ROLE_ADMIN/languages/editLanguage");
            modelAndView.addObject("language", languageRepository.findById(id).get());

        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editLanguage")
    public ModelAndView editLanguage(@RequestParam(value = "id") Long id, HttpServletRequest request, ModelAndView modelAndView, Language language){
        if (request.isUserInRole("ROLE_ADMIN")){

            modelAndView.setViewName("ROLE_ADMIN/languages/editLanguage");
            Language dbLanguage = languageRepository.findById(language.getId()).get();
            dbLanguage.setName(language.getName());
            dbLanguage.setCode(language.getCode());
//            County dbCounty = countyRepository.findById(country.getId()).get();
//            dbCountry.setCounties(dbCounty);
//            System.out.println(dbCounty.getName());
            languageRepository.save(dbLanguage);
            modelAndView.addObject("language", dbLanguage);
        }
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deleteLanguage")
    @ResponseBody
    public ResponseEntity<?> deleteLanguage(@RequestParam(value = "id") Long id) {
        try {
            Optional<Language> languageOpt = languageRepository.findById(id);
            if (!languageOpt.isPresent()) {
                return ResponseEntity.badRequest().body("Language not found");
            }

            Language language = languageOpt.get();

            if (!language.getPropertyList().isEmpty()) {
                return ResponseEntity.badRequest().body("Cannot delete this language. It is used by existing properties.");
            }

            if (!language.getUserClientList().isEmpty()) {
                return ResponseEntity.badRequest().body("Cannot delete this language. It is used by existing user clients.");
            }

            languageRepository.deleteById(id);
            return ResponseEntity.ok().body("Language deleted successfully");

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Cannot delete this language. It has related data.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred while deleting the language");
        }
    }
}
