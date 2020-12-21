package com.tinFInale.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@Builder
public class EmpDept {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long empDeptId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(columnDefinition = "employee_id")
            @JsonUnwrapped
    @JsonIgnore
    Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(columnDefinition = "department_id")
            @JsonUnwrapped
    @JsonIgnore
    Department department;

    LocalDate hiredDate;

    public EmpDept(){
        this.hiredDate = LocalDate.now();
    }

    public EmpDept(Employee employee, Department department){
        this.employee=employee;
        this.department=department;
    }

}
