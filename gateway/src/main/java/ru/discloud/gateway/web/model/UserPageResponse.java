package ru.discloud.gateway.web.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.discloud.gateway.domain.User;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPageResponse extends PageImpl<User> {

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public UserPageResponse(@JsonProperty("content") List<User> content,
                          @JsonProperty("pageable") Map<String, Object> pageable,
                          @JsonProperty("number") int number,
                          @JsonProperty("size") int size,
                          @JsonProperty("totalElements") Long totalElements) {
    super(content, PageRequest.of(number, size), totalElements);
  }
}
