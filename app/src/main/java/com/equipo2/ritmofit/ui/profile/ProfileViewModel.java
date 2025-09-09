package com.equipo2.ritmofit.ui.profile;

import androidx.lifecycle.*;
import com.equipo2.ritmofit.data.model.User;
import com.equipo2.ritmofit.data.model.UserResponse;
import com.equipo2.ritmofit.data.repository.UserRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.*;

@HiltViewModel
public class ProfileViewModel extends ViewModel {
    private final UserRepository repo;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<User> user = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> saved = new MutableLiveData<>(false);

    @Inject public ProfileViewModel(UserRepository repo){ this.repo = repo; }

    public LiveData<Boolean> getLoading(){ return loading; }
    public LiveData<String> getError(){ return error; }
    public LiveData<User> getUser(){ return user; }
    public LiveData<Boolean> getSaved(){ return saved; }

    public void load(){
        loading.postValue(true); error.postValue(null);
        repo.getProfile().enqueue(new Callback<User>() {
            @Override public void onResponse(Call<User> c, Response<User> r) {
                loading.postValue(false);
                if (r.isSuccessful()) user.postValue(r.body());
                else error.postValue("No se pudo cargar el perfil");
            }
            @Override public void onFailure(Call<User> c, Throwable t) {
                loading.postValue(false); error.postValue(t.getMessage());
            }
        });
    }

    public void save(String name, String photo){
        User current = user.getValue();
        if (current == null) current = new User();
        current.name = name;
        current.photo = photo;

        loading.postValue(true); error.postValue(null); saved.postValue(false);
        repo.updateProfile(current).enqueue(new Callback<UserResponse>() {
            @Override public void onResponse(Call<UserResponse> c, Response<UserResponse> r) {
                loading.postValue(false);
                if (r.isSuccessful() && r.body()!=null) {
                    user.postValue(r.body().user);
                    saved.postValue(true);
                } else error.postValue("No se pudo guardar");
            }
            @Override public void onFailure(Call<UserResponse> c, Throwable t) {
                loading.postValue(false); error.postValue(t.getMessage());
            }
        });
    }
}
