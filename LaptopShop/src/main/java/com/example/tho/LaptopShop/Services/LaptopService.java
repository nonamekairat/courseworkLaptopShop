package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.*;
import com.example.tho.LaptopShop.repositories.CategoryRepository;
import com.example.tho.LaptopShop.repositories.LaptopRepository;
import com.example.tho.LaptopShop.repositories.NotificationRepository;
import com.example.tho.LaptopShop.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaptopService {

    @Value("${upload.path}")
    private String uploadPath;
    private final LaptopRepository laptopRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationRepository notificationRepository;
    private final PeopleRepository peopleRepository;

    public List<Laptop> listLaptopAdmin(String title, String categoryName, List<Sort.Order> orders){

        Category category = null;
        if(categoryName != null) {
            if(!categoryName.equals("category")){
                category = categoryRepository.findByName(categoryName);
            }
        }
        if(title != null && category != null){
            return laptopRepository.findAllByModelContainsAndCategoriesContains(title, category,Sort.by(orders));
        }
        else if(title != null){
            return laptopRepository.findAllByModelContains(title,Sort.by(orders));
        }
        else if(category != null){
            return laptopRepository.findAllByCategoriesContains(category,Sort.by(orders));
        }
        else {
            return laptopRepository.findAll(Sort.by(orders));
        }

    }
//
//    public boolean checkLaptopIsEnough(List<Laptop> laptops){
//
//    }

    public Laptop getLaptopById(Long id) {
        return Objects.requireNonNull(laptopRepository.findById(id).orElse(null));
    }

    public void save(Laptop laptop, MultipartFile file, String... categoryNames) throws IOException {

        if(laptop.getImageName().equals("")) laptop.setImageName(null);
        List<Category> categories = Arrays.stream(categoryNames).collect(Collectors.toSet()).stream()
                .filter(Objects::nonNull).map(categoryRepository::findByName).toList();
        laptop.setCategories(categories);


        if(!file.isEmpty() && file.getSize() > 0){
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists())
                uploadDir.mkdir();
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            laptop.setImageName(resultFilename);
        }
        laptopRepository.save(laptop);

        List<Notification> notifications = notificationRepository.findAllByLaptop(laptop);

        for (Notification notification : notifications) {
            if (notification.getLaptop().getAmount() > 0) {
                notification.setLaptopUpdate(true);
                Person person = notification.getPerson();
                person.setNotificationCount(person.getNotificationCount() + 1);
                peopleRepository.save(person);
                notificationRepository.save(notification);
            }
        }

    }
    public void save(Laptop laptop){
        laptopRepository.save(laptop);
    }

    public void deleteCategory(Long id, Long laptopId) {
        Laptop laptop = laptopRepository.findById(laptopId).orElse(new Laptop());
        List<Category> categories = laptop.getCategories();
        Category categoryToRemove = categoryRepository.findById(id).get();
        categories.remove(categoryToRemove);
        laptop.setCategories(categories);
        save(laptop);
    }

    public void addCategory(Long laptopId, String name) {
        Laptop laptop = laptopRepository.findById(laptopId).orElse(new Laptop());
        List<Category> categories = laptop.getCategories();
        Category categoryToAdd = categoryRepository.findByName(name);
        categories.add(categoryToAdd);
        laptop.setCategories(categories);
        save(laptop);
    }


    public void setAverageScore(Laptop laptop){
        laptop.setScore(laptop.getReviews().stream().mapToDouble(Review::getScore).average().getAsDouble());
        laptopRepository.save(laptop);
    }

}
