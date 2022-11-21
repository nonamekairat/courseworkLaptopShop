package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    List<Laptop> findByTitleContains(String title);
    List<Laptop> findByModelContains(String title);
}
