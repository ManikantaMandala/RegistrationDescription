package com.areksoft.api.models.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OTPRequest {
    @JsonProperty(value = "firstname", required = true)
    private String firstName;
    @JsonProperty(value = "lastname", required = true)
    private String lastName;
    @JsonProperty(value = "password", required = true)
    private String password;
    @JsonProperty(value = "mobile", required = true)
    private String mobile;
    @JsonProperty(value = "email", required = true)
    private String email;
}
