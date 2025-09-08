package com.equipo2.ritmofit.ui.classdetail;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import androidx.lifecycle.ViewModelProvider;

import android.view.*;
import android.widget.*;
import com.equipo2.ritmofit.R;
import com.equipo2.ritmofit.data.model.GymClass;

@AndroidEntryPoint
public class ClassDetailFragment extends Fragment {

    private ClassDetailViewModel vm;
    private TextView txtName, txtInfo, txtCupo;
    private ProgressBar progress;
    private Button btnReservar;
    private int classId;

    public ClassDetailFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        vm = new ViewModelProvider(this).get(ClassDetailViewModel.class);

        txtName = v.findViewById(R.id.txtName);
        txtInfo = v.findViewById(R.id.txtInfo);
        txtCupo = v.findViewById(R.id.txtCupo);
        progress = v.findViewById(R.id.progress);
        btnReservar = v.findViewById(R.id.btnReservar);

        classId = requireArguments().getInt("classId");
        vm.load(classId);
        vm.checkAlreadyReserved(classId);

        vm.getLoading().observe(getViewLifecycleOwner(), l ->
                progress.setVisibility(Boolean.TRUE.equals(l) ? View.VISIBLE : View.GONE));

        vm.getGymClass().observe(getViewLifecycleOwner(), gc -> { if (gc != null) bind(gc); });

        vm.getAlreadyReserved().observe(getViewLifecycleOwner(), already -> {
            if (Boolean.TRUE.equals(already)) {
                btnReservar.setEnabled(false);
                btnReservar.setText("Reservada");
            } else {
                btnReservar.setEnabled(true);
                btnReservar.setText("Reservar");
            }
        });

        vm.getCreatedReservationId().observe(getViewLifecycleOwner(), id -> {
            if (id != null) {
                Toast.makeText(getContext(), "Reserva creada (#" + id + ")", Toast.LENGTH_SHORT).show();
                // El observer de alreadyReserved ya dejó el botón en “Reservada”
            }
        });

        vm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
        });

        btnReservar.setOnClickListener(view -> {
            // ⛔️ Deshabilitar inmediatamente para evitar taps múltiples
            btnReservar.setEnabled(false);
            btnReservar.setText("Reservando...");
            vm.reservar(classId);
            // El observer de alreadyReserved/ error volverá a dejar el botón como corresponda
        });
    }

    private void bind(GymClass c){
        txtName.setText(c.name + " — " + c.profesor);
        txtInfo.setText(c.discipline + " • " + c.sede + " • " + c.fecha + " " + c.hora);
        txtCupo.setText("Cupo: " + c.cupo);
    }
}
