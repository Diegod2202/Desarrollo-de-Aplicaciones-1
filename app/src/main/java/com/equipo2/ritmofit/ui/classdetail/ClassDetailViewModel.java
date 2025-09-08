package com.equipo2.ritmofit.ui.classdetail;

import androidx.lifecycle.*;
import com.equipo2.ritmofit.data.model.GymClass;
import com.equipo2.ritmofit.data.model.ReservationResponse;
import com.equipo2.ritmofit.data.repository.ClassRepository;
import com.equipo2.ritmofit.data.repository.ReservationRepository;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.*;

@HiltViewModel
public class ClassDetailViewModel extends ViewModel {
    private final ClassRepository classRepo;
    private final ReservationRepository resRepo;

    private final MutableLiveData<GymClass> gymClass = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> alreadyReserved = new MutableLiveData<>(false);
    private final MutableLiveData<Integer> createdReservationId = new MutableLiveData<>(null);

    // 👇 banderas internas (no LiveData)
    private volatile boolean reserving = false;

    @Inject
    public ClassDetailViewModel(ClassRepository c, ReservationRepository r) {
        this.classRepo = c; this.resRepo = r;
    }

    public LiveData<GymClass> getGymClass(){ return gymClass; }
    public LiveData<Boolean> getLoading(){ return loading; }
    public LiveData<String> getError(){ return error; }
    public LiveData<Boolean> getAlreadyReserved(){ return alreadyReserved; }
    public LiveData<Integer> getCreatedReservationId(){ return createdReservationId; }

    public void load(int classId) {
        loading.postValue(true); error.postValue(null);
        classRepo.getClassById(classId).enqueue(new retrofit2.Callback<GymClass>() {
            @Override public void onResponse(retrofit2.Call<GymClass> c, retrofit2.Response<GymClass> r) {
                loading.postValue(false);
                if (r.isSuccessful()) gymClass.postValue(r.body());
                else error.postValue("No se pudo cargar el detalle");
            }
            @Override public void onFailure(retrofit2.Call<GymClass> c, Throwable t) {
                loading.postValue(false); error.postValue(t.getMessage());
            }
        });
    }

    public void checkAlreadyReserved(int classId){
        resRepo.getMine().enqueue(new retrofit2.Callback<java.util.List<com.equipo2.ritmofit.data.model.Reservation>>() {
            @Override public void onResponse(retrofit2.Call<java.util.List<com.equipo2.ritmofit.data.model.Reservation>> c,
                                             retrofit2.Response<java.util.List<com.equipo2.ritmofit.data.model.Reservation>> r) {
                if (r.isSuccessful() && r.body()!=null) {
                    boolean has = false;
                    for (var res : r.body()) {
                        if (res.id == classId && "confirmada".equalsIgnoreCase(res.status)) { has = true; break; }
                    }
                    alreadyReserved.postValue(has);
                }
            }
            @Override public void onFailure(retrofit2.Call<java.util.List<com.equipo2.ritmofit.data.model.Reservation>> c, Throwable t) {
                // si falla, no bloqueamos la UI
            }
        });
    }

    public void reservar(int classId){
        // 🔒 Guard de seguridad
        Boolean ar = alreadyReserved.getValue();
        if (ar != null && ar) return;        // ya reservada
        if (reserving) return;               // ya hay request en vuelo
        reserving = true;                    // bloqueo

        loading.postValue(true); error.postValue(null);
        resRepo.create(classId).enqueue(new retrofit2.Callback<com.equipo2.ritmofit.data.model.ReservationResponse>() {
            @Override public void onResponse(retrofit2.Call<com.equipo2.ritmofit.data.model.ReservationResponse> c,
                                             retrofit2.Response<com.equipo2.ritmofit.data.model.ReservationResponse> r) {
                reserving = false;
                loading.postValue(false);
                if (r.isSuccessful() && r.body()!=null) {
                    createdReservationId.postValue(r.body().reservationId);
                    alreadyReserved.postValue(true); // ✅ marcaremos UI como “Reservada”
                } else {
                    error.postValue("No se pudo reservar (cupo lleno o ya reservada)");
                }
            }
            @Override public void onFailure(retrofit2.Call<com.equipo2.ritmofit.data.model.ReservationResponse> c, Throwable t) {
                reserving = false;
                loading.postValue(false);
                error.postValue(t.getMessage());
            }
        });
    }
}

