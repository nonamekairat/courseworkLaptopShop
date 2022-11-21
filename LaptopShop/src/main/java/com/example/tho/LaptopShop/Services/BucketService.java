package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Bucket;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.repositories.BucketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final BucketRepository bucketRepository;

    public void save(Bucket bucket){
        bucketRepository.save(bucket);
    }

//    public Bucket getBucketByPerson(Person person){
//        bucketRepository.findById(person.getBucket().getId());
//    }

}
