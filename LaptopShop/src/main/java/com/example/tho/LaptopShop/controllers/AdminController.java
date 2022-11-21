package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.LaptopService;
import com.example.tho.LaptopShop.Services.OrderService;
import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.models.Laptop;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final PeopleService peopleService;
    private final LaptopService laptopService;
    private final OrderService orderService;


    @GetMapping("/order/view")
    public String viewOrders(Principal principal, Model model){

        model.addAttribute("person",peopleService.getUserByPrincipal(principal));
        model.addAttribute("orders",orderService.orderList());
        return "orders/orders-view";
    }
    @GetMapping("/order/view/{id}")
    public String orderInfo(@PathVariable Long id, Principal principal, Model model) {
        model.addAttribute("person",peopleService.getUserByPrincipal(principal));
        model.addAttribute("order",orderService.getOrderById(id));
        return "orders/order-info";
    }


    @GetMapping("/laptop/edit/{id}")
    public String editLaptopView(@PathVariable Long id, @ModelAttribute("laptop") Laptop laptop, Model model, Principal principal){
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        model.addAttribute("laptop", laptopService.getLaptopById(id));
        return "laptops/laptop-edit";

    }
    @PostMapping("/laptop/edit")
    public String editLaptop(@ModelAttribute("laptop") Laptop laptop,
                             @RequestParam("file") MultipartFile file) throws IOException {
        laptopService.save(laptop,file);
        return "redirect:/";

    }



    @GetMapping("/laptop/create")
    public String newLaptop(@ModelAttribute("laptop") Laptop laptop, Model model, Principal principal){
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        return "laptops/laptop-new";
    }

    @PostMapping("/laptop/create")
    public String create(@ModelAttribute("laptop") Laptop laptop,
                         @RequestParam("file")MultipartFile file) throws IOException {

        laptopService.save(laptop,file);
        return "redirect:/";
    }
}
