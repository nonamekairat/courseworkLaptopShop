package com.example.tho.LaptopShop.models;

import com.example.tho.LaptopShop.models.enums.Role;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.nio.file.attribute.FileAttributeView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "people")
@Data
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Email should not be empty")
//    @UniqueElements(message = "Email")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @Column(length = 1000)
    private String password;

    private boolean active;
    private String address;
    private String phoneNumber;

    private String name;
    private String lastname;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image avatar;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bucket_id")
    private Bucket bucket;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "favorites_id")
    private Favorites favorites;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "favorite_laptops",
//            joinColumns = @JoinColumn(name="user_id"),
//            inverseJoinColumns = @JoinColumn(name = "laptop_id"))
//    private List<Laptop> favoriteLaptops;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;


}
