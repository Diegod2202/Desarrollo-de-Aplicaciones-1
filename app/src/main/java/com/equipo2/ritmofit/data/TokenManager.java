package com.equipo2.ritmofit.data;

import android.content.Context;
import android.content.SharedPreferences;
import javax.inject.Inject;
import javax.inject.Singleton;
import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class TokenManager {
    private final SharedPreferences prefs;
    private static final String KEY = "auth_token";

    @Inject
    public TokenManager(@ApplicationContext Context ctx){
        this.prefs = ctx.getSharedPreferences("ritmofit_prefs", Context.MODE_PRIVATE);
    }
    public void saveToken(String token){ prefs.edit().putString(KEY, token).apply(); }
    public String getToken(){ return prefs.getString(KEY, null); }
    public void clear(){ prefs.edit().remove(KEY).apply(); }
}
