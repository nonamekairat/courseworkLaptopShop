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

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})

    @JoinColumn(name = "user_id")
    private Person user;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "favorites_laptops",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "laptop_id"))
    private List<Laptop> laptops;

    public void removeLaptop(Laptop laptop){
        laptops.remove(laptop);
    }
}
