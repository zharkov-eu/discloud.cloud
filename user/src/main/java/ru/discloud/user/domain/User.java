package ru.discloud.user.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import ru.discloud.shared.UserPrivileges;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
@Accessors(chain = true)
public class User {
  @Id
  @Column(name = "id")
  @GeneratedValue(generator = "User_Sequence")
  @GenericGenerator(name = "User_Sequence", strategy = "sequence", parameters = {
      @Parameter(name = "sequence_name", value = "user_sequence")
  })
  private Long id;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "phone", unique = true)
  private String phone;

  @Column(name = "username", unique = true, nullable = false)
  private String username;

  @Enumerated(EnumType.STRING)
  @Column(name = "privileges", nullable = false)
  private UserPrivileges privileges;

  // Quota in kilobytes
  @Column(name = "quota")
  private Long quota;

  @Column(name = "signup_message")
  private String signupMessage;
}
