package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.util.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @GetMapping("/profile")
    public String profile(Principal principal, Model model){
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        return "users/profile";
    }

    @GetMapping("/profile/edit")
    public String getEditProfile(@ModelAttribute("person") Person person,Principal principal, Model model){
//        person = peopleService.getUserByPrincipal(principal);
        model.addAttribute("person",peopleService.getUserByPrincipal(principal));

        return "users/profileEdit";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute("person") @Valid Person person,@RequestParam("file") MultipartFile file,
                              BindingResult bindingResult) throws IOException {
        personValidator.validate(person,bindingResult);
        if(bindingResult.hasErrors())
            return "/users/profileEdit";

        peopleService.change(person,file);
        return "redirect:/profile";

    }




}
