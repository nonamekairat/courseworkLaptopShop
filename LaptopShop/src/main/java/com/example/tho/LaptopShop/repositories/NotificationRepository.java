package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Notification;
import com.example.tho.LaptopShop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByPerson(Person person);
    List<Notification> findAllByPersonAndLaptopUpdateIsTrue(Person person);
    List<Notification> findAllByLaptop(Laptop laptop);

    Optional<Notification> findByLaptopAndPerson(Laptop laptop, Person person);

    void deleteByPerson(Person person);

//    void deleteAllByLaptop(Laptop laptop);
    void deleteAllByLaptop(Laptop laptop);
}
