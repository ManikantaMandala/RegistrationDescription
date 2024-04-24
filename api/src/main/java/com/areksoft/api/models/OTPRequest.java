package com.areksoft.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OTPRequest {
    @JsonProperty(value = "username", required = true)
    private String username;
    @JsonProperty(value = "password", required = true)
    private String password;
    @JsonProperty(value = "mobile", required = true)
    private String mobile;
    @JsonProperty(value = "email", required = true)
    private String email;
}
