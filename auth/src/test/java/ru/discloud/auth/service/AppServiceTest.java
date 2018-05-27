package ru.discloud.auth.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import ru.discloud.auth.domain.App;
import ru.discloud.auth.domain.AppTokenPermission;
import ru.discloud.auth.domain.AppTokenType;
import ru.discloud.auth.repository.jpa.AppRepository;
import ru.discloud.auth.web.model.AppRequest;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppServiceTest {
    private final App app = new App()
            .setId(1)
            .setName("oauthApp")
            .setTokenPermission(AppTokenPermission.READONLY)
            .setTokenType(AppTokenType.ETERNAL);

    private final AppRequest appRequest = new AppRequest()
            .setName("oauthApp")
            .setTokenPermission(AppTokenPermission.READONLY.toString())
            .setTokenType(AppTokenType.ETERNAL.toString());

    private AppService appService;

    @Mock
    private AppRepository appRepositoryMock;

    @Before
    public void setUp() {
        appService = new AppServiceImpl(appRepositoryMock);

        when(appRepositoryMock.save(any(App.class))).thenAnswer((Answer) invocation -> {
            App appEntity = (App) invocation.getArguments()[0];
            return new App()
                    .setId(1)
                    .setName(appEntity.getName())
                    .setSecret(appEntity.getSecret())
                    .setTokenPermission(appEntity.getTokenPermission())
                    .setTokenType(appEntity.getTokenType());
        });

        when(appRepositoryMock.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(appRepositoryMock.findById(1)).thenReturn(Optional.of(app));
    }

    @Test
    public void createApp() {
        when(appRepositoryMock.findByName("oauthApp")).thenReturn(Optional.empty());

        App appEntity = appService.save(appRequest);
        app.setSecret(appEntity.getSecret());

        verify(appRepositoryMock, times(1)).save(any(App.class));
        assertEquals("Invalid app saved", app, appEntity);
        assertNotNull("AppSecret is null", app.getSecret());
        assertNotEquals("AppSecret is empty", app.getSecret(), "");
    }

    @Test(expected = EntityExistsException.class)
    public void checkAppExists() {
        when(appRepositoryMock.findByName("oauthApp")).thenReturn(Optional.of(app));

        appService.save(appRequest);
    }

    @Test
    public void findApp() {
        assertEquals(app, appService.findById(1));
    }

    @Test(expected = EntityNotFoundException.class)
    public void findAppNotFound() {
        appService.findById(2);
    }

    @Test
    public void deleteApp() { appService.delete(1); }

    @Test(expected = EntityNotFoundException.class)
    public void deleteAppNotFound() {
        appService.delete(2);
    }
}
