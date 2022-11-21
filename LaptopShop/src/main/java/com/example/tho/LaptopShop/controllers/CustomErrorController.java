package com.example.tho.LaptopShop.controllers;

import com.example.tho.LaptopShop.Services.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {


//    private final PeopleService peopleService;

//    @RequestMapping("/error")
//    @ResponseBody
//    String error(HttpServletRequest request) {
//        return "<h1>Error occurred</h1>";
//    }
//
//
//    public String getErrorPath(Principal principal, Model model) {
//        model.addAttribute("person",peopleService.getUserByPrincipal(principal));
//        return "/error";
//    }
}
