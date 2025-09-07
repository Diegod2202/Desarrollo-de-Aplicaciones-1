package com.equipo2.ritmofit.ui.profile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;
import com.equipo2.ritmofit.R;
import com.equipo2.ritmofit.data.TokenManager;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    public ProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        Button btnLogout = v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(view -> {
            // Borro token
            new TokenManager(requireContext().getApplicationContext()).clear();
            // Vuelvo a Login
            Navigation.findNavController(v).navigate(R.id.loginFragment);
        });
    }
}
