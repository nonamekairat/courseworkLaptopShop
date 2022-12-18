package com.example.tho.LaptopShop.repositories;


import com.example.tho.LaptopShop.models.Favorites;
import com.example.tho.LaptopShop.models.Laptop;
import com.example.tho.LaptopShop.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoritesRepository extends JpaRepository<Favorites, Long> {
    Optional<Favorites> findByUser(Person person);
    void deleteAllByUser(Person person);
    void deleteByUser(Person person);

    List<Favorites> findFavoritesByLaptopsContains(Laptop laptop);
}
