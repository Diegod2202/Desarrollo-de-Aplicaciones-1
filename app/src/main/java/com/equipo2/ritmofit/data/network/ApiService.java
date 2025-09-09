package com.equipo2.ritmofit.data.network;

import com.equipo2.ritmofit.data.model.GymClass;
import com.equipo2.ritmofit.data.model.History;
import com.equipo2.ritmofit.data.model.LoginResponse;
import com.equipo2.ritmofit.data.model.OtpRequest;
import com.equipo2.ritmofit.data.model.Reservation;
import com.equipo2.ritmofit.data.model.ReservationRequest;
import com.equipo2.ritmofit.data.model.ReservationResponse;
import com.equipo2.ritmofit.data.model.User;
import com.equipo2.ritmofit.data.model.UserResponse;
import com.equipo2.ritmofit.data.model.VerifyOtpRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("auth/request-otp")
    Call<Void> requestOtp(@Body OtpRequest body);

    @POST("auth/verify-otp")
    Call<LoginResponse> verifyOtp(@Body VerifyOtpRequest body);

    @GET("classes")
    Call<List<GymClass>> getClasses(
            @Query("disciplina") String disciplina,
            @Query("sede") String sede,
            @Query("fecha") String fecha
    );

    @GET("classes/{id}")
    Call<GymClass> getClassById(@Path("id") int id);

    @POST("reservations")
    Call<ReservationResponse> createReservation(@Body ReservationRequest req);

    @GET("reservations/me")
    Call<List<Reservation>> getMyReservations();

    @DELETE("reservations/{id}")
    Call<Void> cancelReservation(@Path("id") int id);

    @GET("history")
    Call<java.util.List<History>> getHistory(
            @Query("from") String from,
            @Query("to") String to
    );

    @GET("users/me")
    Call<User> getProfile();

    @PUT("users/me")
    Call<UserResponse> updateProfile(
            @Body User body);


}
