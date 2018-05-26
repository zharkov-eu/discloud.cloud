package ru.discloud.user.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "country")
public class Country {
  @Id
  @Column(name = "key")
  @Size(min = 2, max = 2)
  private String key;

  @Column(name = "name", unique = true)
  private String name;

  @Column(name = "flag")
  private String flag;
}
