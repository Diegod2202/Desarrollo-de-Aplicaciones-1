package com.equipo2.ritmofit.ui.history;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.equipo2.ritmofit.R;
import com.equipo2.ritmofit.data.model.History;
import java.util.*;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.VH> {
    private final List<History> data = new ArrayList<>();

    public void submit(List<History> items){
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_history, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        History it = data.get(pos);
        h.txtName.setText(it.name + " — " + it.profesor);
        h.txtInfo.setText(it.discipline + " • " + it.sede + " • " + it.fecha + " " + it.hora);
        // mostrar solo fecha/hora de asistencia (cortamos la 'T' si viene ISO)
        String asis = it.asistencia_fecha;
        if (asis != null && asis.contains("T")) asis = asis.replace("T", " ").replace("Z", "");
        h.txtAsistencia.setText("Asistió: " + asis);
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtName, txtInfo, txtAsistencia;
        VH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            txtAsistencia = itemView.findViewById(R.id.txtAsistencia);
        }
    }
}
