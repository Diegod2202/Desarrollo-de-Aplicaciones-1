package com.equipo2.ritmofit.data.repository;

import com.equipo2.ritmofit.data.model.Reservation;
import com.equipo2.ritmofit.data.model.ReservationResponse;
import com.equipo2.ritmofit.data.model.ReservationRequest;
import com.equipo2.ritmofit.data.network.ApiService;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;

public class ReservationRepository {
    private final ApiService api;
    @Inject public ReservationRepository(ApiService api){ this.api = api; }

    public Call<List<Reservation>> getMine(){ return api.getMyReservations(); }
    public Call<Void> cancel(int reservationId){ return api.cancelReservation(reservationId); }
    public Call<ReservationResponse> create(int classId){ return api.createReservation(new ReservationRequest(classId)); }
}
