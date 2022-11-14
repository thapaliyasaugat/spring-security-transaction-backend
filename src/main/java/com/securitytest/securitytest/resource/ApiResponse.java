package com.securitytest.securitytest.resource;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> implements Serializable {
    private T data;
    private String message;
    private int status;
}

