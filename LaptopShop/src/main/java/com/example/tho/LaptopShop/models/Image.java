package com.example.tho.LaptopShop.models;


import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "images")
@Data
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @Override
    public String toString() {
        return "/resources/" + name;
    }
}
