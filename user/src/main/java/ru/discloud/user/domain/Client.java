package ru.discloud.user.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "Client_Sequence")
    @GenericGenerator(name = "Client_Sequence", strategy = "sequence", parameters = {
            @Parameter(name = "sequence_name", value = "client_sequence")
    })
    private Long id;

    @Column(name = "email")
    private String email;

    @OneToMany(targetEntity = User.class, cascade = CascadeType.ALL)
    private Set<User> users;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(targetEntity = Country.class, cascade = CascadeType.PERSIST)
    private Country country;

    @Column(name = "state")
    private String state;

    @Column(name = "city")
    private String city;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "balance")
    private BigDecimal balance;
}
