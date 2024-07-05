package com.areksoft.api.models.request;

import com.areksoft.api.models.User;
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
