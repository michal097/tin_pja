package com.tinFInale.application.service;

import com.tinFInale.application.model.EmpDept;
import org.springframework.stereotype.Service;


@Service
public class SortService {

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

}
