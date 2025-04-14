package com.example.paneli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**", "/Desktop/**", "/live/**", "/uploads/**", "/webjars/**", "/klienti/**").
                addResourceLocations("/resources/", "file:C:/Users/User/Desktop/", "file:/home/allbookersusr/home/BookersDesk/data/uploads/live/", "file:/home/allbookersusr/home/BookersDesk/data/klienti/" ,"file:/home/allbookersusr/home/BookersDesk/data/uploads/", "/webjars/");
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
