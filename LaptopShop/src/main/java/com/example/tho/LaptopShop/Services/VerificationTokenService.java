package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.VerificationToken;
import com.example.tho.LaptopShop.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken findByToken(String token){
        return verificationTokenRepository.findByToken(token);
    }

    public VerificationToken findByPerson(Person person){
        return verificationTokenRepository.findByPerson(person);
    }

    public void save(Person person, String token){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setPerson(person);
        // set expiry date to 24 hours
        verificationToken.setExpiryDate(calculateExpiryDate(24 * 60));
        verificationTokenRepository.save(verificationToken);
    }

    private Timestamp calculateExpiryDate(int expiryTimeInMinutes){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(calendar.getTime().getTime());
    }


}
