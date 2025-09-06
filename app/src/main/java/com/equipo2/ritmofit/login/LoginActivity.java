package com.equipo2.ritmofit.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.equipo2.ritmofit.data.auth.SessionManager;
import com.equipo2.ritmofit.databinding.ActivityLoginBinding;
import com.equipo2.ritmofit.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        session = new SessionManager(this);

        // Si ya está logueado, salteamos login
        if (session.isLoggedIn()) {
            goToMain();
            return;
        }

        binding.btnSendOtp.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
                return;
            }
            sendOtp(email);
        });

        binding.btnVerify.setOnClickListener(v -> {
            String otp = binding.etOtp.getText().toString().trim();
            if (otp.isEmpty()) {
                Toast.makeText(this, "OTP is required", Toast.LENGTH_SHORT).show();
                return;
            }
            verifyOtpAndLogin(otp);
        });
    }

    private void sendOtp(String email) {
        // TODO: call backend with Retrofit
        binding.progress.setVisibility(View.VISIBLE);

        // Demo: simulate success
        binding.progress.postDelayed(() -> {
            binding.progress.setVisibility(View.GONE);
            Toast.makeText(this, "OTP sent to " + email, Toast.LENGTH_SHORT).show();
        }, 800);
    }

    private void verifyOtpAndLogin(String otp) {
        // TODO: call backend to verify OTP and get session token
        binding.progress.setVisibility(View.VISIBLE);

        // Demo: simulate token
        binding.progress.postDelayed(() -> {
            binding.progress.setVisibility(View.GONE);
            String fakeToken = "demo_token_123";
            session.saveToken(fakeToken);
            goToMain();
        }, 800);
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        // Limpiar back stack para que el Back no vuelva a Login
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // finish() no es necesario por los flags, pero no molesta
        finish();
    }
}
