package ru.discloud.shared.web.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Accessors(chain = true)
public class UserRequest {
  @JsonProperty("id")
  @NotNull(message = "Id isn't presented")
  @Positive(message = "Id must be positive value")
  Long id;

  @JsonProperty("username")
  @NotEmpty(message = "Username isn't presented")
  String username;

  @JsonProperty("password")
  @NotEmpty(message = "Password isn't presented")
  @Length(min = 8, message = "Password must be more than 8 symbols length")
  String password;
}
