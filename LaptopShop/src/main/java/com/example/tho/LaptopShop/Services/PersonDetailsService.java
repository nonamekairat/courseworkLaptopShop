package com.example.tho.LaptopShop.Services;


import com.example.tho.LaptopShop.models.Person;
import com.example.tho.LaptopShop.repositories.PeopleRepository;
import com.example.tho.LaptopShop.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(username);
        if(person.isEmpty())
            throw new UsernameNotFoundException("User no found!");

        return new PersonDetails(person.get());
    }
}
