package com.example.tho.LaptopShop.models;


import com.example.tho.LaptopShop.models.enums.DeliveryType;
import com.example.tho.LaptopShop.models.enums.OrderStatus;
import com.example.tho.LaptopShop.models.enums.PaymentType;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime created;
//    later
    @UpdateTimestamp
    private LocalDateTime updated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Person person;
    private BigDecimal sum;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Email(message = "Email should be valid")
    private String email;
    private String address;
    private String firstName;
    private String lastName;
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name = "details_id")
    private List<OrderDetails> details;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type")
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
