package com.equipo2.ritmofit.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.equipo2.ritmofit.data.TokenManager;
import com.equipo2.ritmofit.data.model.LoginResponse;
import com.equipo2.ritmofit.data.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final AuthRepository repo;
    private final TokenManager tokenManager;

    private final MutableLiveData<Boolean> otpSent = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> loggedIn = new MutableLiveData<>(false);

    @Inject
    public AuthViewModel(AuthRepository repo, TokenManager tokenManager){
        this.repo = repo;
        this.tokenManager = tokenManager;
    }

    public LiveData<Boolean> getOtpSent(){ return otpSent; }
    public LiveData<Boolean> getLoading(){ return loading; }
    public LiveData<String> getError(){ return error; }
    public LiveData<Boolean> getLoggedIn(){ return loggedIn; }

    public void requestOtp(String email){
        loading.postValue(true);
        error.postValue(null);
        repo.requestOtp(email).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> res){
                loading.postValue(false);
                if(res.isSuccessful()) otpSent.postValue(true);
                else error.postValue("No se pudo enviar OTP");
            }
            @Override public void onFailure(Call<Void> call, Throwable t){
                loading.postValue(false);
                error.postValue(t.getMessage());
            }
        });
    }

    public void verifyOtp(String email, String code){
        loading.postValue(true);
        error.postValue(null);
        repo.verifyOtp(email, code).enqueue(new Callback<LoginResponse>() {
            @Override public void onResponse(Call<LoginResponse> call, Response<LoginResponse> res){
                loading.postValue(false);
                if(res.isSuccessful() && res.body()!=null){
                    tokenManager.saveToken(res.body().getToken());
                    loggedIn.postValue(true);
                } else {
                    error.postValue("OTP inválido o expirado");
                }
            }
            @Override public void onFailure(Call<LoginResponse> call, Throwable t){
                loading.postValue(false);
                error.postValue(t.getMessage());
            }
        });
    }
}
