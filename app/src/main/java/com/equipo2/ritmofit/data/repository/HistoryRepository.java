package com.equipo2.ritmofit.data.repository;

import com.equipo2.ritmofit.data.model.History;
import com.equipo2.ritmofit.data.network.ApiService;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;

public class HistoryRepository {
    private final ApiService api;
    @Inject public HistoryRepository(ApiService api){ this.api = api; }
    public Call<List<History>> get(String from, String to){ return api.getHistory(from, to); }
}
