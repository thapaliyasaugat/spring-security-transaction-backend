package com.securitytest.securitytest.resource;

import lombok.*;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoadBalanceRequest {
    @NotBlank(message = "Specify from where yo want to load.")
    private String loadedFrom;

    @DecimalMin("0.0") @DecimalMax("50000.0")
    private Double amount;
}
