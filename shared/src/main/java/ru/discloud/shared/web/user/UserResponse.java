package ru.discloud.shared.web.user;

public interface UserResponse {
    String getEmail(String email);
    String getPhone(String phone);
    String getUsername(String username);
    String getUserPrivileges(String privileges);
    Long getQuota();
}
