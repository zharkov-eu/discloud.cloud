package ru.discloud.auth.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "user")
@Accessors(chain = true)
public class User {
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "salt")
  private String salt;
}
