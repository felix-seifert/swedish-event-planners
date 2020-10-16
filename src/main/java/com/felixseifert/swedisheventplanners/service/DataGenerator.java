package com.felixseifert.swedisheventplanners.service;

import com.felixseifert.swedisheventplanners.model.Client;
import com.felixseifert.swedisheventplanners.model.Employee;
import com.felixseifert.swedisheventplanners.model.enums.Role;
import com.felixseifert.swedisheventplanners.repos.ClientRepository;
import com.felixseifert.swedisheventplanners.repos.EmployeeRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import java.util.ArrayList;
import java.util.List;

@SpringComponent
@Slf4j
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ClientRepository clientRepository, EmployeeRepository employeeRepository) {
        return args -> {

            insertEmployees(employeeRepository);

            insertClients(clientRepository);
        };
    }

    private void insertEmployees(EmployeeRepository employeeRepository) {
        if (employeeRepository.count() != 0L) {
            log.info("Using existing database");
            return;
        }

        log.info("Generating demo employees");

        List<Employee> employees = new ArrayList<>();

        Employee mike = new Employee();
        mike.setName("Mike");
        mike.addRole(Role.ADMINISTRATION_MANAGER);
        employees.add(mike);

        Employee janet = new Employee();
        janet.setName("Janet");
        janet.addRole(Role.SENIOR_CUSTOMER_SERVICE_OFFICER);
        janet.addRole(Role.CLIENT_VIEWER);
        employees.add(janet);

        employeeRepository.saveAll(employees);
    }

    private void insertClients(ClientRepository clientRepository) {
        if (clientRepository.count() != 0L) {
            log.info("Using existing database");
            return;
        }
        long seed = 123L;

        log.info("Generating demo clients");

        int numberOfClients = 30;
        log.info("... generating {} Person entities...", numberOfClients);
        ExampleDataGenerator<Client> personRepositoryGenerator = new ExampleDataGenerator<>(Client.class, seed);
        personRepositoryGenerator.setData(Client::setName, DataType.COMPANY_NAME);
        personRepositoryGenerator.setData(Client::setContactDetails, DataType.EMAIL);
        clientRepository.saveAll(personRepositoryGenerator.create(numberOfClients));

        log.info("Generated demo data");
    }

}