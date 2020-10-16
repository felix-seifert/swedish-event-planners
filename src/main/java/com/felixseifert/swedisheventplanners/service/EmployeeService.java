package com.felixseifert.swedisheventplanners.service;

import com.felixseifert.swedisheventplanners.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    Employee postEmployee(Employee employee);

    Employee putEmployee(Employee employee);

    void deleteEmployee(Employee employee);
}
