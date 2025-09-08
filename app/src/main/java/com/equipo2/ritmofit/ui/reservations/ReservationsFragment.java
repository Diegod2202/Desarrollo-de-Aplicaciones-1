package com.equipo2.ritmofit.ui.reservations;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.equipo2.ritmofit.R;

@AndroidEntryPoint
public class ReservationsFragment extends Fragment {

    private ReservationsViewModel vm;
    private ReservationsAdapter adapter;

    public ReservationsFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reservations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        vm = new ViewModelProvider(this).get(ReservationsViewModel.class);

        RecyclerView rv = v.findViewById(R.id.recyclerReservations);
        ProgressBar progress = v.findViewById(R.id.progress);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReservationsAdapter(new ReservationsAdapter.OnAction() {
            @Override public void onOpen(com.equipo2.ritmofit.data.model.Reservation r) {
                Bundle args = new Bundle();
                args.putInt("classId", r.id);
                Navigation.findNavController(v).navigate(R.id.classDetailFragment, args);
            }

            @Override public void onCancel(com.equipo2.ritmofit.data.model.Reservation r) {
                vm.cancel(r.reservation_id);
            }
        });
        rv.setAdapter(adapter);

        vm.getLoading().observe(getViewLifecycleOwner(), l ->
                progress.setVisibility(Boolean.TRUE.equals(l) ? View.VISIBLE : View.GONE));
        vm.getReservations().observe(getViewLifecycleOwner(), adapter::submit);
        vm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
        });

        vm.load();
    }
}
