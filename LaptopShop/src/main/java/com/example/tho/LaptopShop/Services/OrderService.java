package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.*;
import com.example.tho.LaptopShop.models.enums.OrderStatus;
import com.example.tho.LaptopShop.repositories.LaptopRepository;
import com.example.tho.LaptopShop.repositories.OrderRepository;
import com.example.tho.LaptopShop.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final LaptopRepository laptopRepository;
    private final PeopleRepository peopleRepository;
    private final EmailService emailService;

    public void create(Order order) {
        orderRepository.save(order);
    }

    public List<Order> orderList(String status, List<Sort.Order> orders){
        if(status != null){

            return orderRepository.findAllByStatus(OrderStatus.valueOf(status), Sort.by(orders));
        }
        else {
            return orderRepository.findAll(Sort.by(orders));
        }
    }

    public List<Order> orderListByUser(Person person){
        return orderRepository.findOrdersByPerson(person);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(new Order());
    }

    public Person getUserByPrincipal(Principal principal){
        if(principal == null) return new Person();
        return peopleRepository.findByUsername(principal.getName()).orElse(new Person());
    }

    public void setNewStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId).get();

        if(order.getStatus().equals(OrderStatus.CANCELED) && newStatus.equals(String.valueOf(OrderStatus.CANCELED))){
            return;
        }

        if(order.getStatus().equals(OrderStatus.CANCELED) && !newStatus.equals(String.valueOf(OrderStatus.CANCELED))){

            List<Laptop> laptops = order.getDetails().stream().map(OrderDetails::getLaptop).toList();
            for (int i = 0; i < laptops.size(); i++) {
                Laptop laptop = laptops.get(i);
                laptop.setAmount(laptop.getAmount() - 1);
                laptopRepository.save(laptop);
            }

        }

        if(!order.getStatus().equals(OrderStatus.CANCELED) && newStatus.equals(String.valueOf(OrderStatus.CANCELED))){

            List<Laptop> laptops = order.getDetails().stream().map(OrderDetails::getLaptop).toList();
            for (int i = 0; i < laptops.size(); i++) {
                Laptop laptop = laptops.get(i);
                laptop.setAmount(laptop.getAmount() + 1);
                laptopRepository.save(laptop);
            }

        }

        order.setStatus(OrderStatus.valueOf(newStatus));
        orderRepository.save(order);
    }

    public void makeOrder(Principal principal, Order orderInfo) throws MessagingException {
        Person person = getUserByPrincipal(principal);
        Order order = new Order();
        order.setPerson(person);

        order.setAddress(orderInfo.getAddress());
        order.setEmail(orderInfo.getEmail());
        order.setLastName(orderInfo.getLastName());
        order.setFirstName(orderInfo.getFirstName());
        order.setPhoneNumber(orderInfo.getPhoneNumber());
        order.setDeliveryType(orderInfo.getDeliveryType());
        order.setPaymentType(orderInfo.getPaymentType());

        order.setStatus(OrderStatus.NEW);
        order.setDescription(orderInfo.getDescription());
        BigDecimal sum = new BigDecimal(0);
        List<OrderDetails> orderDetails = new ArrayList<>();
        for (Laptop laptop : person.getBucket().getLaptops()) {

            OrderDetails orderDetail = new OrderDetails();
            sum = sum.add(BigDecimal.valueOf(laptop.getPrice()));
            orderDetail.setLaptop(laptop);
            orderDetails.add(orderDetail);
//          Доделать`
            if(laptop.getAmount() != 0) laptop.setAmount(laptop.getAmount() - 1);

            laptopRepository.save(laptop);
        }
        order.setDetails(orderDetails);
        order.setSum(sum);
        person.getBucket().removeLaptops();
        peopleRepository.save(person);
        create(order);

        // remake
        String content = "<p>Hello,</p>"
                + "<p>You have ordered Laptop.</p>"
                + "<p>Click the link below to see your laptop progress:</p>"
                + "<p><a href=\"" + "http://localhost:8080/profile/" + "\">view orders</a></p>"
                + "<br>";
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(person.getEmail());
        emailMessage.setSubject("Order laptop");
        emailMessage.setMessage(content);



        emailService.sendMail(emailMessage);
    }

    public List<Laptop> laptopList(Order order) {
        if (order.getDetails() != null)
            return order.getDetails().stream().map(OrderDetails::getLaptop).toList();
        else return new ArrayList<>();
    }
}
