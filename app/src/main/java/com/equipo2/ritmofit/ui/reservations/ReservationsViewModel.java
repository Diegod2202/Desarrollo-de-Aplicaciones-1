package com.equipo2.ritmofit.ui.reservations;

import androidx.lifecycle.*;
import com.equipo2.ritmofit.data.model.Reservation;
import com.equipo2.ritmofit.data.repository.ReservationRepository;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.*;

@HiltViewModel
public class ReservationsViewModel extends ViewModel {
    private final ReservationRepository repo;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<List<Reservation>> reservations = new MutableLiveData<>();

    @Inject public ReservationsViewModel(ReservationRepository repo){ this.repo = repo; }

    public LiveData<Boolean> getLoading(){ return loading; }
    public LiveData<String> getError(){ return error; }
    public LiveData<List<Reservation>> getReservations(){ return reservations; }

    public void load(){
        loading.postValue(true); error.postValue(null);
        repo.getMine().enqueue(new Callback<List<Reservation>>() {
            @Override public void onResponse(Call<List<Reservation>> c, Response<List<Reservation>> r) {
                loading.postValue(false);
                if (r.isSuccessful()) reservations.postValue(r.body());
                else error.postValue("No se pudieron cargar las reservas");
            }
            @Override public void onFailure(Call<List<Reservation>> c, Throwable t) {
                loading.postValue(false); error.postValue(t.getMessage());
            }
        });
    }

    public void cancel(int reservationId){
        loading.postValue(true); error.postValue(null);
        repo.cancel(reservationId).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> c, Response<Void> r) {
                loading.postValue(false);
                if (r.isSuccessful()) load(); else error.postValue("No se pudo cancelar");
            }
            @Override public void onFailure(Call<Void> c, Throwable t) {
                loading.postValue(false); error.postValue(t.getMessage());
            }
        });
    }
}
