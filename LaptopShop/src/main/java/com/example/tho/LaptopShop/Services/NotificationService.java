package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Favorites;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Notification;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.repositories.NotificationRepository;
import com.example.tho.LaptopShop.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PeopleRepository peopleRepository;

    public void save(Laptop laptop, Person person) {
        Notification notification = notificationRepository.findByLaptopAndPerson(laptop,person).orElse(new Notification());
        notification.setPerson(person);
        notification.setLaptopUpdate(false);
        notification.setLaptop(laptop);
        notificationRepository.save(notification);
    }
    public List<Notification> getNotifications() {
        return notificationRepository.findAll();
    }
    public Notification getNotificationById(Long id){
        return notificationRepository.findById(id).orElse(null);
    }

    public List<Notification> getNotificationsByPerson(Person person) {
        return notificationRepository.findAllByPerson(person);
    }
    public List<Notification> getUpdatedNotificationsByPerson(Person person) {
        return notificationRepository.findAllByPersonAndLaptopUpdateIsTrue(person);
    }

    public void deleteNotificationById(Long id) {
        Notification notification = getNotificationById(id);
        Person person = notification.getPerson();
        person.setNotificationCount(person.getNotificationCount() - 1);
        peopleRepository.save(person);
        notificationRepository.delete(notification);
    }

    public List<Laptop> notificationsToLaptops(List<Notification> notifications) {
        return notifications.stream().map(Notification::getLaptop).toList();
    }
}
