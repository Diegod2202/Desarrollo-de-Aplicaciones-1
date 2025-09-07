package com.equipo2.ritmofit.ui.home;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.equipo2.ritmofit.R;
import com.equipo2.ritmofit.data.model.GymClass;
import java.util.*;

public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.VH> {
    public interface OnItemClick { void onClick(GymClass item); }
    private final List<GymClass> data = new ArrayList<>();
    private final OnItemClick onItemClick;

    public ClassesAdapter(OnItemClick onItemClick){ this.onItemClick = onItemClick; }

    public void submit(List<GymClass> items){
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vType) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_class, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        GymClass it = data.get(pos);
        h.txtName.setText(it.name + " — " + it.profesor);
        h.txtInfo.setText(it.discipline + " • " + it.sede + " • " + it.fecha + " " + it.hora);
        h.txtCupo.setText("Cupo: " + it.cupo);
        h.itemView.setOnClickListener(v -> onItemClick.onClick(it));
    }

    @Override public int getItemCount(){ return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtName, txtInfo, txtCupo;
        VH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtInfo = itemView.findViewById(R.id.txtInfo);
            txtCupo = itemView.findViewById(R.id.txtCupo);
        }
    }
}
