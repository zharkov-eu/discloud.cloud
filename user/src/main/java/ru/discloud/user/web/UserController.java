package ru.discloud.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.discloud.user.domain.User;
import ru.discloud.user.integration.mailgun.MailClient;
import ru.discloud.user.service.UserService;
import ru.discloud.user.web.model.UserRequest;
import ru.discloud.user.web.model.UserResponse;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/user/user/")
public class UserController {

    private final UserService userService;
    private final MailClient mailClient;

    @Autowired
    public UserController(UserService userService, MailClient mailClient) {
        this.userService = userService;
        this.mailClient = mailClient;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Page<UserResponse> getUsers(Pageable pageable) {
        Page<User> usersPage = userService.findAll(pageable);
        return usersPage.map(UserResponse::new);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserResponse getUser(@PathVariable Long id) {
        return new UserResponse(userService.findById(id));
    }

    @RequestMapping(value = "/by/", method = RequestMethod.GET)
    public UserResponse getUserBy(@RequestParam(name = "username", required = false) String username,
                                  @RequestParam(name = "email", required = false) String email,
                                  @RequestParam(name = "phone", required = false) String phone) {
        User user = null;
        if (username != null) user = userService.findByUsername(username);
        if (email != null) user = userService.findByEmail(email);
        if (phone != null) user = userService.findByPhone(phone);
        if (user == null) throw new EntityNotFoundException("User not found");
        return new UserResponse(user);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public UserResponse save(@Valid @RequestBody UserRequest userRequest, HttpServletResponse response) throws Exception {
        User user = userService.save(userRequest);
        response.addHeader(HttpHeaders.LOCATION, "/user/" + user.getId());
        return new UserResponse(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        return new UserResponse(userService.update(id, userRequest));
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }
}
