package com.felixseifert.swedisheventplanners.service;

import com.felixseifert.swedisheventplanners.model.Client;
import com.felixseifert.swedisheventplanners.repos.ClientRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

@SpringComponent
@Slf4j
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(ClientRepository clientRepository) {
        return args -> {
            if (clientRepository.count() != 0L) {
                log.info("Using existing database");
                return;
            }
            long seed = 123L;

            log.info("Generating demo data");

            log.info("... generating 100 Person entities...");
            ExampleDataGenerator<Client> personRepositoryGenerator = new ExampleDataGenerator<>(Client.class, seed);
            personRepositoryGenerator.setData(Client::setName, DataType.COMPANY_NAME);
            personRepositoryGenerator.setData(Client::setContactDetails, DataType.EMAIL);
            clientRepository.saveAll(personRepositoryGenerator.create(30));

            log.info("Generated demo data");
        };
    }

}