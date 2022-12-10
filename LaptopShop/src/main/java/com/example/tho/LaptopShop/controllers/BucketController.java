package com.example.tho.LaptopShop.controllers;



import com.example.tho.LaptopShop.Services.*;
import com.example.tho.LaptopShop.models.*;
import com.example.tho.LaptopShop.models.enums.DeliveryType;
import com.example.tho.LaptopShop.models.enums.OrderStatus;
import com.example.tho.LaptopShop.models.enums.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BucketController {


    private final PeopleService peopleService;
    private final OrderService orderService;
    private final BucketService bucketService;

    @GetMapping("/bucket")
    public String bucket(@ModelAttribute("order") Order order, Principal principal,Model model){
        Person person = peopleService.getUserByPrincipal(principal);
        bucketService.createBucket(person);
        order.setAddress(person.getAddress());
        order.setEmail(person.getEmail());
        order.setFirstName(person.getName());
        order.setLastName(person.getLastname());
        order.setPhoneNumber(person.getPhoneNumber());
        model.addAttribute("person",person);
        model.addAttribute("bucket",person.getBucket());
        model.addAttribute("paymentTypes", Arrays.stream(PaymentType.values()).toList());
        model.addAttribute("deliveryTypes", Arrays.stream(DeliveryType.values()).toList());
        return "bucket/bucket-view";
    }
    @PostMapping("/bucket/clear")
    public String clear(Principal principal){
        Person person = peopleService.getUserByPrincipal(principal);
        person.getBucket().removeLaptops();
        peopleService.save(person);
        return "redirect:/";
    }

    @PostMapping("/bucket/order")
    public String order(@ModelAttribute("order") Order order,
                        Principal principal
                        ) throws MessagingException {
        orderService.makeOrder(principal,order);

        return "redirect:/";
    }

}
