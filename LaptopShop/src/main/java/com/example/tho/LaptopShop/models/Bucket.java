package com.example.tho.LaptopShop.models;


import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "buckets")
@Data
public class Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private Person user;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "buckets_laptops",
    joinColumns = @JoinColumn(name="bucket_id"),
    inverseJoinColumns = @JoinColumn(name = "laptop_id"))
    private List<Laptop> laptops;

    public void removeLaptops(){
        laptops.removeAll(laptops);
    }
    public void removeLaptop(Laptop laptop){
        laptops.remove(laptop);
    }

    public String getSum(){
        return  "Sum: " + laptops.stream().mapToDouble(Laptop::getPrice).sum();
    }




}
