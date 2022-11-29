package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.OrderService;
import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.util.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final OrderService orderService;

    @GetMapping("/profile")
    public String profile(Principal principal, Model model){
        Person person = peopleService.getUserByPrincipal(principal);

        model.addAttribute("orders",orderService.orderListByUser(person));
        model.addAttribute("person", person);
        return "users/profile";
    }

    @GetMapping("/profile/edit")
    public String getEditProfile(@ModelAttribute("person") Person person,Principal principal, Model model){
//        person = peopleService.getUserByPrincipal(principal);
        model.addAttribute("person",peopleService.getUserByPrincipal(principal));

        return "users/profileEdit";
    }
    @PreAuthorize("#username == authentication.name")
    @GetMapping("/profile/order/{username}/{id}")
    public String orderInfo(@PathVariable Long id,@PathVariable("username") String username, Principal principal, Model model) {
        model.addAttribute("person",peopleService.getUserByPrincipal(principal));
        model.addAttribute("order",orderService.getOrderById(id));
        return "orders/order-info";
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
