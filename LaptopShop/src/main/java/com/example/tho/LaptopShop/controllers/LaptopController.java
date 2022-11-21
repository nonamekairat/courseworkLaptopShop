package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.BucketService;
import com.example.tho.LaptopShop.Services.LaptopService;
import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.models.Bucket;
import com.example.tho.LaptopShop.models.Image;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
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

    @GetMapping("/")
    public String startPage(@RequestParam(name = "searchWord",required = false) String title, Model model, Principal principal){
        Person person = peopleService.getUserByPrincipal(principal);
        List<Laptop> laptops;
        if(person.getRoles().contains(Role.ROLE_ADMIN)) laptops = laptopService.listLaptopAdmin(title);
        else laptops = laptopService.listLaptopUser(title);

        model.addAttribute("laptops",laptops);
        model.addAttribute("person",person);
        return "laptops/start-page";
    }

    @GetMapping("/laptop/{id}")
    public String laptopInfo(@PathVariable Long id, Model model, Principal principal) {
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

}
