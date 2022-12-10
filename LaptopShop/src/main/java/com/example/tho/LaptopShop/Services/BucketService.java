package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Bucket;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.repositories.BucketRepository;
import com.example.tho.LaptopShop.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BucketService {

    private final BucketRepository bucketRepository;
    private final PeopleRepository peopleRepository;

    public void save(Bucket bucket){
        bucketRepository.save(bucket);
    }

    public void createBucket(Person person){
        if(person.getBucket() == null) {
            Bucket bucket = new Bucket();
            bucket.setUser(person);
            person.setBucket(bucket);
            peopleRepository.save(person);
        }
    }

//    public Bucket getBucketByPerson(Person person){
//        bucketRepository.findById(person.getBucket().getId());
//    }

}
