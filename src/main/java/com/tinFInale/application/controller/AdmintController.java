package com.tinFInale.application.controller;

import com.tinFInale.application.model.Department;
import com.tinFInale.application.model.EmpDept;
import com.tinFInale.application.model.Employee;
import com.tinFInale.application.repository.DepartmentRepository;
import com.tinFInale.application.repository.EmpDeptRepository;
import com.tinFInale.application.repository.EmployeeRepository;
import com.tinFInale.application.service.SortService;
import com.tinFInale.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/admin")
public class AdmintController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmpDeptRepository empDeptRepository;
    private final UserRepository userRepository;
    private final SortService sortService;

    @Autowired
    public AdmintController(EmpDeptRepository empDeptRepository,
                            EmployeeRepository employeeRepository,
                            DepartmentRepository departmentRepository,
                            UserRepository userRepository,
                            SortService sortService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.empDeptRepository = empDeptRepository;
        this.userRepository = userRepository;
        this.sortService = sortService;


    }

    @GetMapping("/getAllEmployeesWithDepartments")
    public List<EmpDept> getAllEmployeesWithDepartments() {
        return empDeptRepository.findAll();
    }

    @GetMapping("search/{phrase}")
    public Set<EmpDept> getAllBySearchOhrase(@PathVariable String phrase) {
        return sortService.fuzzySearch(phrase);
    }

    @GetMapping("/getAllEmployeesWithDepartments/sortBy/{sort}/{order}/{phrase}")
    public List<EmpDept> gelAllEmployeesWithSort(@PathVariable("sort") String sortBy, @PathVariable("order") String sortOrder, @PathVariable String phrase) {

        var sortedEmployeeDeptList = sortService.fuzzySearch(phrase)
                .stream()
                .sorted(Comparator.comparing(x -> {
                    return sortService.sortedObj(x, sortBy);
                })).collect(toList());

        if (sortOrder.equals("asc")) {
            return sortedEmployeeDeptList;
        } else
            Collections.reverse(sortedEmployeeDeptList);
        return sortedEmployeeDeptList;
    }

    @GetMapping("/getEmpDeptById/{id}")
    public EmpDept getEmpDeptById(@PathVariable Long id) {
        log.info("details of user id: " + id);
        return empDeptRepository.findById(id).orElse(new EmpDept());
    }

    @GetMapping("/getEmployeeById/{empId}")
    public Employee getEmployeeById(@PathVariable("empId") Long id) {
        return employeeRepository.findById(id).orElseThrow(IllegalAccessError::new);
    }

    @PostMapping("/saveEmp")
    @Transactional
    public Employee saveEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult) throws Exception {
        employee.setEployedDate(LocalDate.now());
        if (bindingResult.hasErrors()) {
            log.error("błędny employee!!");
            log.error(bindingResult.getFieldErrors().toString());
            throw new IllegalArgumentException("failed save employee");

        }
        if (employeeRepository.findByUsername(employee.getUsername()).isPresent()) {

            throw new IllegalArgumentException("This username already exists in database");
        }
        var dep = departmentRepository.findOneByDepartmentName(employee.getDepartment()).orElse(new Department());
        var newEmpDepartment = new EmpDept(employee, dep);
        newEmpDepartment.setHiredDate(LocalDate.now());
        empDeptRepository.save(newEmpDepartment);

        return employeeRepository.save(employee);
    }

    @GetMapping("/getDepartmentNames")
    @Transactional
    public Set<String> departmentNames() {
        return departmentRepository.findAll()
                .stream()
                .map(Department::getDepartmentName)
                .collect(toSet());
    }

    @GetMapping("/getDepartmentNamesExceptHavingByUser/{id}")
    public List<String> getDepartmentNamesExceptHavingByUser(@PathVariable Long id) {
        var empDepts = empDeptRepository.findAllByEmployee(employeeRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new));
        var employeeDepartments = empDepts.stream()
                .map(EmpDept::getDepartment)
                .collect(toList());

        return departmentRepository.findAll()
                .stream()
                .filter(department -> !employeeDepartments.contains(department))
                .map(Department::getDepartmentName)
                .collect(toList());
    }

    @PostMapping("/saveDep")
    public Department saveDepartment(@Valid @RequestBody Department department, BindingResult bindingResult) {
        var depart = departmentRepository.findAll()
                .stream()
                .filter(d -> d.getDepartmentName().equals(department.getDepartmentName()))
                .findAny();

        if (depart.isPresent()) {
            throw new IllegalArgumentException("already there is dept like this one :c");
        }
        if (bindingResult.hasErrors()) {
            log.error("błędny departament");
        }
        return departmentRepository.save(department);
    }


    @PutMapping("/updateEmpDept/{id}")
    public Employee updateEmpDept(@Valid @RequestBody Employee employee, @PathVariable Long id, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.error("errors occured while putting data -> see CrudRestController.class");
            throw new IllegalArgumentException("Errors in put method");
        }
        return employeeRepository.findById(id).map(
                e -> {
                    e.setName(employee.getName());
                    e.setLastName(employee.getLastName());
                    e.setSalary(employee.getSalary());
                    e.setAge(employee.getAge());

                    log.info("Employee with" + id + "has been updated");

                    departmentRepository.findOneByDepartmentName(employee.getDepartment())
                            .ifPresent(depart -> {
                                var empDept = new EmpDept(e, depart);
                                empDept.setHiredDate(LocalDate.now());
                                empDeptRepository.save(empDept);
                            });
                    return employeeRepository.save(e);
                }).orElseGet(() -> {
                    employee.setEmployeeId(id);
                    return employeeRepository.save(employee);
                }
        );
    }

    @DeleteMapping("/delete/{id}")
    public void deleteEmp(@PathVariable Long id) {

        //IN CASE OF DELETE LAST RELATION WITH EMPLOYEE
        var emp = empDeptRepository.findById(id).orElseThrow();
        var myEmp = emp.getEmployee();

        empDeptRepository.delete(emp);
        log.warn("Employee with id: " + id + " has been deleted");

        var stillEmployeed = (long) empDeptRepository.findAllByEmployee(myEmp).size();
        if (stillEmployeed == 0) {
            employeeRepository.delete(myEmp);
            log.warn("It was last relation with this employee, now this emp is deleted!");
            userRepository.deleteByUsername(myEmp.getUsername());
            log.warn("And has been deleted from users");

        }
    }

    @GetMapping("/getAllEmployees")
    public List<Employee> allEmps() {
        return employeeRepository.findAll();
    }

    @GetMapping("/getAllDepartments")
    public List<Department> allDepts() {
        return departmentRepository.findAll();
    }

}
