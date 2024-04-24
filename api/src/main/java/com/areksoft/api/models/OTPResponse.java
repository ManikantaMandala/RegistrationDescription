package com.areksoft.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OTPResponse {
    @JsonProperty(value = "message", required = true)
    private String message;
}
