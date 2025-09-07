package com.equipo2.ritmofit.ui.auth;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.navigation.Navigation;
import com.equipo2.ritmofit.R;
import com.equipo2.ritmofit.data.TokenManager;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private AuthViewModel vm;
    private EditText inputEmail, inputOtp;
    private Button btnRequestOtp, btnVerify;
    private TextView txtStatus;

    public LoginFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        // Si ya hay token guardado, navego directo
        TokenManager tm = new TokenManager(requireContext().getApplicationContext());
        if (tm.getToken() != null) {
            Navigation.findNavController(v).navigate(R.id.homeFragment);
            return;
        }

        vm = new ViewModelProvider(this).get(AuthViewModel.class);

        inputEmail = v.findViewById(R.id.inputEmail);
        inputOtp = v.findViewById(R.id.inputOtp);
        btnRequestOtp = v.findViewById(R.id.btnRequestOtp);
        btnVerify = v.findViewById(R.id.btnVerify);
        txtStatus = v.findViewById(R.id.txtStatus);

        btnRequestOtp.setOnClickListener(view -> {
            String email = inputEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Ingresá un email", Toast.LENGTH_SHORT).show();
                return;
            }
            vm.requestOtp(email);
        });

        btnVerify.setOnClickListener(view -> {
            String email = inputEmail.getText().toString().trim();
            String code = inputOtp.getText().toString().trim();
            if (TextUtils.isEmpty(code)) {
                Toast.makeText(getContext(), "Ingresá el código", Toast.LENGTH_SHORT).show();
                return;
            }
            vm.verifyOtp(email, code);
        });

        vm.getLoading().observe(getViewLifecycleOwner(), loading -> {
            txtStatus.setText(loading != null && loading ? "Procesando..." : "");
        });

        vm.getOtpSent().observe(getViewLifecycleOwner(), sent -> {
            if (sent != null && sent) {
                inputOtp.setVisibility(View.VISIBLE);
                btnVerify.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Código enviado a tu email", Toast.LENGTH_SHORT).show();
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
        });

        vm.getLoggedIn().observe(getViewLifecycleOwner(), ok -> {
            if (ok != null && ok) {
                // Navegar a Home
                Navigation.findNavController(v).navigate(R.id.homeFragment);
            }
        });
    }
}
