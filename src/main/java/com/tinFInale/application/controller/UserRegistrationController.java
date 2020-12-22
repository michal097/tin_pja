package com.tinFInale.application.controller;

import com.tinFInale.application.model.Employee;
import com.tinFInale.application.repository.EmployeeRepository;
import com.tinFInale.security.model.User;
import com.tinFInale.security.repository.UserRepository;
import com.tinFInale.security.service.UserCreateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@CrossOrigin
public class UserRegistrationController {

    private final EmployeeRepository employeeRepository;
    private final UserCreateService userCreateService;
    private final UserRepository userRepository;

    @Autowired
    public UserRegistrationController(EmployeeRepository employeeRepository,
                                      UserCreateService userCreateService,
                                      UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userCreateService = userCreateService;
        this.userRepository = userRepository;

    }

    @PostMapping("/createAndSaveUser")
    public User createUser(@RequestBody User user) {
        var username = employeeRepository.findAll()
                .stream()
                .map(Employee::getUsername)
                .filter(u -> u.equals(user.getUsername()))
                .findAny();

        var isAlreadyPresent = userRepository.findByUsername(user.getUsername());

        if (username.isEmpty() || isAlreadyPresent != null) {
            String str = "Admin didnt create employee with this username: " + user.getUsername();
            log.error(str);
            throw new UsernameNotFoundException(str);
        }

        log.info("User has been created with name: " + user.getUsername() + " with ROLE_USER");
        userCreateService.addWithDefaultRole(user, "ROLE_USER");
        return userRepository.save(user);

    }

    @GetMapping("/user")
    public User user() {
        System.out.println("username");
        userRepository.findAll().forEach(System.out::println);
        return userRepository.findByUsername("fdsa");
    }

    @GetMapping("/test") //SHOWS ROLE
    public String str() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }

    @GetMapping("/username") //SHOWS USERNAME
    public String usrs() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
