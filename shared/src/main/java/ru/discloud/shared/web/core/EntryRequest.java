package ru.discloud.shared.web.core;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.shared.MemberOf;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Accessors(chain = true)
public class EntryRequest {
  @MemberOf(value = "f,d")
  protected String type;

  protected String filetype;

  @NotBlank
  protected String path;

  @NotNull
  @Positive
  protected Long owner;

  @NotNull
  @Positive
  protected Integer group;

  @NotBlank
  @Size(min = 3, max = 3)
  protected String permission;

  @MemberOf(value = "n,l")
  protected String share;

  @NotNull
  protected List<String> location;
}
