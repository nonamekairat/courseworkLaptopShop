package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Order;
import com.example.tho.LaptopShop.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public void create(Order order) {
        orderRepository.save(order);
    }

    public List<Order> orderList(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(new Order());
    }
}
