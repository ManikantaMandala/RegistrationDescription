package com.areksoft.api.models;

import lombok.Data;

@Data
public class OTPVerifyRequest {
    User user;
    Integer otp;
    public OTPVerifyRequest(User user, Integer otp){
        this.user = user;
        this.otp = otp;
    }
}
