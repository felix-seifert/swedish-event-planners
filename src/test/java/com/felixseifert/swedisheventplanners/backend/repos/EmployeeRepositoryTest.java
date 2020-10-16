package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.Employee;
import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    public void setDatabase() {
        employee1 = new Employee();
        employee1.setName("Karl");
        employee1.addRole(Role.ACCOUNTANT);
        employee2 = new Employee();
        employee2.setName("Carla");
        employee2.addRole(Role.ADMINISTRATION_MANAGER);

        testEntityManager.persist(employee1);
        testEntityManager.persist(employee2);
        testEntityManager.flush();
    }

    @Test
    public void findAllTest() {
        List<Employee> employeesActual = employeeRepository.findAll();
        assertEquals(List.of(employee1, employee2), employeesActual);
    }

    @Test
    public void findByIdTest() {
        Optional<Employee> employeeActual = employeeRepository.findById(employee1.getId());
        Optional<Employee> employeeActualEmpty = employeeRepository.findById(9999L);

        assertTrue(employeeActual.isPresent());
        assertEquals(employee1, employeeActual.get());
        assertFalse(employeeActualEmpty.isPresent());
    }

    @Test
    public void findByRoleTest() {

    }

    @Test
    public void saveTest() {
        Employee employeeToSave = new Employee();
        employeeToSave.setName("Daniel Dudley");
        employeeToSave.addRole(Role.CUSTOMER_SERVICE_OFFICER);

        Employee employeeActual = employeeRepository.save(employeeToSave);
        List<Employee> employeeListActual = employeeRepository.findAll();
        boolean employeeExists = employeeListActual.contains(employeeToSave);

        assertEquals(employeeToSave, employeeActual);
        assertTrue(employeeExists);
        assertTrue(employeeActual.getId() != null);
        assertEquals(3, employeeListActual.size());
        assertEquals(employeeToSave, employeeListActual.get(2));
    }

    @Test
    public void deleteTest() {
        employeeRepository.delete(employee1);

        List<Employee> employeeListActual = employeeRepository.findAll();
        boolean clientExists = employeeListActual.contains(employee1);

        assertFalse(clientExists);
        assertEquals(1, employeeListActual.size());
    }
}
