package com.app.Bank.controller;

import com.app.Bank.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetAuth {
    public static User getAuth(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
