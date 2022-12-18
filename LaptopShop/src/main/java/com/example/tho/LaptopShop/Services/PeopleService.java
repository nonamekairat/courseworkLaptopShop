package com.example.tho.LaptopShop.Services;

import com.example.tho.LaptopShop.models.*;
import com.example.tho.LaptopShop.models.enums.Role;
import com.example.tho.LaptopShop.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final BucketRepository bucketRepository;
    private final FavoritesRepository favoritesRepository;
    private final NotificationRepository notificationRepository;
    private final ReviewRepository reviewRepository;
    @Value("${upload.path}")
    private String uploadPath;
    private final OrderDetailsRepository orderDetailsRepository;
    private final LaptopRepository laptopRepository;


    public List<Person> getUsers(){
        return peopleRepository.findAll();
    }

    public Optional<Person> usernameExist(String username){
        return peopleRepository.findByUsername(username);
    }
    public Person getByEmail(String email){
        return peopleRepository.findByEmail(email);
    }

    public Person getUserByPrincipal(Principal principal){
        if(principal == null) return new Person();
        return peopleRepository.findByUsername(principal.getName()).orElse(new Person());
    }


    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.getRoles().add(Role.ROLE_USER);
        person.setActive(false);

        peopleRepository.save(person);
        Optional<Person> saved = Optional.of(person);
        saved.ifPresent(u -> {
            try {
                String token = UUID.randomUUID().toString();
                verificationTokenService.save(saved.get(),token);
                emailService.sendHtmlMail(u);
            }catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public Person getUserById(Long id){
        return peopleRepository.findById(id).orElse(new Person());
    }
    public Person getUserByUsername(String username){
        return peopleRepository.findByUsername(username).orElse(null);
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
    public void change(Person person, MultipartFile file,Person oldPerson) throws IOException {
        oldPerson.setAddress(person.getAddress());
        oldPerson.setName(person.getName());
        oldPerson.setUsername(person.getUsername());
        oldPerson.setLastname(person.getLastname());
        oldPerson.setEmail(person.getEmail());
        oldPerson.setPhoneNumber(person.getPhoneNumber());

        if(!file.isEmpty() && file.getSize() > 0){
//            System.out.println("File not null");
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists())
                uploadDir.mkdir();
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            Image image = new Image();
            image.setName(resultFilename);
//            imageRepository.save(image);
            oldPerson.setAvatar(image);
        }
        peopleRepository.save(oldPerson);

        Collection<SimpleGrantedAuthority> nowAuthorities =
                (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities();
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(oldPerson.getUsername(), oldPerson.getPassword(), nowAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Transactional
    public void deleteUserById(Long id) {
        Person person = peopleRepository.findById(id).orElse(null);

        verificationTokenService.deleteByPerson(person);
        notificationRepository.deleteByPerson(person);
        orderRepository.deleteOrdersByPerson(person);


//        List<Review> reviews = reviewRepository.findAllByPerson(person);
//        for (Review review : reviews) {
//            Laptop laptop = review.getLaptop();
//            if(laptop != null) {
//                System.out.println(laptop.getId());
//                System.out.println(review);
//                laptop.removeReview(review);
//                System.out.println(review);
//                System.out.println(laptop.getReviews());
//                laptopRepository.save(laptop);
//                reviewRepository.save(review);
//                reviewRepository.delete(review);
//            }
//        }
//        List<Laptop> laptops = laptopRepository.findAllByReviewsContaining()
//        reviewRepository.deleteAll(reviews);

        if (person != null) {
            peopleRepository.delete(person);
        }
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

    public void updateResetPasswordToken(String token, String email){
        Person customer = peopleRepository.findByEmail(email);
        if (customer != null) {
            customer.setResetPasswordToken(token);
            peopleRepository.save(customer);
        }
    }

    public Person getByResetPasswordToken(String token) {
        return peopleRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(Person person, String newPassword) {
        person.setPassword(passwordEncoder.encode(newPassword));
        person.setResetPasswordToken(null);
        peopleRepository.save(person);
    }

    public void registerAdmin(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.getRoles().add(Role.ROLE_ADMIN);
        person.setActive(true);
        peopleRepository.save(person);
    }
}
