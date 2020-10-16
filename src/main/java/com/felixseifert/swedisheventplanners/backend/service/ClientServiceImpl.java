package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.exceptions.BlankValueNotAllowedException;
import com.felixseifert.swedisheventplanners.backend.exceptions.EntityAlreadyExistsException;
import com.felixseifert.swedisheventplanners.backend.model.Client;
import com.felixseifert.swedisheventplanners.backend.repos.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        log.debug("Return {} clients", clients.size());
        return clients;
    }

    @Override
    public Client getClientById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if(client.isEmpty()) {
            throw new EntityNotFoundException("Client with id " + id + " does not exist");
        }
        log.debug("Return client {}", client.get());
        return client.get();
    }

    @Override
    public Client postClient(Client client) {
        if(client.getId() != null) {
            throw new EntityAlreadyExistsException("Given Client already has id");
        }

        checkBlankVariables(client);

        Client newClient = clientRepository.save(client);
        log.info("New Client {} created", newClient);
        return newClient;
    }

    @Override
    public Client putClient(Client client) {
        if(client.getId() == null) {
            throw new EntityNotFoundException("Client does not have id");
        }

        checkBlankVariables(client);

        Client updatedClient = clientRepository.save(client);
        log.info("Client {} updated", updatedClient);
        return updatedClient;
    }

    @Override
    public void deleteClient(Client client) {
        if(client.getId() == null) {
            throw new EntityNotFoundException("Client does not have id");
        }

        clientRepository.delete(client);
        log.info("Client {} deleted", client);
    }

    private void checkBlankVariables(Client client) {
        if(StringUtils.isBlank(client.getName())) {
            throw new BlankValueNotAllowedException("Client name is blank");
        }
    }
}
