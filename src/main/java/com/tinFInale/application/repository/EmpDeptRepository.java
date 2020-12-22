package com.tinFInale.application.repository;


import com.tinFInale.application.model.EmpDept;
import com.tinFInale.application.model.Employee;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmpDeptRepository extends CrudRepository<EmpDept, Long> {
    List<EmpDept> findAll();

    List<EmpDept> findAll(Sort sort);

    List<EmpDept> findAllByEmployee(Employee employee);


}
