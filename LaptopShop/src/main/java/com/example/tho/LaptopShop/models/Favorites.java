package com.example.tho.LaptopShop.models;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "favorite_laptops")
@Data
public class Favorites {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Person user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "laptop_id")
    private List<Laptop> laptops;
}
