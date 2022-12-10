package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.*;
import com.example.tho.LaptopShop.models.*;
import com.example.tho.LaptopShop.models.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LaptopController {

    private final LaptopService laptopService;
    private final PeopleService peopleService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    @GetMapping("/")
    public String startPage(@RequestParam(name = "searchWord",required = false) String title,
            @RequestParam(name = "category",required = false) String categoryName, Model model, Principal principal){
        Person person = peopleService.getUserByPrincipal(principal);
        List<Laptop> laptops;
        if(person.getRoles().contains(Role.ROLE_ADMIN)) laptops = laptopService.listLaptopAdmin(title,categoryName);
        else laptops = laptopService.listLaptopUser(title,categoryName);

        model.addAttribute("laptops",laptops);
        model.addAttribute("person",person);
        model.addAttribute("categories",categoryService.getCategories());
        return "laptops/start-page";
    }

    @GetMapping("/laptop/view/{id}")
    public String laptopInfo(@ModelAttribute("review") Review review, @PathVariable Long id, Model model, Principal principal) {
        Laptop laptop = laptopService.getLaptopById(id);
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        model.addAttribute("laptop", laptop);
        return "laptops/laptop-info";
    }

    @PostMapping("/laptop/add-to-bucket/{id}")
    public String laptopToCart(@PathVariable Long id, Principal principal){
        Laptop laptop = laptopService.getLaptopById(id);
        peopleService.laptopToCart(principal,laptop);
        return "redirect:/";
    }

    @PostMapping("laptop/make-review/{id}")
    public String makeReview(@PathVariable Long id, @ModelAttribute("review") Review review1, Principal principal){
        Review review = new Review();
        review.setPerson(peopleService.getUserByPrincipal(principal));
        review.setText(review1.getText());
        reviewService.save(review);
        Laptop laptop = laptopService.getLaptopById(id);
        List<Review> reviews = laptop.getReviews();
        reviews.add(review);
        laptop.setReviews(reviews);
        laptopService.save(laptop);
        return "redirect:/laptop/view/" + id;
    }


}
