package com.equipo2.ritmofit.data.repository;

import com.equipo2.ritmofit.data.model.User;
import com.equipo2.ritmofit.data.model.UserResponse;
import com.equipo2.ritmofit.data.network.ApiService;

import javax.inject.Inject;

import retrofit2.Call;

public class UserRepository {
    private final ApiService api;
    @Inject public UserRepository(ApiService api){ this.api = api; }

    public Call<User> getProfile(){ return api.getProfile(); }
    public Call<UserResponse> updateProfile(User user){ return api.updateProfile(user); }
}
