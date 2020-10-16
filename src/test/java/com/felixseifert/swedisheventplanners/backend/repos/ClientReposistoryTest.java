package com.felixseifert.swedisheventplanners.backend.repos;

import com.felixseifert.swedisheventplanners.backend.model.Client;
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
public class ClientReposistoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ClientRepository clientRepository;

    private Client client1;
    private Client client2;

    @BeforeEach
    public void setDatabase() {
        client1 = new Client();
        client1.setName("Karl");
        client1.setContactDetails("Call me here!");
        client2 = new Client();
        client2.setName("Carla");
        client2.setContactDetails("Message me");

        testEntityManager.persist(client1);
        testEntityManager.persist(client2);
        testEntityManager.flush();
    }

    @Test
    public void findAllTest() {
        List<Client> clientsActual = clientRepository.findAll();
        assertEquals(List.of(client1, client2), clientsActual);
    }

    @Test
    public void findByIdTest() {
        Optional<Client> clientActual = clientRepository.findById(client1.getId());
        Optional<Client> clientActualEmpty = clientRepository.findById(9999L);

        assertTrue(clientActual.isPresent());
        assertEquals(client1, clientActual.get());
        assertFalse(clientActualEmpty.isPresent());
    }

    @Test
    public void saveTest() {
        Client clientToSave = new Client();
        clientToSave.setName("Daniel Dudley");
        clientToSave.setContactDetails("daniel.dudley@test.com");

        Client clientActual = clientRepository.save(clientToSave);
        List<Client> clientListActual = clientRepository.findAll();
        boolean clientExists = clientListActual.contains(clientToSave);

        assertEquals(clientToSave, clientActual);
        assertTrue(clientExists);
        assertNotNull(clientActual.getId());
        assertEquals(3, clientListActual.size());
        assertEquals(clientToSave, clientListActual.get(2));
    }

    @Test
    public void deleteTest() {
        clientRepository.delete(client1);

        List<Client> clientListActual = clientRepository.findAll();
        boolean clientExists = clientListActual.contains(client1);

        assertFalse(clientExists);
        assertEquals(1, clientListActual.size());
    }
}
