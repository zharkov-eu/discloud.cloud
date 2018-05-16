package ru.discloud.shared.web.User;

public interface UserRequest {
    UserRequest setEmail(String email);
    UserRequest setPhone(String phone);
    UserRequest setUsername(String username);
    UserRequest setUserPrivileges(String privileges);
    UserRequest setQuota(Long quota);
}
