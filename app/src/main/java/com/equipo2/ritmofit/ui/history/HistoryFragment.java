package com.equipo2.ritmofit.ui.history;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.hilt.android.AndroidEntryPoint;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.*;
import android.widget.*;

import com.equipo2.ritmofit.R;

@AndroidEntryPoint
public class HistoryFragment extends Fragment {

    private HistoryViewModel vm;
    private HistoryAdapter adapter;

    public HistoryFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        vm = new ViewModelProvider(this).get(HistoryViewModel.class);

        EditText inputFrom = v.findViewById(R.id.inputFrom);
        EditText inputTo   = v.findViewById(R.id.inputTo);
        Button btnFilter   = v.findViewById(R.id.btnFilter);
        RecyclerView rv    = v.findViewById(R.id.recyclerHistory);
        ProgressBar progress = v.findViewById(R.id.progress);
        TextView empty = v.findViewById(R.id.emptyView);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new HistoryAdapter();
        rv.setAdapter(adapter);

        vm.getLoading().observe(getViewLifecycleOwner(), l ->
                progress.setVisibility(Boolean.TRUE.equals(l) ? View.VISIBLE : View.GONE));

        vm.getItems().observe(getViewLifecycleOwner(), list -> {
            adapter.submit(list);
            boolean isEmpty = (list == null || list.isEmpty());
            empty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        });

        vm.getError().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(getContext(), err, Toast.LENGTH_LONG).show();
        });

        // Cargar todo por defecto
        vm.load(null, null);

        btnFilter.setOnClickListener(view -> {
            String from = inputFrom.getText().toString().trim();
            String to   = inputTo.getText().toString().trim();
            if (TextUtils.isEmpty(from)) from = null;
            if (TextUtils.isEmpty(to)) to = null;
            vm.load(from, to);
        });
    }
}
