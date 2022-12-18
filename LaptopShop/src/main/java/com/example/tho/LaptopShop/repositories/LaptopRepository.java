package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Category;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.Review;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaptopRepository extends JpaRepository<Laptop, Long> {

    List<Laptop> findAll(Sort sort);

    List<Laptop> findAllByModelContains(String title, Sort sort);
    List<Laptop> findAllByModelContainsAndCategoriesContains(String title, Category category, Sort sort);
    List<Laptop> findAllByCategoriesContains(Category category, Sort sort);
    List<Laptop> findAllByReviewsContaining(Review review, Sort sort);

    List<Laptop> findAllByModelContains(String title);
    List<Laptop> findAllByModelContainsAndCategoriesContains(String title, Category category);
    List<Laptop> findAllByCategoriesContains(Category category);
    List<Laptop> findAllByReviewsContaining(Review review);

}
