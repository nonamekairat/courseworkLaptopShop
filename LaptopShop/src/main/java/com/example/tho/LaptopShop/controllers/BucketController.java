package com.example.tho.LaptopShop.controllers;



import com.example.tho.LaptopShop.Services.BucketService;
import com.example.tho.LaptopShop.Services.LaptopService;
import com.example.tho.LaptopShop.Services.OrderService;
import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.models.*;
import com.example.tho.LaptopShop.models.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BucketController {


    private final PeopleService peopleService;
    private final OrderService orderService;
    private final BucketService bucketService;
    private final LaptopService laptopService;


    @GetMapping("/bucket")
    public String bucket(Principal principal,Model model){
        Person person = peopleService.getUserByPrincipal(principal);
        model.addAttribute("person",person);
        model.addAttribute("bucket",person.getBucket());
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
    public String order(Principal principal, @RequestParam(name="description",required = false) String description){
        Person person = peopleService.getUserByPrincipal(principal);
        Order order = new Order();
        order.setPerson(person);
        order.setAddress(person.getAddress());
        order.setStatus(OrderStatus.NEW);
        order.setDescription(description);
        BigDecimal sum = new BigDecimal(0);
        List<OrderDetails> orderDetails = new ArrayList<>();
        for (Laptop laptop : person.getBucket().getLaptops()) {

            OrderDetails orderDetail = new OrderDetails();
            sum = sum.add(BigDecimal.valueOf(laptop.getPrice()));
            orderDetail.setLaptop(laptop);
            orderDetails.add(orderDetail);
//          Доделать
            if(laptop.getAmount() != 0) laptop.setAmount(laptop.getAmount() - 1);

            laptopService.save(laptop);
        }
        order.setDetails(orderDetails);
        order.setSum(sum);
        person.getBucket().removeLaptops();
        peopleService.save(person);
        orderService.create(order);


        return "redirect:/";
    }

}
