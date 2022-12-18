package com.example.tho.LaptopShop.models;


import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "laptops")
@Data
public class Laptop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    in the future
//    @OneToMany(cascade = CascadeType.ALL,fetch= FetchType.EAGER)
//    private List<Image> images = new ArrayList<>();
    private String imageName;
    private String title;
    @Column(length = 1000)
    private String description;
    @Column(length = 1000)
    private String model;
    private int price;
    private int amount;
    private double score;
    private boolean visible;


    @CreationTimestamp
    private LocalDateTime dateOfCreated;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "laptops_categories",
            joinColumns = @JoinColumn(name="laptop_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setLaptop(null);
    }

    @Override
    public String toString() {
        return "/resources/" + imageName;
    }
}
