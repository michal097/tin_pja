package com.tinFInale.application.repository;


import com.tinFInale.application.model.EmpDept;
import com.tinFInale.application.model.Employee;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmpDeptRepository extends CrudRepository<EmpDept, Long> {
    List<EmpDept> findAll();
    List<EmpDept> findAll(Pageable pageable);

    List<EmpDept> findAllByEmployee(Employee employee);

    @Query("select emp from EmpDept emp where " +
            "lower(emp.employee.name) like %:phrase% or " +
            "lower(emp.employee.lastName) like %:phrase% or " +
            "lower(emp.department.departmentName) like %:phrase% or " +
            "lower(emp.department.departmentManager) like %:phrase%")

    List<EmpDept> findBySearchPhrase(String phrase, Pageable pageable);

}
