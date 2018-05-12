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

@RestController
@RequestMapping("/api/auth/app/")
public class AppController {

    private final AppService appService;

    @Autowired
    public AppController(AppService appService) {
        this.appService = appService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public AppResponse createApp(@RequestBody AppRequest appRequest, HttpServletResponse response) {
        App app = appService.save(appRequest);
        response.addHeader(HttpHeaders.LOCATION, "/api/auth/app/" + app.getId());
        return new AppResponse(app);
    }
}
