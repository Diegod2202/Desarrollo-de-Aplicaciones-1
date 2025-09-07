package com.equipo2.ritmofit.ui.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.equipo2.ritmofit.R;
import com.equipo2.ritmofit.data.TokenManager;

// ui/home/HomeFragment.java (reemplazá el contenido del onViewCreated)
@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private HomeViewModel vm;
    private ClassesAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        Button btnLogout = v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(view -> {
            // Borrar token
            new TokenManager(requireContext().getApplicationContext()).clear();
            // Navegar de vuelta a Login
            Navigation.findNavController(v).navigate(R.id.loginFragment);
        });

        vm = new ViewModelProvider(this).get(HomeViewModel.class);

        RecyclerView rv = v.findViewById(R.id.recyclerClasses);
        ProgressBar progress = v.findViewById(R.id.progress);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ClassesAdapter(item -> {
            // TODO: navegar a detalle pasando item.id
            // Navigation.findNavController(v).navigate(R.id.classDetailFragment, bundle);
        });
        rv.setAdapter(adapter);

        vm.getLoading().observe(getViewLifecycleOwner(), loading ->
                progress.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE));

        vm.getClasses().observe(getViewLifecycleOwner(), adapter::submit);

        vm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
        });

        // Cargar sin filtros por ahora
        vm.load(null, null, null);
    }
}
