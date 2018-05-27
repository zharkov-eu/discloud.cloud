package ru.discloud.statistics.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AckResponse {
  private Boolean success;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
  private Date timestamp;

  public AckResponse(Boolean success) {
    this.success = success;
    this.timestamp = new Date();
  }
}