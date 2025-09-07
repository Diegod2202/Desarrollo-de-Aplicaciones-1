package com.equipo2.ritmofit.data.repository;

import com.equipo2.ritmofit.data.model.GymClass;
import com.equipo2.ritmofit.data.network.ApiService;
import java.util.List;
import javax.inject.Inject;
import retrofit2.Call;

public class ClassRepository {
    private final ApiService api;
    @Inject public ClassRepository(ApiService api){ this.api = api; }
    public Call<List<GymClass>> getClasses(String disciplina, String sede, String fecha){
        return api.getClasses(disciplina, sede, fecha);
    }
}
