package com.equipo2.ritmofit.data.repository;

import com.equipo2.ritmofit.data.model.LoginResponse;
import com.equipo2.ritmofit.data.model.OtpRequest;
import com.equipo2.ritmofit.data.model.VerifyOtpRequest;
import com.equipo2.ritmofit.data.network.ApiService;

import javax.inject.Inject;

import retrofit2.Call;

public class AuthRepository {
    private final ApiService api;
    @Inject
    public AuthRepository(ApiService api){ this.api = api; }

    public Call<Void> requestOtp(String email){
        return api.requestOtp(new OtpRequest(email));
    }
    public Call<LoginResponse> verifyOtp(String email, String code){
        return api.verifyOtp(new VerifyOtpRequest(email, code));
    }
}
