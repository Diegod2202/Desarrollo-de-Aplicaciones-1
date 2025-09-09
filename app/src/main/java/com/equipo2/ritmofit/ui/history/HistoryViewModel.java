package com.equipo2.ritmofit.ui.history;

import androidx.lifecycle.*;
import com.equipo2.ritmofit.data.model.History;
import com.equipo2.ritmofit.data.repository.HistoryRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.*;

@HiltViewModel
public class HistoryViewModel extends ViewModel {
    private final HistoryRepository repo;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<List<History>> items = new MutableLiveData<>();

    @Inject public HistoryViewModel(HistoryRepository repo){ this.repo = repo; }

    public LiveData<Boolean> getLoading(){ return loading; }
    public LiveData<String> getError(){ return error; }
    public LiveData<List<History>> getItems(){ return items; }

    public void load(String from, String to){
        loading.postValue(true); error.postValue(null);
        repo.get(from, to).enqueue(new Callback<List<History>>() {
            @Override public void onResponse(Call<List<History>> c, Response<List<History>> r) {
                loading.postValue(false);
                if (r.isSuccessful()) items.postValue(r.body());
                else error.postValue("No se pudo cargar el historial");
            }
            @Override public void onFailure(Call<List<History>> c, Throwable t) {
                loading.postValue(false); error.postValue(t.getMessage());
            }
        });
    }
}
