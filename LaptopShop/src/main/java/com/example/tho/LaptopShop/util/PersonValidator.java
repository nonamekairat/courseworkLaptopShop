package com.example.tho.LaptopShop.util;


import com.example.tho.LaptopShop.Services.PeopleService;
import com.example.tho.LaptopShop.models.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@RequiredArgsConstructor
@Component
public class PersonValidator implements Validator {

    private final PeopleService peopleService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }
    public void validateChange(Person person, Errors errors,Person newPerson){


        if(!newPerson.getEmail().equals(person.getEmail())){
            if (!(peopleService.getByEmail(person.getEmail()) == null)) {
                errors.rejectValue("email","",
                        "this email is already used");
            }
        }
        if(!newPerson.getUsername().equals(person.getUsername())){
            if (peopleService.usernameExist(person.getUsername()).isPresent()) {
                errors.rejectValue("username","",
                        "this username is already used");
            }
        }
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;


        if (peopleService.usernameExist(person.getUsername()).isPresent()) {
            errors.rejectValue("username","",
                    "this username is already used");
        }
        if (!(peopleService.getByEmail(person.getEmail()) == null)) {
            errors.rejectValue("email","",
                    "this email is already used");
        }






    }
}
