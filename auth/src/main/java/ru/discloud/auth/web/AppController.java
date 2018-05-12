package ru.discloud.auth.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.discloud.auth.domain.App;
import ru.discloud.auth.service.AppService;
import ru.discloud.auth.web.model.AppRequest;
import ru.discloud.auth.web.model.AppResponse;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/app")
public class AppController {

    private final AppService appService;

    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AppResponse getApp(@PathVariable Integer id) {
        return new AppResponse(appService.findById(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public AppResponse createApp(@Valid @RequestBody AppRequest appRequest, HttpServletResponse response) {
        App app = appService.save(appRequest);
        response.addHeader(HttpHeaders.LOCATION, "/api/auth/app/" + app.getId());
        return new AppResponse(app);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteApp(@PathVariable Integer id) {
        appService.delete(id);
    }
}
