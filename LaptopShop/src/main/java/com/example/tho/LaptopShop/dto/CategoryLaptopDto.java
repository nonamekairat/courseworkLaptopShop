package com.example.tho.LaptopShop.dto;


import com.example.tho.LaptopShop.models.Category;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Data
public class CategoryLaptopDto {

    private List<Category> categories;

    public void addCategory(Category category){
        categories.add(category);
    }


}
