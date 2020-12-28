package com.tinFInale.application.controller;

import com.tinFInale.application.model.Department;
import com.tinFInale.application.model.EmpDept;
import com.tinFInale.application.model.Employee;
import com.tinFInale.application.repository.EmpDeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class EmployeeController {


    private final EmpDeptRepository empDeptRepository;

    @Autowired
    EmployeeController(EmpDeptRepository empDeptRepository) {
        this.empDeptRepository = empDeptRepository;
    }

    public Stream<EmpDept> getAuthenticatedName() {
        return empDeptRepository.findAll()
                .stream()
                .filter(e -> e.getEmployee()
                        .getUsername()
                        .equals(SecurityContextHolder.getContext()
                                .getAuthentication()
                                .getName()));
    }

    @GetMapping("/getAllEmps")
    public Set<Employee> getAllEmpsWithDepart() {

        Set<Department> depts = getAuthenticatedName().map(EmpDept::getDepartment).collect(Collectors.toSet());

        Set<Employee> list = new HashSet<>();
        for (EmpDept e : empDeptRepository.findAll()) {
            if (depts.contains(e.getDepartment())) {
                list.add(e.getEmployee());
            }
        }
        return list;
    }

    @GetMapping("/getMyDepts")
    public Set<Department> departments() {
        return getAuthenticatedName().map(EmpDept::getDepartment).collect(Collectors.toSet());
    }

}
