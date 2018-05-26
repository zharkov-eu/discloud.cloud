package ru.discloud.user.integration.mailgun;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendMessageRequest {
  /**
   * Email address for From header
   */
  protected String from;

  /**
   * Email address of the recipient(s).
   * Example: "Bob <bob@host.com>". You can use commas to separate multiple recipients.
   */
  protected String to;

  /**
   * Same as To but for Cc
   */
  protected String cc;

  /**
   * Same as To but for Bcc
   */
  protected String bcc;

  /**
   * Message subject
   */
  protected String subject;

  /**
   * Body of the message. (text version)
   */
  protected String text;

  /**
   * Body of the message. (HTML version)
   */
  protected String html;
}
