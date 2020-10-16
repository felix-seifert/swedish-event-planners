package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.exceptions.BlankValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.exceptions.EntityAlreadyExistsException;
import com.felixseifert.swedisheventplanners.backend.model.Client;
import com.felixseifert.swedisheventplanners.backend.repos.ClientRepository;
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
public class ClientServiceImplTest {

    @Autowired
    private ClientServiceImpl clientServiceImpl;

    @MockBean
    private ClientRepository clientRepository;

    private static Client client1;
    private static Client client2;
    private static Client newClient;

    @BeforeAll
    public static void setup() {
        client1 = new Client();
        client1.setName("Felix Seifert");
        client1.setContactDetails("Hey there");
        client1.setId(1L);
        client2 = new Client();
        client2.setName("Alex Costa");
        client2.setContactDetails("Testing here");
        client2.setId(2L);

        newClient = new Client();
        newClient.setName("Joe New");
        newClient.setContactDetails("I am new.");
    }

    @Test
    public void getAllClientsTest() {
        when(clientRepository.findAll()).thenReturn(List.of(client1, client2));
        List<Client> actualClients = clientServiceImpl.getAllClients();
        assertEquals(List.of(client1,client2), actualClients);
    }

    @Test
    public void getClientByIdTest() {
        when(clientRepository.findById(client1.getId())).thenReturn(Optional.of(client1));
        Client actual = clientServiceImpl.getClientById(client1.getId());
        assertEquals(client1, actual);
    }

    @Test
    public void getClientByIdTest_noClientFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> clientServiceImpl.getClientById(anyLong()));
    }

    @Test
    public void postClientTest() {
        when(clientRepository.save(newClient)).thenReturn(newClient);
        assertEquals(newClient, clientServiceImpl.postClient(newClient));
        verify(clientRepository).save(newClient);
    }

    @Test
    public void postClientTest_idExists() {
        assertThrows(EntityAlreadyExistsException.class, () -> clientServiceImpl.postClient(client1));
    }

    @Test
    public void postClientTest_emptyName() {
        Client emptyNameClient = new Client();
        emptyNameClient.setName(null);
        emptyNameClient.setContactDetails("Contact details");
        assertThrows(BlankValueNotAllowedException.class, () -> clientServiceImpl.postClient(emptyNameClient));
    }

    @Test
    public void postClientTest_blankName() {
        Client blankNameClient = new Client();
        blankNameClient.setName("   ");
        blankNameClient.setContactDetails("Contact details");
        assertThrows(BlankValueNotAllowedException.class, () -> clientServiceImpl.postClient(blankNameClient));
    }

    @Test
    public void putClientTest() {
        when(clientRepository.save(client1)).thenReturn(client1);
        assertEquals(client1, clientServiceImpl.putClient(client1));
        verify(clientRepository).save(client1);
    }

    @Test
    public void putClientTest_idDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> clientServiceImpl.putClient(newClient));
    }

    @Test
    public void putClientTest_emptyName() {
        Client emptyNameClient = new Client();
        emptyNameClient.setId(10L);
        emptyNameClient.setName(null);
        emptyNameClient.setContactDetails("Contact details");
        assertThrows(BlankValueNotAllowedException.class, () -> clientServiceImpl.putClient(emptyNameClient));
    }

    @Test
    public void putClientTest_blankName() {
        Client blankNameClient = new Client();
        blankNameClient.setId(20L);
        blankNameClient.setName("   ");
        blankNameClient.setContactDetails("Contact details");
        assertThrows(BlankValueNotAllowedException.class, () -> clientServiceImpl.putClient(blankNameClient));
    }

    @Test
    public void deleteClientTest() {
        clientServiceImpl.deleteClient(client1);
        verify(clientRepository).delete(client1);
    }

    @Test
    public void deleteClientTest_IdDoesNotExist() {
        assertThrows(EntityNotFoundException.class, () -> clientServiceImpl.deleteClient(newClient));
    }
}
