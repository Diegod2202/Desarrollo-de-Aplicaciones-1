package com.equipo2.ritmofit;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.equipo2.ritmofit.data.TokenManager;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);

        // ✅ Obtener NavController de forma segura
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private boolean safeNavigate(int destId) {
        if (navController == null || navController.getCurrentDestination() == null) return false;
        if (navController.getCurrentDestination().getId() == destId) return true; // ya estás ahí
        try {
            navController.navigate(destId);
            return true;
        } catch (Exception e) {
            // evita crasheos si el destino no cuelga del actual
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            return safeNavigate(R.id.homeFragment);
        } else if (id == R.id.nav_reservations) {
            return safeNavigate(R.id.reservationsFragment);
        } else if (id == R.id.nav_history) {
            return safeNavigate(R.id.historyFragment);
        } else if (id == R.id.nav_profile) {
            return safeNavigate(R.id.profileFragment);
        } else if (id == R.id.action_logout) {
            new TokenManager(getApplicationContext()).clear();
            return safeNavigate(R.id.loginFragment);
        }

        return super.onOptionsItemSelected(item);
    }
}
