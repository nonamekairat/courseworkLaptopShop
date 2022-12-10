package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Review;
import com.example.tho.LaptopShop.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {


    private final ReviewRepository reviewRepository;

    public void save(Review review){
        reviewRepository.save(review);
    }
}
