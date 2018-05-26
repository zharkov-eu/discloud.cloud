package ru.discloud.shared.web;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class HttpStatusSerializer extends JsonSerializer<HttpStatus> {

  @Override
  public void serialize(HttpStatus status,
                        JsonGenerator jgen,
                        SerializerProvider serializers)
      throws IOException {
    jgen.writeNumber(status.value());
  }
}