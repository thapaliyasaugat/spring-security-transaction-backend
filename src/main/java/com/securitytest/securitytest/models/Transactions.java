package com.securitytest.securitytest.models;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transactions extends DateAudit{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "transaction_to")
    private User customer_to;

    private Double amount;

    @ManyToOne
    @JoinColumn(name = "transaction_from")
    private User customer_from;
}
