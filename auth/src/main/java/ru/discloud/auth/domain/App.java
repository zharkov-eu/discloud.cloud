package ru.discloud.auth.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Data
@Entity
@Table(name = "app")
@Accessors(chain = true)
public class App {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "App_Sequence")
    @GenericGenerator(name = "App_Sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence_name", value = "app_sequence")
    })
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "secret")
    private String secret;

    @Column(name = "token_type")
    private AppTokenType tokenType;

    @Column(name = "token_permission")
    private AppTokenPermission tokenPermission;
}
