package com.equipo2.ritmofit.ui.reservations;

import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.equipo2.ritmofit.R;
import com.equipo2.ritmofit.data.model.Reservation;
import java.util.*;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.VH> {
    public interface OnAction {
        void onOpen(Reservation r);
        void onCancel(Reservation r);
    }
    private final List<Reservation> data = new ArrayList<>();
    private final OnAction onAction;

    public ReservationsAdapter(OnAction onAction){ this.onAction = onAction; }

    public void submit(List<Reservation> items){
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int t) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_reservation, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Reservation r = data.get(pos);
        h.txtName.setText(r.name + " — " + r.profesor);
        h.txtInfo.setText(r.discipline + " • " + r.sede + " • " + r.fecha + " " + r.hora + " • " + r.status);

        h.btnOpen.setOnClickListener(v -> onAction.onOpen(r));
        h.btnCancel.setOnClickListener(v -> onAction.onCancel(r));
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtName, txtInfo;
        Button btnOpen, btnCancel;
        VH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            btnOpen = itemView.findViewById(R.id.btnOpen);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
