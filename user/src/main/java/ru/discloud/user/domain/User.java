package ru.discloud.user.domain;

import lombok.Data;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user")
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

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", nullable = false)
    private String salt;

    @Enumerated(EnumType.STRING)
    @Column(name = "privileges", nullable = false)
    private UserPrivileges privileges;

    // Quota in kilobytes
    @Column(name = "quota")
    private Long quota;
}
