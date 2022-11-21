package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.AdminService;
import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.security.PersonDetails;
import com.example.tho.LaptopShop.Services.AdminService;
import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HelloController {
    private final AdminService adminService;
    private final PeopleService peopleService;

    @GetMapping("/hello")
    public String sayHello(){
        return "hello";
    }

    @GetMapping("/showUserInfo")
    public String showUserInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        System.out.println(personDetails.getPerson());
        return "hello";
    }

    @GetMapping("/admin")
    public String adminPage(){
        adminService.doAdminStuff();
        return "admin";
    }

    @GetMapping("/user_info")
    public String userinfo(Model model, Principal principal){
        model.addAttribute("person",peopleService.getUserByPrincipal(principal));
        return "user-info";
    }
}
