package ru.discloud.user.integration.mailgun;

import lombok.Data;

@Data
public class SendMessageResponse {
  private String id;
  private String message;
}
