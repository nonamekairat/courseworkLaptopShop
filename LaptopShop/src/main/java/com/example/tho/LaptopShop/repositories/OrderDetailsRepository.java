package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails,Long> {

    void deleteAllByLaptop(Laptop laptop);
}
