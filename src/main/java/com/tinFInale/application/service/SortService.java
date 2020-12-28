package com.tinFInale.application.service;

import com.tinFInale.application.model.EmpDept;
import com.tinFInale.application.repository.EmpDeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


@Service
public class SortService {


    private final EmpDeptRepository empDeptRepository;

    @Autowired
    public SortService(EmpDeptRepository empDeptRepository) {
        this.empDeptRepository = empDeptRepository;
    }

    public <T> T sortedObj(EmpDept empDept, String sortProperty) {
        if (sortProperty.contains("employee")) {

            var employee = empDept.getEmployee();

            if (sortProperty.contains("name")) {
                return (T) employee.getName();

            } else if (sortProperty.contains("lastName")) {
                return (T) (employee.getLastName());
            }

        } else if (sortProperty.contains("department")) {
            var department = empDept.getDepartment();

            if (sortProperty.contains("name")) {
                return (T) (department.getDepartmentName());

            } else if (sortProperty.contains("budget")) {
                return (T) (department.getBugdet());

            } else if (sortProperty.contains("manager")) {
                return (T) (department.getDepartmentManager());
            }
        }
        return null;
    }


    public List<EmpDept> fuzzySearch(String phrase, Integer page, Integer recordsPerPage) {

        var preparedPhrase = phrase.toLowerCase().trim();

        //SEARCH PHRASE COMES FROM ANGULAR FRONT
        if (preparedPhrase.equals("undefined") || phrase.equals(""))
            return new ArrayList<>(empDeptRepository.findAll(PageRequest.of(page,recordsPerPage)));
        else
            return empDeptRepository.findBySearchPhrase(preparedPhrase,PageRequest.of(page,recordsPerPage));
    }

}
