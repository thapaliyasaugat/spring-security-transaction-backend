package com.securitytest.securitytest.resource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashbackSchemeDto implements Serializable {
    private int id;
    private String Subject;
    private String description;
    private double rewardRate;
    private boolean active;
    private String initiatedBy;
    private String updatedBy;
}
