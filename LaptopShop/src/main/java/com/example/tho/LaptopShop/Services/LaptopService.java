package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Category;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.repositories.CategoryRepository;
import com.example.tho.LaptopShop.repositories.LaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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


    public List<Laptop> listLaptopUser(String title,String categoryName){
//        if(categoryName.equals("category")) categoryName = null;
        Category category = null;
        if(categoryName != null) {
            if(!categoryName.equals("category")){
                category = categoryRepository.findByName(categoryName);
            }
        }

        if(title != null && category != null){
            return laptopRepository.findAllByModelContainsAndAmountGreaterThanAndCategoriesContains(title, 0,category);
        }
        else if(title != null){
            return laptopRepository.findAllByModelContainsAndAmountGreaterThan(title, 0);
        }
        else if(category != null){
            return laptopRepository.findAllByCategoriesContainsAndAmountGreaterThan(category, 0);
        }
        else {
            return laptopRepository.findAllByAmountGreaterThan(0);
        }
    }
    public List<Laptop> listLaptopAdmin(String title,String categoryName){
        Category category = null;
        if(categoryName != null) {
            if(!categoryName.equals("category")){
                category = categoryRepository.findByName(categoryName);
            }
        }
        if(title != null && category != null){
            return laptopRepository.findAllByModelContainsAndCategoriesContains(title, category);
        }
        else if(title != null){
            return laptopRepository.findAllByModelContains(title);
        }
        else if(category != null){
            return laptopRepository.findAllByCategoriesContains(category);
        }
        else {
            return laptopRepository.findAll();
        }

//
//        if(title != null) return laptopRepository.findAllByModelContains(title)
//                .stream()
////                .filter(laptop -> laptop.getAmount() > 0)
//                .toList();
//        return laptopRepository.findAll()
//                .stream()
////                .filter(laptop -> laptop.getAmount() > 0)
//                .toList();
    }
//
//    public boolean checkLaptopIsEnough(List<Laptop> laptops){
//
//    }

    public Laptop getLaptopById(Long id) {
        return Objects.requireNonNull(laptopRepository.findById(id).orElse(null));
    }

    public void save(Laptop laptop, MultipartFile file, String... categoryNames) throws IOException {


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


}
