package com.example.tho.LaptopShop.controllers;


import com.example.tho.LaptopShop.Services.CategoryService;
import com.example.tho.LaptopShop.Services.LaptopService;
import com.example.tho.LaptopShop.Services.OrderService;
import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.models.Category;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final PeopleService peopleService;
    private final LaptopService laptopService;
    private final OrderService orderService;
    private final CategoryService categoryService;


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
        model.addAttribute("statuses", Arrays.stream(OrderStatus.values()).toList());
        return "orders/order-info";
    }
    @PostMapping("/order/view/{id}/change-status")
    public String changeStatusOfOrder(@PathVariable Long id, @RequestParam("newStatus") String newStatus){
        orderService.setNewStatus(id, newStatus);
        return "redirect:/order/view/" + id;
    }


    @GetMapping("/laptop/edit/{id}")
    public String editLaptopView(@PathVariable Long id, @ModelAttribute("laptop") Laptop laptop, Model model, Principal principal){
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        model.addAttribute("laptop", laptopService.getLaptopById(id));
        model.addAttribute("categories",categoryService.getCategories());
        return "laptops/laptop-edit";

    }
    @PostMapping("/laptop/edit")
    public String editLaptop(@ModelAttribute("laptop") Laptop laptop,
                             @RequestParam("file") MultipartFile file) throws IOException {
        laptopService.save(laptop,file);
        return "redirect:/";

    }
    @GetMapping("/users/view")
    public String viewUsers(Model model, Principal principal){
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        model.addAttribute("users", peopleService.getUsers());
        return "admin/users-view";
    }


    @GetMapping("/users/view/{id}")
    public String viewUserById(@PathVariable("id") Long id, Model model, Principal principal){
        Person user = peopleService.getUserById(id);
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        model.addAttribute("user", user);
        model.addAttribute("orders",orderService.orderListByUser(user));
        return "admin/user-detailed-view";
    }



    @PostMapping("/users/view/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id){

        //doesn't work make it work
        peopleService.deleteUserById(id);
        return "redirect:/users/view";
    }

    //probably remove replace with simple create Admin
    @PostMapping("/users/view/{id}/make-admin")
    public String makeAdmin(@PathVariable("id") Long id){
        peopleService.makeAdmin(id);
        return "redirect:/users/view";
    }
    @PostMapping("/users/view/{id}/ban")
    public String banUnUser(@PathVariable("id") Long id){
        peopleService.banUnBanUser(id);
        return "redirect:/users/view";
    }


    @GetMapping("/laptop/create")
    public String newLaptop(@ModelAttribute("laptop") Laptop laptop, Model model, Principal principal){
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        model.addAttribute("categories",categoryService.getCategories());
        return "laptops/laptop-new";
    }

    @PostMapping("/laptop/create")
    public String createLaptop(@ModelAttribute("laptop") Laptop laptop,
                         @RequestParam("file")MultipartFile file,
                               @RequestParam("category1")String category1,
                               @RequestParam("category2")String category2,
                               @RequestParam("category3")String category3) throws IOException {

        laptopService.save(laptop,file,category1,category2,category3);
        return "redirect:/";
    }

    @GetMapping("/categories")
    public String viewCategories(@ModelAttribute("category") Category category, Model model, Principal principal){
        model.addAttribute("person", peopleService.getUserByPrincipal(principal));
        model.addAttribute("categories",categoryService.getCategories());
        return "admin/categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute("category") Category category){
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @PostMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
    @PostMapping("/categories/{id}/delete/{laptop-id}")
    public String deleteCategoryFromLaptop(@PathVariable("id") Long id, @PathVariable("laptop-id") Long laptopId){
        laptopService.deleteCategory(id,laptopId);
        return "redirect:/laptop/edit/" + laptopId;
    }

    @PostMapping("/categories/add/{laptop-id}")
    public String addCategoryToLaptop(@PathVariable("laptop-id") Long laptopId,
                                      @RequestParam("category-add") String name){
        laptopService.addCategory(laptopId,name);
        return "redirect:/laptop/edit/" + laptopId;
    }
}
