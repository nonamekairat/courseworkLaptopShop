package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Order;
import com.example.tho.LaptopShop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findOrdersByPerson(Person person);
    void deleteAllByPerson(Person person);
    void deleteOrdersByPerson(Person person);

}
