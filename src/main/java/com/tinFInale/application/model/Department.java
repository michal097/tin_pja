package com.tinFInale.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Department {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departId;
    @NotNull
    private String departmentName;
    @Min(value = 1500)
    private Double bugdet;
    @NotNull
    private String departmentManager;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    @JsonIgnore
    Set<EmpDept> depts;

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName.substring(0, 1).toUpperCase() + departmentName.substring(1);
    }

    public void setDepartmentManager(String departmentManager) {
        this.departmentManager = departmentManager.substring(0, 1).toUpperCase() + departmentManager.substring(1);
    }
}
