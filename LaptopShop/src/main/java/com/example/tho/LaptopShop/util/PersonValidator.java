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

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(person.getId() != null) {
            Person person1 = peopleService.getUserById(person.getId());
            if (person1.getUsername().equals(person.getUsername()) && person1.getEmail().equals(person.getEmail()))
                return;
            if (!person1.getUsername().equals(person.getUsername()) && !person1.getEmail().equals(person.getEmail())) {
                if (peopleService.usernameExist(person.getUsername()).isPresent()
                        && !(peopleService.getEmail(person.getEmail()) == null)){
                    errors.rejectValue("username", "",
                            "this username is already used");
                    errors.rejectValue("email", "",
                            "this email is already used");
                    return;
                }
                else if (peopleService.usernameExist(person.getUsername()).isPresent()) {
                    errors.rejectValue("username", "",
                            "this username is already used");
                    return;
                } else if (!(peopleService.getEmail(person.getEmail()) == null)) {
                    errors.rejectValue("email", "",
                            "this email is already used");
                    return;
                } else {
                    return;
                }
            } else if (!person1.getUsername().equals(person.getUsername()) && person1.getEmail().equals(person.getEmail())) {
                if (peopleService.usernameExist(person.getUsername()).isPresent()) {
                    errors.rejectValue("username", "",
                            "this username is already used");

                }
            } else if (!person1.getEmail().equals(person.getEmail()) && person1.getUsername().equals(person.getUsername())) {
                if (!(peopleService.getEmail(person.getEmail()) == null)) {
                    errors.rejectValue("email", "",
                            "this email is already used");
                }
            }
            return;
        }

        if(peopleService.usernameExist(person.getUsername()).isEmpty()
                && peopleService.getEmail(person.getEmail()) == null)
            return;
        else if (peopleService.usernameExist(person.getUsername()).isPresent()) {
            errors.rejectValue("username","",
                    "this username is already used");
            return;
        } else if (!(peopleService.getEmail(person.getEmail()) == null)) {
            errors.rejectValue("email","",
                    "this email is already used");
            return;
        }
        errors.rejectValue("username","",
                "this username is already used");
        errors.rejectValue("email","",
                "this email is already used");



    }
}
