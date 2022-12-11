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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@Controller
@RequiredArgsConstructor
public class LaptopController {

    private final LaptopService laptopService;
    private final PeopleService peopleService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final FavoritesService favoritesService;
    private final NotificationService notificationService;

    @GetMapping("/")
    public String startPage(@RequestParam(name = "searchWord",required = false) String title,
                            @RequestParam(name = "category",required = false) String categoryName,
                            @RequestParam(name = "field1",required = false) String field1,
                            @RequestParam(name = "field2",required = false) String ascDesc,
                            Model model, Principal principal){
        Person person = peopleService.getUserByPrincipal(principal);
        List<Order> orders = new ArrayList<>();
        if(field1 != null) {
            if(ascDesc == null) orders.add(new Order(Direction.ASC, field1));
            else if(ascDesc.equals("DESC")) orders.add(new Order(Direction.DESC, field1));
            else orders.add(new Order(Direction.ASC, field1));
        }
        List<Laptop> laptops;
        laptops = laptopService.listLaptopAdmin(title,categoryName,orders);

        model.addAttribute("laptops",laptops);
        model.addAttribute("person",person);
        model.addAttribute("categories",categoryService.getCategories());
        return "laptops/start-page";
    }

    @GetMapping("/laptop/view/{id}")
    public String laptopInfo(@ModelAttribute("review") Review review, @PathVariable Long id, Model model, Principal principal) {
        Person person = peopleService.getUserByPrincipal(principal);
        Laptop laptop = laptopService.getLaptopById(id);
        boolean haveReview = reviewService.isPersonHaveReview(person,laptop);
        boolean haveInFavorites = favoritesService.isPersonHaveInFavorites(person,laptop);
        model.addAttribute("person", person);
        model.addAttribute("haveReview", haveReview);
        model.addAttribute("haveInFavorites", haveInFavorites);
        model.addAttribute("laptop", laptop);
        return "laptops/laptop-info";
    }

    @PostMapping("/laptop/add-to-bucket/{id}")
    public String laptopToCart(@PathVariable Long id, Principal principal){
        Laptop laptop = laptopService.getLaptopById(id);
        peopleService.laptopToCart(principal,laptop);
        return "redirect:/";
    }

    @PostMapping("/laptop/make-review/{id}")
    public String makeReview(@PathVariable Long id, @ModelAttribute("review") Review review1, Principal principal){
        Review review = new Review();
        review.setPerson(peopleService.getUserByPrincipal(principal));
        review.setText(review1.getText());
        review.setScore(review1.getScore());
        reviewService.save(review);
        Laptop laptop = laptopService.getLaptopById(id);
        List<Review> reviews = laptop.getReviews();
        reviews.add(review);
        laptop.setReviews(reviews);
        laptopService.save(laptop);
        laptopService.setAverageScore(laptop);
        return "redirect:/laptop/view/" + id;
    }

    @PostMapping("/laptop/{laptop_id}/review/{id}/delete")
    public String deleteReview(@PathVariable("id") Long id,@PathVariable("laptop_id") Long laptop_id){
        reviewService.delete(reviewService.findById(id));
        return "redirect:/laptop/view/" + laptop_id;
    }

    @PostMapping("/laptop/view/{id}/make-favorite")
    public String makeFavorite(@PathVariable Long id, Principal principal){
        Laptop laptop = laptopService.getLaptopById(id);
        Person person = peopleService.getUserByPrincipal(principal);
        favoritesService.save(laptop,person);
        return "redirect:/laptop/view/" + id;
    }

    @PostMapping("/laptop/view/{id}/notification")
    public String notificationCreate(@PathVariable Long id, Principal principal){
        Laptop laptop = laptopService.getLaptopById(id);
        Person person = peopleService.getUserByPrincipal(principal);
        notificationService.save(laptop,person);
        return "redirect:/laptop/view/" + id;
    }

    @PostMapping("/laptop/view/{id}/remove-favorite")
    public String removeFavorite(@PathVariable Long id, Principal principal){
        Laptop laptop = laptopService.getLaptopById(id);
        Person person = peopleService.getUserByPrincipal(principal);
        favoritesService.remove(laptop,person);
        return "redirect:/laptop/view/" + id;
    }

    @GetMapping("/laptop/favorites")
    public String viewFavorites(Principal principal, Model model){
        Person person = peopleService.getUserByPrincipal(principal);
        favoritesService.createfavorites(person);
        Favorites favorites = person.getFavorites();
        model.addAttribute("person", person);
        model.addAttribute("favorites",favorites);
        return "laptops/favorite-laptops";

    }


}
