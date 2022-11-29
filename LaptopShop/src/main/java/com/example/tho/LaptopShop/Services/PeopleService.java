package com.example.tho.LaptopShop.Services;

import com.example.tho.LaptopShop.models.Bucket;
import com.example.tho.LaptopShop.models.Image;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.models.enums.Role;
import com.example.tho.LaptopShop.repositories.ImageRepository;
import com.example.tho.LaptopShop.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final ImageRepository imageRepository;
    @Value("${upload.path}")
    private String uploadPath;


    public List<Person> getUsers(){
        return peopleRepository.findAll();
    }

    public Optional<Person> usernameExist(String username){
        return peopleRepository.findByUsername(username);
    }
    public Person getEmail(String email){
        return peopleRepository.findByEmail(email);
    }

    public Person getUserByPrincipal(Principal principal){
        if(principal == null) return new Person();
        return peopleRepository.findByUsername(principal.getName()).orElse(new Person());
    }

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.getRoles().add(Role.ROLE_USER);
        person.setActive(true);
        peopleRepository.save(person);
    }

    public Person getUserById(Long id){
        return peopleRepository.findById(id).orElse(new Person());
    }
    public void save(Person person) {peopleRepository.save(person);}
    public void laptopToCart(Principal principal, Laptop laptop){
        List<Laptop> laptops;
        Person person = getUserByPrincipal(principal);
        Bucket bucket = person.getBucket();
        if(bucket == null) bucket = new Bucket();
//        bucket.setUser(person);
//        if(person.getBucket() == null){
//            person.setBucket(new Bucket());
//        }
//        Bucket bucket = person.getBucket();
        if(bucket.getLaptops() != null) laptops = bucket.getLaptops();
        else laptops = new ArrayList<>();
        laptops.add(laptop);
        bucket.setLaptops(laptops);
        bucket.setUser(person);
        person.setBucket(bucket);
        peopleRepository.save(person);
    }

    @Transactional
    public void change(Person person, MultipartFile file) throws IOException {
        if(!file.isEmpty()){
//            System.out.println("File not null");
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists())
                uploadDir.mkdir();
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            Image image;
            if(person.getAvatar().getName() != null){
                image = imageRepository.findById(person.getAvatar().getId()).orElse(new Image());
                image.setName(resultFilename);
//                System.out.println(image);
//                imageRepository.save(image);
            }
            else {
                image = new Image();
                image.setName(resultFilename);
                person.setAvatar(image);
            }
        }
        peopleRepository.save(person);
    }

    public void deleteUserById(Long id) {
        peopleRepository.deleteById(id);
    }

    public void makeAdmin(Long id) {
        Person person = peopleRepository.findById(id).orElse(new Person());
        if(person.getRoles().contains(Role.ROLE_USER)){
            person.getRoles().removeAll(person.getRoles());
            person.getRoles().add(Role.ROLE_ADMIN);
        }else {
            person.getRoles().removeAll(person.getRoles());
            person.getRoles().add(Role.ROLE_USER);
        }
        peopleRepository.save(person);
    }

    public void banUnBanUser(Long id) {
        Person person = peopleRepository.findById(id).orElse(new Person());
        if(person.isActive()){
            person.setActive(false);
        }else {
            person.setActive(true);
        }
        peopleRepository.save(person);
    }
}
