package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Bucket;
import com.example.tho.LaptopShop.models.Favorites;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);
    Person findByEmail(String mail);
    void deletePersonById(Long id);
    Person findByResetPasswordToken(String token);


    List<Person> findByUsernameContaining(String username);

    Person findByBucket(Bucket bucket);

    Person findByFavorites(Favorites favorite);
}
