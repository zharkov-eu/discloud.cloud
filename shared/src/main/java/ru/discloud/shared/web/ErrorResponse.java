package ru.discloud.shared.web;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class ErrorResponse {
  private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

  private final String error;
  private final String message;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private final Date timestamp;
  @JsonSerialize(using = HttpStatusSerializer.class)
  private final HttpStatus status;

  public ErrorResponse(Exception exception, HttpStatus status) {
    this.error = exception.getClass().getSimpleName();
    this.message = exception.getMessage() != null ? exception.getMessage() : error;
    this.timestamp = new Date();
    this.status = status;
  }

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public ErrorResponse(@JsonProperty("error") String error,
                       @JsonProperty("message") String message,
                       @JsonProperty("timestamp") String timestamp,
                       @JsonProperty("status") Integer status) throws ParseException {
    this.error = error;
    this.message = message;
    this.timestamp = dateFormat.parse(timestamp);
    this.status = HttpStatus.resolve(status);
  }
}