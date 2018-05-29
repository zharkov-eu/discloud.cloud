package ru.discloud.gateway.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.discloud.gateway.web.model.EntryRequest;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class Entry {
  private String uuid; // UUID v4
  private EntryType type; // Entry type
  private String filetype; // File Type
  private String parent; // Parent UUID
  private List<String> child; // Child UUIDs (Only if type = EntryType.DIRECTORY)
  private String path; // Path representation
  private Long owner; // Entry owner ID
  private Integer group; // Entry group ID
  private String permission; // Entry permission (UNIX-like, ex: 644)
  private EntryShare share; // Entry share status, set only if entry have one of shared type
  private Date created; // Entry created timestamp
  private Integer size; // Entry size in kilobytes
  private Date lastModify; // Entry last modify timestamp
  private String upload; // Upload url
  private List<String> location; // Nodes of physical content host
  private String locationPath; // Physical host on nodes

  public Entry(EntryRequest entryRequest) {
    this.type = EntryType.valueOf(entryRequest.getType());
    this.filetype = entryRequest.getFiletype();
    this.path = entryRequest.getPath();
    this.owner = entryRequest.getOwner();
    this.group = entryRequest.getGroup();
    this.permission = entryRequest.getPermission();
    this.share = EntryShare.valueOf(entryRequest.getShare());
    this.location = entryRequest.getLocation();
  }

  public ru.discloud.shared.web.core.EntryRequest getCoreEntryRequest() {
    return new EntryRequest()
        .setType(this.type.toString())
        .setFiletype(this.filetype)
        .setPath(this.path)
        .setOwner(this.owner)
        .setGroup(this.group)
        .setPermission(this.permission)
        .setShare(this.share.toString())
        .setLocation(this.location);

  }
}
