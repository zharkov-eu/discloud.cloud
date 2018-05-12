package ru.discloud.auth.service;

import ru.discloud.auth.domain.App;
import ru.discloud.auth.web.model.AppRequest;

public interface AppService {

    App findById(Integer id);

    App findByName(String name);

    App save(AppRequest appRequest);
}
