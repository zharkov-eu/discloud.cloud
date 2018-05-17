package ru.discloud.user.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "Payment_Sequence")
    @GenericGenerator(name = "Payment_Sequence", strategy = "sequence", parameters = {
            @org.hibernate.annotations.Parameter(name = "sequence_name", value = "payment_sequence")
    })
    private Long id;

    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "payment_system_id")
    private String paymentSystemId;

    @ManyToOne(targetEntity = Client.class, optional = false)
    private Client client;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;
}
