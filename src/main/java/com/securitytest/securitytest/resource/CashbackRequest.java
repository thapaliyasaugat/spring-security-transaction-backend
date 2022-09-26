package com.securitytest.securitytest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashbackRequest {
    @NotEmpty(message = "cashbackScheme Subject can't be empty.")
    private String Subject;
    @NotEmpty(message = "cashbackScheme Description can't be empty.")
    private String description;
    @NotNull(message = "Cashback Rate can't be empty.")
    @Min(1)
    private double rewardRate;
    @NotNull(message = "Specify atLeast one Role.")
    private Set<RoleDto> eligibleRoles;
}
