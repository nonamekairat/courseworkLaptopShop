package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
