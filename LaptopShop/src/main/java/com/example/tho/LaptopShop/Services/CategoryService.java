package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.dto.CategoryLaptopDto;
import com.example.tho.LaptopShop.models.Category;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.repositories.CategoryRepository;
import com.example.tho.LaptopShop.repositories.LaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final LaptopRepository laptopRepository;


    public void addCategory(Category category){
        categoryRepository.save(category);
    }
    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }
    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id).orElse(null);
        List<Laptop> laptops = laptopRepository.findAllByCategoriesContains(category);
        for (Laptop laptop : laptops) {
            List<Category> categories = laptop.getCategories();
            categories.remove(category);
            laptop.setCategories(categories);
            laptopRepository.save(laptop);
        }
        categoryRepository.deleteById(id);
    }

    public CategoryLaptopDto addCategoryToLaptop(){
        CategoryLaptopDto categoryLaptopDto = new CategoryLaptopDto();

        for (int i = 1; i <= 3; i++) {
            categoryLaptopDto.addCategory(new Category());
        }
        return categoryLaptopDto;
    }


}
