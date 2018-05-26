package ru.discloud.user.web.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.discloud.shared.MemberOf;

import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserRequest {
  @Email(message = "Provided email isn't valid")
  private String email;

  @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone not provided")
  private String phone;

  @NotBlank(message = "Username not provided")
  private String username;

  @MemberOf(value = "user,admin", message = "userPrivileges isn't recognized")
  private String userPrivileges;

  @NotNull(message = "Quota not provided")
  @Positive(message = "Quota must be a positive value")
  private Long quota;
}
