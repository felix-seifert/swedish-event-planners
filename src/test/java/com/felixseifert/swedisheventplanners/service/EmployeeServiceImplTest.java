package com.felixseifert.swedisheventplanners.service;

import com.felixseifert.swedisheventplanners.exceptions.BlankValueNotAllowedException;
import com.felixseifert.swedisheventplanners.exceptions.EntityAlreadyExistsException;
import com.felixseifert.swedisheventplanners.model.Employee;
import com.felixseifert.swedisheventplanners.model.enums.Role;
import com.felixseifert.swedisheventplanners.repos.EmployeeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class EmployeeServiceImplTest {

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @MockBean
    private EmployeeRepository employeeRepository;

    private static Employee employee1;
    private static Employee employee2;
    private static Employee newEmployee;

    @BeforeAll
    public static void setup() {
        employee1 = new Employee();
        employee1.setName("Karl Johansson");
        employee1.addRole(Role.CLIENT_VIEWER); //role 1
        employee1.setId(1L);
        employee2 = new Employee();
        employee2.setName("Carla");
        employee2.addRole(Role.CLIENT_VIEWER); //role 1
        employee2.addRole(Role.ADMINISTRATION_MANAGER); //role 2
        employee2.setId(2L);

        newEmployee = new Employee();
        newEmployee.setName("Joe New");
        newEmployee.addRole(Role.CUSTOMER_SERVICE_OFFICER); //Role 3
    }
    @Test
    public void getAllEmployeesTest() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));
        List<Employee> actualEmployees = employeeServiceImpl.getAllEmployees();
        assertEquals(List.of(employee1, employee2), actualEmployees);
    }

    @Test
    public void getEmployeeByIdTest() {
        when(employeeRepository.findById(employee1.getId())).thenReturn(Optional.of(employee1));
        Employee actual = employeeServiceImpl.getEmployeeById(employee1.getId());
        assertEquals(employee1, actual);
    }

    @Test
    public void getEmployeeByIdTest_noEmployeeFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> employeeServiceImpl.getEmployeeById(anyLong()));
    }

    @Test
    public void postEmployeeTest() {
        when(employeeRepository.save(newEmployee)).thenReturn(newEmployee);
        assertEquals(newEmployee, employeeServiceImpl.postEmployee(newEmployee));
        verify(employeeRepository).save(newEmployee);
    }

    @Test
    public void postEmployeeTest_idExists() {
        assertThrows(EntityAlreadyExistsException.class, () -> employeeServiceImpl.postEmployee(employee1));
    }

    @Test
    public void postEmployeeTest_emptyName() {
        Employee emptyNameEmployee = new Employee();
        emptyNameEmployee.setName(null);
        emptyNameEmployee.addRole(Role.MARKETING_EMPLOYEE);
        assertThrows(BlankValueNotAllowedException.class, () -> employeeServiceImpl.postEmployee(emptyNameEmployee));
    }

    @Test
    public void postEmployeeTest_blankName() {
        Employee blankNameEmployee = new Employee();
        blankNameEmployee.setName("   ");
        blankNameEmployee.addRole(Role.STAFF_VIEWER);
        assertThrows(BlankValueNotAllowedException.class, () -> employeeServiceImpl.postEmployee(blankNameEmployee));
    }

    @Test
    public void putEmployeeTest() {
        when(employeeRepository.save(employee1)).thenReturn(employee1);
        assertEquals(employee1, employeeServiceImpl.putEmployee(employee1));
        verify(employeeRepository).save(employee1);
    }

    @Test
    public void putEmployeeTest_idDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> employeeServiceImpl.putEmployee(newEmployee));
    }

    @Test
    public void putEmployeeTest_emptyName() {
        Employee emptyNameEmployee = new Employee();
        emptyNameEmployee.setId(10L);
        emptyNameEmployee.setName(null);
        emptyNameEmployee.addRole(Role.HR_EMPLOYEE);
        assertThrows(BlankValueNotAllowedException.class, () -> employeeServiceImpl.putEmployee(emptyNameEmployee));
    }

    @Test
    public void putEmployeeTest_blankName() {
        Employee blankNameEmployee = new Employee();
        blankNameEmployee.setId(20L);
        blankNameEmployee.setName("   ");
        blankNameEmployee.addRole(Role.ACCOUNTANT);
        assertThrows(BlankValueNotAllowedException.class, () -> employeeServiceImpl.putEmployee(blankNameEmployee));
    }

    @Test
    public void deleteEmployeeTest() {
        employeeServiceImpl.deleteEmployee(employee1);
        verify(employeeRepository).delete(employee1);
    }

    @Test
    public void deleteEmployeeTest_IdDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> employeeServiceImpl.deleteEmployee(newEmployee));
    }
}
