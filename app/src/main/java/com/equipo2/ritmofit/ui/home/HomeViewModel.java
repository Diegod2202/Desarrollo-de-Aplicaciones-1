package com.equipo2.ritmofit.ui.home;

import androidx.lifecycle.*;
import com.equipo2.ritmofit.data.model.GymClass;
import com.equipo2.ritmofit.data.repository.ClassRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.*;

@HiltViewModel
public class HomeViewModel extends ViewModel {
    private final ClassRepository repo;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<List<GymClass>> classes = new MutableLiveData<>();

    @Inject public HomeViewModel(ClassRepository repo){ this.repo = repo; }

    public LiveData<Boolean> getLoading(){ return loading; }
    public LiveData<String> getError(){ return error; }
    public LiveData<List<GymClass>> getClasses(){ return classes; }

    public void load(String disciplina, String sede, String fecha){
        loading.postValue(true);
        error.postValue(null);
        repo.getClasses(disciplina, sede, fecha).enqueue(new Callback<List<GymClass>>() {
            @Override public void onResponse(Call<List<GymClass>> c, Response<List<GymClass>> r) {
                loading.postValue(false);
                if (r.isSuccessful()) classes.postValue(r.body());
                else error.postValue("No se pudieron cargar las clases");
            }
            @Override public void onFailure(Call<List<GymClass>> c, Throwable t) {
                loading.postValue(false);
                error.postValue(t.getMessage());
            }
        });
    }
}
