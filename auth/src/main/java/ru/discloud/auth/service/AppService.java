package ru.discloud.auth.service;

import ru.discloud.auth.domain.App;
import ru.discloud.auth.web.model.AppRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

public interface AppService {

    App findById(Integer id) throws EntityNotFoundException;

    App save(AppRequest appRequest) throws EntityExistsException;

    void delete(Integer id);
}
