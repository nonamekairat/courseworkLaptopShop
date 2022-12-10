package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.Services.VerificationTokenService;
import com.example.tho.LaptopShop.models.Bucket;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.VerificationToken;
import com.example.tho.LaptopShop.util.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.Timestamp;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final VerificationTokenService verificationTokenService;

    @GetMapping("/login")
    public String loginPage(Principal principal, Model model,@ModelAttribute("message") String message){
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
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes){
        personValidator.validate(person, bindingResult);

        if(bindingResult.hasErrors())
            return "/auth/registration";
        redirectAttributes.addFlashAttribute("message",
                "Success! A verification email has been sent to your email address.");
        peopleService.register(person);
        return "redirect:/auth/login";
    }

    @GetMapping("/activation")
    public String activation(@RequestParam("token") String token, Model model){
        // create html page activation
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if(verificationToken == null){
            model.addAttribute("message","Your verification token is invalid.");
        }else {
            Person person = verificationToken.getPerson();
            if(!person.isActive()){
                Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
                //check if expired
                if(verificationToken.getExpiryDate().before(currentTimeStamp)){
                    model.addAttribute("message","Your verification toke has expired");
                }else {
                    // the token is valid
                    // activate the user account
                    person.setActive(true);
                    peopleService.save(person);
                    model.addAttribute("message","Your account is successfully activated");
                }
            }else {
                model.addAttribute("message","Your account is already activated");

            }
        }



        return "auth/activation";
    }





}
