package com.tinFInale.application.controller;

import com.tinFInale.security.model.User;
import com.tinFInale.security.repository.UserRepository;
import com.tinFInale.security.service.UserCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/test")
public class Testing {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserCreateService userCreateService;

    @GetMapping("init")
    public String init() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");

        userCreateService.addWithDefaultRole(user, "ROLE_ADMIN");
        userRepository.save(user);

        return "hello there";

    }

}