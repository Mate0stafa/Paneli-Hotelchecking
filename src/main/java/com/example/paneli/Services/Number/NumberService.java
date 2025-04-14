package com.example.paneli.Services.Number;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class NumberService {

    public String generateThreedigitNumber(){
        Random random = new Random();

// generate a random integer from 0 to 899, then add 100
        int x = random.nextInt(900) + 112;
        return String.valueOf(x);
    }

}
