package com.felixseifert.swedisheventplanners.backend.service;

import com.felixseifert.swedisheventplanners.backend.model.Client;

import java.util.List;

public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(Long id);

    Client postClient(Client client);

    Client putClient(Client client);

    void deleteClient(Client client);
}
