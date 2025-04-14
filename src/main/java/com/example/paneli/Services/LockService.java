package com.example.paneli.Services;

import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LockService {

    private final Lock lock = new ReentrantLock();

    public Lock getLock() {
        return lock;
    }

}