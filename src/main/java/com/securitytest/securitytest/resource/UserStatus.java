package com.securitytest.securitytest.resource;

public enum UserStatus {
    BLOCKED("BLOCKED"),
    ACTIVE("ACTIVE");
    public final String status;

    UserStatus(String status){
        this.status = status;
    }
}
