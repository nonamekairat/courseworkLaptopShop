package com.example.tho.LaptopShop.models;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "buckets")
@Data
public class Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Person user;

    @ManyToMany
    @JoinTable(name = "buckets_laptops",
    joinColumns = @JoinColumn(name="bucket_id"),
    inverseJoinColumns = @JoinColumn(name = "laptop_id"))
    private List<Laptop> laptops;

    public void removeLaptops(){

        laptops.removeAll(laptops);
    }




}
