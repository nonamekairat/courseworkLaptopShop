package com.example.tho.LaptopShop.models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "orders_details")
@Data
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "laptop_id")
    private Laptop laptop;



}
