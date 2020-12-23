package com.tinFInale.application.service;

import com.tinFInale.application.model.EmpDept;
import com.tinFInale.application.repository.EmpDeptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

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


    public Set<EmpDept> fuzzySearch(String phrase, Integer page) {

        var preparedPhrase = phrase.toLowerCase().trim();

        var findNames = searchLogic(s -> s.getEmployee().getName().toLowerCase().contains(preparedPhrase));
        var findLastName = searchLogic(s -> s.getEmployee().getLastName().toLowerCase().contains(preparedPhrase));
        var findDepart = searchLogic(s -> s.getDepartment().getDepartmentName().toLowerCase().contains(preparedPhrase));

        //SEARCH PHRASE COMES FROM ANGULAR FRONT
        if (preparedPhrase.equals("undefined"))
            return new HashSet<>(empDeptRepository.findAll(PageRequest.of(page,5)));
        else
            return Stream.of(findNames, findLastName, findDepart).flatMap(Collection::stream).collect(toSet());
    }

    public Set<EmpDept> searchLogic(Predicate<EmpDept> pred) {
        return empDeptRepository.findAll()
                .stream()
                .filter(pred)
                .collect(toSet());
    }
}
