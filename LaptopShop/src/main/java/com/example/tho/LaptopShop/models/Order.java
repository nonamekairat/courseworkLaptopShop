package com.example.tho.LaptopShop.models;


import com.example.tho.LaptopShop.models.enums.OrderStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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
    private String address;
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn( name = "details_id")
    private List<OrderDetails> details;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
