package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.Review;
import com.example.tho.LaptopShop.repositories.LaptopRepository;
import com.example.tho.LaptopShop.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {


    private final ReviewRepository reviewRepository;
    private final LaptopRepository laptopRepository;


    public void save(Review review){
        reviewRepository.save(review);
    }

    public void delete(Review review, Long laptop_id){
        Laptop laptop = laptopRepository.findById(laptop_id).orElse(null);
        List<Review> reviews = laptop.getReviews();
        reviews.remove(review);
        laptop.setReviews(reviews);
        laptopRepository.save(laptop);

        reviewRepository.delete(review);
    }
    public Review findById(Long id){
        return reviewRepository.findById(id).get();
    }

    public boolean isPersonHaveReview(Person person, Laptop laptop) {
        List<Review> laptopReviews = laptop.getReviews();
        return laptopReviews.stream().filter(r -> r.getPerson().getUsername().equals(person.getUsername()))
                .toList().size() > 0;
    }

}
