package com.securitytest.securitytest.resource;

import com.securitytest.securitytest.models.Role;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class CashbackRequest {
    @NotEmpty(message = "cashbackScheme Subject can't be empty.")
    private String Subject;
    @NotEmpty(message = "cashbackScheme Description can't be empty.")
    private String description;
    @NotNull(message = "Cashback Rate can't be empty.")
    @Min(1)
    private double rewardRate;
    @NotNull(message = "Specify atLeast one Role.")
    private Set<Role> eligibleRoles;
}
