package ru.discloud.statistics.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.discloud.statistics.service.UploadService;
import ru.discloud.statistics.web.model.AckResponse;
import ru.discloud.statistics.web.model.UploadRequest;

import javax.validation.Valid;

@Controller
@RequestMapping("/api/statistics/upload")
public class UploadController {
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public AckResponse save(@Valid @RequestBody UploadRequest request) {
        uploadService.save(request);
        return new AckResponse(true);
    }
}
