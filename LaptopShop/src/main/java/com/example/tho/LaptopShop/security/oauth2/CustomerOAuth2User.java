package com.example.tho.LaptopShop.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

// nedodelannoe govno


@RequiredArgsConstructor
public class CustomerOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;


    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getUsername(){
        return oAuth2User.getAttribute("name");
    }
    public String getPhoneNumber(){
        return oAuth2User.getAttribute("phone number");
    }
    public String getLastName(){
        return oAuth2User.getAttribute("last name");
    }
    public String getAddress(){
        return oAuth2User.getAttribute("address");
    }
}
