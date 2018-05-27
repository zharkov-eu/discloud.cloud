package ru.discloud.shared.web.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserRequest {
  @JsonProperty("username")
  @NotBlank(message = "Username not provided")
  protected String username;

  @JsonProperty("group")
  @NotBlank(message = "Group not provided")
  protected List<String> group;

  @JsonProperty("password")
  @NotBlank(message = "Password not provided")
  protected String password;
}
