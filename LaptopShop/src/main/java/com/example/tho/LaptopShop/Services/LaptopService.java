package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.*;
import com.example.tho.LaptopShop.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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
    private final OrderDetailsRepository orderDetailsRepository;
    private final BucketRepository bucketRepository;
    private final FavoritesRepository favoritesRepository;


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

        if(laptop.getId() != null){
            Laptop oldLaptop = laptopRepository.findById(laptop.getId()).orElse(null);
            List<Review> reviews = oldLaptop.getReviews();
            laptop.setReviews(reviews);
            laptop.setScore(oldLaptop.getScore());
            laptop.setDateOfCreated(oldLaptop.getDateOfCreated());

        }

        if(laptop.getImageName().equals("")) laptop.setImageName(null);
        List<Category> categories = Arrays.stream(categoryNames).collect(Collectors.toSet()).stream()
                .filter(Objects::nonNull).filter(c -> !c.equals("category")).map(categoryRepository::findByName).toList();
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

    @Transactional
    public void deleteLaptop(Laptop laptop){
        System.out.println(laptop.getId());

        notificationRepository.deleteAllByLaptop(laptop);
        orderDetailsRepository.deleteAllByLaptop(laptop);
        List<Bucket> buckets = bucketRepository.findBucketsByLaptopsContains(laptop);
        for (Bucket bucket : buckets) {
            bucket.removeLaptop(laptop);
            Person person = peopleRepository.findByBucket(bucket);
            if(person != null) {
                person.setBucket(bucket);
                peopleRepository.save(person);
            }
        }
        List<Favorites> favorites = favoritesRepository.findFavoritesByLaptopsContains(laptop);
        for (Favorites favorite : favorites) {
            favorite.removeLaptop(laptop);
            Person person = peopleRepository.findByFavorites(favorite);
            if(person != null) {
                person.setFavorites(favorite);
                peopleRepository.save(person);
            }

        }

        laptopRepository.delete(laptop);
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
        try{
            laptop.setScore(laptop.getReviews().stream().mapToDouble(Review::getScore).average().getAsDouble());
        } catch (NoSuchElementException e){
            laptop.setScore(0);
        }

        laptopRepository.save(laptop);
    }

}
