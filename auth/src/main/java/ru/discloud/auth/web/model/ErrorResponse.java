package ru.discloud.auth.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ErrorResponse {
  private String error;
  private String message;
  private Date timestamp;
  private Integer status;
}
