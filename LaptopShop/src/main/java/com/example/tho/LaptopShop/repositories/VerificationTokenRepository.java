package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByPerson(Person person);


}
