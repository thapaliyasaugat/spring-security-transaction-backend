package com.securitytest.securitytest.resource;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LoadBalanceRequest {
    @NotEmpty(message = "Specify from where yo want to load.")
    private String loadedFrom;
    @NotNull(message = "Invalid amount.")
    private Double amount;
}
