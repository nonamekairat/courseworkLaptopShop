package com.example.tho.LaptopShop.models;


import lombok.Data;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notifications")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    private boolean laptopUpdate;

    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "user_id")
    private Person person;

    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "laptop_id")
    private Laptop laptop;




}
