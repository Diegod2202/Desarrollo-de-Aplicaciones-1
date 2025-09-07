package com.equipo2.ritmofit.data.model;
public class VerifyOtpRequest {
    private String email;
    private String code;
    public VerifyOtpRequest(String email, String code){ this.email=email; this.code=code; }
}