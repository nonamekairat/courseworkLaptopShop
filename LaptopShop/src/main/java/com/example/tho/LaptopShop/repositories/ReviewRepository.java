package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    void deleteAllByPerson(Person person);

    List<Review> findAllByPerson(Person person);
}
