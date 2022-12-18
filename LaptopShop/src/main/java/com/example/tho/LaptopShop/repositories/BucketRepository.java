package com.example.tho.LaptopShop.repositories;

import com.example.tho.LaptopShop.models.Bucket;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BucketRepository extends JpaRepository<Bucket, Long> {
    void deleteBucketByUser(Person user);

    List<Bucket> findBucketsByLaptopsContains(Laptop laptop);
}
