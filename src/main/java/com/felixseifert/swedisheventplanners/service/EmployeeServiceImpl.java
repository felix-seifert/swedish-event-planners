package com.felixseifert.swedisheventplanners.service;

import com.felixseifert.swedisheventplanners.exceptions.BlankValueNotAllowedException;
import com.felixseifert.swedisheventplanners.exceptions.EntityAlreadyExistsException;
import com.felixseifert.swedisheventplanners.model.Employee;
import com.felixseifert.swedisheventplanners.repos.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        log.debug("Return {} employees", employees.size());
        return employees;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if(employee.isEmpty()) {
            throw new EntityNotFoundException("Employee with id " + id + " does not exist");
        }
        log.debug("Return client {}", employee.get());
        return employee.get();
    }

    @Override
    public Employee postEmployee(Employee employee) {
        if(employee.getId() != null) {
            throw new EntityAlreadyExistsException("Given employee already has id");
        }

        checkBlankVariables(employee);

        Employee newEmployee = employeeRepository.save(employee);
        log.info("New employee {} created", newEmployee);
        return newEmployee;
    }

    @Override
    public Employee putEmployee(Employee employee) {
        if(employee.getId() == null) {
            throw new EntityNotFoundException("Employee does not have id");
        }

        checkBlankVariables(employee);

        Employee updatedEmployee = employeeRepository.save(employee);
        log.info("Employee {} updated", updatedEmployee);
        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(Employee employee) {
        if(employee.getId() == null) {
            throw new EntityNotFoundException("Employee does not have id");
        }

        employeeRepository.delete(employee);
        log.info("Employee {} deleted", employee);
    }

    private void checkBlankVariables(Employee employee) {
        if(StringUtils.isBlank(employee.getName())) {
            throw new BlankValueNotAllowedException("Employee name is blank");
        }
    }
}
