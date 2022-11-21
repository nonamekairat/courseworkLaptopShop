package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.models.Bucket;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.util.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @GetMapping("/login")
    public String loginPage(Principal principal, Model model){
        model.addAttribute("person",peopleService.getUserByPrincipal(principal));
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person, Principal principal, Model model){
        model.addAttribute("person",peopleService.getUserByPrincipal(principal));
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult){
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors())
            return "/auth/registration";

        peopleService.register(person);
        return "redirect:/auth/login";
    }





}
