package ru.discloud.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.discloud.auth.domain.App;
import ru.discloud.auth.repository.AppRepository;
import ru.discloud.auth.web.model.AppRequest;

import javax.persistence.EntityNotFoundException;

@Service
public class AppServiceImpl implements AppService {
    private final AppRepository appRepository;

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
    public App findByName(String name) {
        return appRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("App by name '{" + name + "}' not found"));
    }

    @Override
    public App save(AppRequest appRequest) {
        return null;
    }
}
