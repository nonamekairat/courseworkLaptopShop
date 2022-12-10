package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
