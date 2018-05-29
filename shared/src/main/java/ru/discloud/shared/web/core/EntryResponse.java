package ru.discloud.shared.web.core;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class EntryResponse {
  protected String uuid;
  protected String type;
  protected String filetype;
  protected String path;
  protected Long owner;
  protected Number group;
  protected String permission;
  protected String share;
  protected Date created;
  protected Date lastModify;
  protected String upload;
  protected List<String> location;
}
