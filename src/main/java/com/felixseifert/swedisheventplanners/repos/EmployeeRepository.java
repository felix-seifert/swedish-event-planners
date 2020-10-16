package com.felixseifert.swedisheventplanners.repos;

import com.felixseifert.swedisheventplanners.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameIgnoreCase(String name);
}
