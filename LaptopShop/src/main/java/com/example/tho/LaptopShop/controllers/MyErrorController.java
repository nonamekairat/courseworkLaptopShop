package com.example.tho.LaptopShop.controllers;

import com.example.tho.LaptopShop.Services.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MyErrorController implements ErrorController {


    private final PeopleService peopleService;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Principal principal, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("person",peopleService.getUserByPrincipal(principal));
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/error-404";
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error-500";
            }
        }
        return "error/error-by-default";
    }
}
