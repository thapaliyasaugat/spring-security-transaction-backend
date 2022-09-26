package com.securitytest.securitytest.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashbackScheme extends DateAudit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String subject;
    private String description;
    private double rewardRate;

    @ManyToMany
    @JoinTable(name = "cashback_eligible_roles",
            joinColumns = @JoinColumn(name = "cashback_scheme"),
            inverseJoinColumns = @JoinColumn(name = "eligible_role"))
    private Set<Role> eligibleRoles = new HashSet<>();

    private boolean active;
    private String initiatedBy;//email-unique
    private String updatedBy;
}
