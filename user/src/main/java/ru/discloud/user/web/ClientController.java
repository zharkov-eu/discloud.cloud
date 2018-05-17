package ru.discloud.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.discloud.user.domain.Client;
import ru.discloud.user.service.ClientService;
import ru.discloud.user.web.model.ClientResponse;

@RestController
@RequestMapping("/api/user/")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Page<ClientResponse> getClients(Pageable pageable) {
        Page<Client> clientsPage = clientService.findAll(pageable);
        return clientsPage.map(ClientResponse::new);
    }
}
