package com.example.paneli.Services;

import com.example.paneli.Models.UserApiToken;
import com.example.paneli.Repositories.UserApiTokenRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class TokenDeletionService {

    @Async
    public CompletableFuture<Void> deleteTokenAfterDelay(UserApiTokenRepository userApiTokenRepository, UserApiToken userApiToken){
        try{
            Thread.sleep(10*60*1000); //Sleep for 10 minutes
            userApiTokenRepository.delete(userApiToken);
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }

}
