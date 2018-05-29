package ru.discloud.gateway.web.model;

import ru.discloud.gateway.domain.Entry;

public class EntryResponse extends ru.discloud.shared.web.core.EntryResponse {
  public EntryResponse(Entry entry) {
    this.uuid = entry.getUuid();
    this.type = entry.getType().toString();
    this.filetype = entry.getFiletype();
    this.path = entry.getPath();
    this.owner = entry.getOwner();
    this.group = entry.getGroup();
    this.permission = entry.getPermission();
    this.share = entry.getShare().toString();
    this.created = entry.getCreated();
    this.lastModify = entry.getLastModify();
    this.upload = entry.getUpload();
    this.location = entry.getLocation();
  }
}
