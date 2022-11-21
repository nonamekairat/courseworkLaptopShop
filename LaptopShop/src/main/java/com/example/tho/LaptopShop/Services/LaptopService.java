package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.repositories.LaptopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LaptopService {

    @Value("${upload.path}")
    private String uploadPath;
    private final LaptopRepository laptopRepository;


    public List<Laptop> listLaptopUser(String title){
        if(title != null) return laptopRepository.findByModelContains(title)
                .stream()
                .filter(laptop -> laptop.getAmount() > 0)
                .toList();
        return laptopRepository.findAll()
                .stream()
                .filter(laptop -> laptop.getAmount() > 0)
                .toList();
    }
    public List<Laptop> listLaptopAdmin(String title){
        if(title != null) return laptopRepository.findByModelContains(title)
                .stream()
//                .filter(laptop -> laptop.getAmount() > 0)
                .toList();
        return laptopRepository.findAll()
                .stream()
//                .filter(laptop -> laptop.getAmount() > 0)
                .toList();
    }
//
//    public boolean checkLaptopIsEnough(List<Laptop> laptops){
//
//    }

    public Laptop getLaptopById(Long id) {
        return Objects.requireNonNull(laptopRepository.findById(id).orElse(null));
    }

    public void save(Laptop laptop, MultipartFile file) throws IOException {

        if(!file.isEmpty()){
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
}
