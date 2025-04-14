package com.example.paneli.Models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_login_attempts")
public class UserLoginAttempts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "attempts")
    private Integer attempts;

    @Column(name = "blocked_until")
    private LocalDateTime blockedUntil;

    public UserLoginAttempts() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public LocalDateTime getBlockedUntil() {
        return blockedUntil;
    }

    public void setBlockedUntil(LocalDateTime blockedUntil) {
        this.blockedUntil = blockedUntil;
    }

    public void blockUserForGivenMinutes(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime blockUntil = now.plusMinutes(minutes);
        setBlockedUntil(blockUntil);
    }

    public boolean isUserBlocked() {
        if (getBlockedUntil() != null) {
            if (getBlockedUntil().isAfter(LocalDateTime.now())) {
                return true;
            }
        }
        return false;
    }

}
