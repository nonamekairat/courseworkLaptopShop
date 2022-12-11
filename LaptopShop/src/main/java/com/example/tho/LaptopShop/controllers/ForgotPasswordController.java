package com.example.tho.LaptopShop.controllers;

import com.example.tho.LaptopShop.Services.EmailService;
import com.example.tho.LaptopShop.Services.PeopleService;


import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.util.Utility;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final EmailService emailService;
    private final PeopleService peopleService;



    @GetMapping("/forgot_username")
    public String showForgotUsernameForm(){
        return "forgot-data/send-forgot-username";
    }

    @PostMapping("/forgot_username")
    public String sendForgotUsername(HttpServletRequest request, Model model){
        String email = request.getParameter("email");
        try {

            emailService.sendForgottenUsername(email);
            model.addAttribute("message", "We have sent an username to your email. Please check.");

        } catch (Exception e){
            e.printStackTrace();
        }
        return "message";
    }



    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot-data/email-send";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            peopleService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            emailService.sendForgottenPassword(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (Exception e){
            e.printStackTrace();
        }
        return "message";
    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        Person person = peopleService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (person == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "forgot-data/reset-password-form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        Person person = peopleService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (person == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            peopleService.updatePassword(person, password);
            System.out.println(password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "message";
    }
}
