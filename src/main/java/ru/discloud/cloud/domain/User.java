package ru.discloud.cloud.domain;

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

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "privileges")
    private UserPrivileges privileges;
}
