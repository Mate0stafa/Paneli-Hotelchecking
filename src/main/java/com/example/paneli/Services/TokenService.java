package com.example.paneli.Services;

import com.example.paneli.Models.UserApiToken;
import com.example.paneli.Repositories.UserApiTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    @Autowired
    private UserApiTokenRepository userApiTokenRepository;
    @Autowired
    private DateService dateService;

    /**Gjenerojme tokens njeperdorimesh te vlefshem per 24 ore */
    public String generateSecureToken(Long userId) {
        //gjenerojme nje token te rastesishem prej 32 byte (256 bits)
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        //token id perdoret per ti bere lookup token value
        String tokenId = UUID.randomUUID().toString();
        String stringToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

        BCryptPasswordEncoder hasher = new BCryptPasswordEncoder(12);

        //token id + token value i dergohen si parametra url-je ne email perdoruesit
        String fullToken = tokenId + "." + stringToken;

        //hashi i tokenit ruhet ne db me emajlin e perdoruesit
        UserApiToken finalToken = new UserApiToken(
                tokenId,
                hasher.encode(stringToken),
                userId,
                false,
                dateService.getTomorrow()
        );

        userApiTokenRepository.save(finalToken);

        return fullToken;
    }

    /**Metoda per te verifikuar tokens */
    @Transactional
    public boolean validateToken(String token) {

        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            return false;
        }

        String tokenId = parts[0];
        String tokenValue = parts[1];

        BCryptPasswordEncoder hasher = new BCryptPasswordEncoder(12);

        //gjejme token te enkriptuar nga token value
        Optional<UserApiToken> tokenOptional = Optional.ofNullable(userApiTokenRepository.findByTokenId(tokenId));

        if (tokenOptional.isPresent()) {

            UserApiToken oneTimeToken = tokenOptional.get();

            //nese tokeni eshte i skaduar ai eshte nuk i vlefshem
            if (oneTimeToken.isExpired() || oneTimeToken.getExpirationDate().before(new Date())) {
                return false;
            }

            /*nese tokeni perputhet me tokenin e ruajtur ne db useri mund te krijoje nje password te ri,
              tokeni tashme shenohet si i skaduar (i perdorur)*/

            if(hasher.matches(tokenValue, oneTimeToken.getTokenValue())){
                oneTimeToken.setExpired(true);
                userApiTokenRepository.save(oneTimeToken);
                return true;
            }

        }

        return false;
    }
    //metode ndihmese me te cilen sigurohemi qe useri qe po perdor tokenin eshte po ai qe u ruajt bashke me tokenin
    public Long getTokenUserID(String token){
        String[] parts = token.split("\\.");
        return userApiTokenRepository.findByTokenId(parts[0]).getUserId();
    }
}
