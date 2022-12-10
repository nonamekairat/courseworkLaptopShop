package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Category;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {
    List<Laptop> findAllByAmountGreaterThan(int amount);

    List<Laptop> findAllByModelContains(String title);
    List<Laptop> findAllByModelContainsAndAmountGreaterThan(String title, int amount);

    List<Laptop> findAllByModelContainsAndCategoriesContains(String title, Category category);
    List<Laptop> findAllByModelContainsAndAmountGreaterThanAndCategoriesContains(String title, int amount, Category category);


    List<Laptop> findAllByCategoriesContains(Category category);
    List<Laptop> findAllByCategoriesContainsAndAmountGreaterThan(Category category,int amount);
}
