package com.equipo2.ritmofit.ui.profile;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import com.equipo2.ritmofit.R;
import com.equipo2.ritmofit.data.model.User;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {

    private ProfileViewModel vm;
    private ProgressBar progress;
    private ImageView imgPhoto;
    private EditText inputPhoto, inputName, inputEmail;
    private Button btnSave;

    public ProfileFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        vm = new ViewModelProvider(this).get(ProfileViewModel.class);

        progress = v.findViewById(R.id.progress);
        imgPhoto = v.findViewById(R.id.imgPhoto);
        inputPhoto = v.findViewById(R.id.inputPhoto);
        inputName = v.findViewById(R.id.inputName);
        inputEmail = v.findViewById(R.id.inputEmail);
        btnSave = v.findViewById(R.id.btnSave);

        vm.getLoading().observe(getViewLifecycleOwner(), l ->
                progress.setVisibility(Boolean.TRUE.equals(l) ? View.VISIBLE : View.GONE));

        vm.getUser().observe(getViewLifecycleOwner(), this::bindUser);

        vm.getSaved().observe(getViewLifecycleOwner(), ok -> {
            if (Boolean.TRUE.equals(ok)) {
                Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
        });

        btnSave.setOnClickListener(view -> {
            String name = inputName.getText().toString().trim();
            String photo = inputPhoto.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(getContext(), "Ingresá un nombre", Toast.LENGTH_SHORT).show();
                return;
            }
            //aca iria validacion de foto en el put, pero esperemos a ver q dice el profe
            vm.save(name, TextUtils.isEmpty(photo) ? null : photo);
        });

        // Cargar perfil al entrar
        vm.load();
    }

    private void bindUser(User u){
        if (u == null) return;
        inputName.setText(u.name);
        inputEmail.setText(u.email);
        inputPhoto.setText(u.photo != null ? u.photo : "");
        // Ahora mismo no hay seleccion de foto, sino que ponemos una URL y agarraria esta, hay que ver que dice el profe sobre el sistema
    }
}
