package com.equipo2.ritmofit.di;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.equipo2.ritmofit.data.TokenManager;
import com.equipo2.ritmofit.data.network.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    private static final String BASE_URL = "http://10.0.2.2:4000/api/";

    @Provides @Singleton
    public Gson provideGson() {
        return new GsonBuilder().create();
    }

    @Provides @Singleton
    public OkHttpClient provideOkHttpClient(TokenManager tokenManager) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Agrego Authorization si hay token
        Interceptor authInterceptor = chain -> {
            var original = chain.request();
            var builder = original.newBuilder();
            String token = tokenManager.getToken();
            if (token != null && !token.isEmpty()) {
                builder.header("Authorization", "Bearer " + token);
            }
            return chain.proceed(builder.build());
        };

        // Si el servidor responde 401, limpiamos el token
        Interceptor unauthorizedInterceptor = chain -> {
            var response = chain.proceed(chain.request());
            if (response.code() == 401) {
                tokenManager.clear();
                // devuelvo igual la respuesta, la UI verá error y al no tener token el próximo ingreso te lleva a Login
            }
            return response;
        };

        return new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(unauthorizedInterceptor)
                .addInterceptor(logging)
                .build();
    }

    @Provides @Singleton
    public Retrofit provideRetrofit(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides @Singleton
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}
