package ru.discloud.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.discloud.user.domain.Client;
import ru.discloud.user.web.model.ClientRequest;

public interface ClientService {
  Page<Client> findAll(Pageable pageable);

  Client findById(Long id);

  Client findByEmail(String email);

  Client save(ClientRequest clientRequest);

  Client update(Long id, ClientRequest clientRequest);

  void delete(Long id);
}
