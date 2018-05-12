package ru.discloud.auth.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.auth.domain.App;
import ru.discloud.auth.domain.AppTokenPermission;
import ru.discloud.auth.domain.AppTokenType;
import ru.discloud.auth.lib.ReverseLookupEnum;
import ru.discloud.auth.repository.jpa.AppRepository;
import ru.discloud.auth.web.model.AppRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
public class AppServiceImpl implements AppService {
    private final AppRepository appRepository;
    private final ReverseLookupEnum<AppTokenType> appTokenType = new ReverseLookupEnum<>(AppTokenType.class);
    private final ReverseLookupEnum<AppTokenPermission> appTokenPermission = new ReverseLookupEnum<>(AppTokenPermission.class);

    @Autowired
    public AppServiceImpl(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    @Override
    public App findById(Integer id) {
        return appRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("App '{" + id + "}' not found"));
    }

    @Override
    public App save(AppRequest appRequest) {
        App existingApp = appRepository.findByName(appRequest.getName()).orElse(null);
        if (existingApp != null) {
            throw new EntityExistsException("App with name '{" + appRequest.getName() + "}' already exist");
        }

        App app = new App()
                .setName(appRequest.getName())
                .setSecret(RandomStringUtils.randomAlphanumeric(75))
                .setTokenType(appTokenType.get(appRequest.getTokenType()))
                .setTokenPermission(appTokenPermission.get(appRequest.getTokenPermission()));

        return appRepository.save(app);
    }

    @Override
    public void delete(Integer id) {
        appRepository.deleteById(id);
    }
}
