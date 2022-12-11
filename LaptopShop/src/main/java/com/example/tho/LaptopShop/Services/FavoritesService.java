package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Favorites;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.repositories.FavoritesRepository;
import com.example.tho.LaptopShop.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final PeopleRepository peopleRepository;
    private final FavoritesRepository favoritesRepository;

    public void save(Laptop laptop, Person person) {
        Favorites favorites = person.getFavorites();
        if(favorites == null) favorites = new Favorites();
        List<Laptop> laptops = favorites.getLaptops();
        if(laptops == null) laptops = new ArrayList<>();
        laptops.add(laptop);
        favorites.setLaptops(laptops);
        person.setFavorites(favorites);
        peopleRepository.save(person);
    }
    public void remove(Laptop laptop, Person person) {
        Favorites favorites = person.getFavorites();
        List<Laptop> laptops = favorites.getLaptops();
        laptops.remove(laptop);
        favorites.setLaptops(laptops);
        person.setFavorites(favorites);
        peopleRepository.save(person);
    }

    public boolean isPersonHaveInFavorites(Person person, Laptop laptop) {
        boolean have = false;
        try {
            Favorites favorites = person.getFavorites();
            List<Laptop> laptops = favorites.getLaptops();

            for (Laptop value : laptops) {
                if (Objects.equals(value.getId(), laptop.getId())) {
                    have = true;
                    break;
                }
            }
        } catch (NullPointerException e){
        }
        return have;
    }


    public void createfavorites(Person person) {
        Favorites favorites = favoritesRepository.findByUser(person).orElse(new Favorites());
        if(favorites.getLaptops() == null) favorites.setLaptops(new ArrayList<>());
        person.setFavorites(favorites);
        peopleRepository.save(person);
    }
}
