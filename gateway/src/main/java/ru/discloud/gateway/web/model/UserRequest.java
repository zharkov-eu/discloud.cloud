package ru.discloud.gateway.web.model;

import lombok.Data;
import ru.discloud.shared.web.UtmLabel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserRequest {
  @Email(message = "Provided email isn't valid")
  private String email;

  @NotEmpty(message = "Phone not provided")
  private String phone;

  @NotNull(message = "Group not provided")
  private List<String> group;

  private String username;

  @Size(min = 8, message = "Provided password length less than 8 symbols")
  private String password;

  private UtmLabel utm;
}
