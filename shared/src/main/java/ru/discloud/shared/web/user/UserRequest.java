package ru.discloud.shared.web.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.MemberOf;
import ru.discloud.shared.UserPrivileges;

import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
public class UserRequest {
  @JsonProperty("id")
  @NotNull(message = "Id not provided")
  @Positive(message = "Id must be a positive value")
  protected Long id;

  @JsonProperty("email")
  @Email(message = "Provided email isn't valid")
  protected String email;

  @JsonProperty("phone")
  @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone not provided")
  protected String phone;

  @JsonProperty("username")
  @NotBlank(message = "Username not provided")
  protected String username;

  @JsonProperty("privileges")
  @MemberOf(value = "user,admin", message = "privileges isn't recognized")
  protected UserPrivileges userPrivileges;

  @JsonProperty("quota")
  @NotNull(message = "Quota not provided")
  @Positive(message = "Quota must be a positive value")
  protected Long quota;
}
