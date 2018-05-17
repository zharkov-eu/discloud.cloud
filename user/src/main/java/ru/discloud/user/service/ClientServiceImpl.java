package ru.discloud.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.discloud.user.domain.Client;
import ru.discloud.user.repository.ClientRepository;
import ru.discloud.user.web.model.ClientRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Page<Client> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client '{" + id + "}' not found"));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Client by email '{" + email + "}' not found"));
    }

    @Override
    public Client save(ClientRequest clientRequest) {
        Client existingClient = clientRepository.findByEmail(clientRequest.getEmail()).orElse(null);
        if (existingClient != null) {
            throw new EntityExistsException("Client with email '{" + clientRequest.getEmail() + "}' already exist");
        }

        Client client = new Client()
                .setEmail(clientRequest.getEmail());

        return clientRepository.save(client);
    }

    @Override
    public Client update(Long id, ClientRequest clientRequest) {
        return null;
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }
}
