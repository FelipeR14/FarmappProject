package com.example.proyectofarmapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.Historialadapter> {

    List<HistorialList> lista;
    private final Context mainContext;

    public HistorialAdapter(List<HistorialList> lista, Context context) {
        this.lista = lista;
        this.mainContext = context;
    }

    @Override
    public HistorialAdapter.Historialadapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_model, parent, false);
        HistorialAdapter.Historialadapter holder = new HistorialAdapter.Historialadapter(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HistorialAdapter.Historialadapter holder, int position) {
        final HistorialList item = lista.get(position);
        holder.fecha.setText("Fecha: "+item.getFecha().replace("-","/"));
        holder.totalPagado.setText("$"+item.getTotalPagado());
        holder.totalpiezas.setText("Total de piezas: "+item.getTotalPiezas());
        holder.totalproductos.setText("Productos adquiridos: "+item.getProductos());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class Historialadapter extends RecyclerView.ViewHolder {
        TextView fecha, totalproductos,totalpiezas;
        Button totalPagado;
        public Historialadapter(View itemView) {
            super(itemView);
            this.fecha=itemView.findViewById(R.id.tvfecha);
            this.totalpiezas=itemView.findViewById(R.id.tvPiezas);
            this.totalPagado=itemView.findViewById(R.id.tvPagado);
            this.totalproductos=itemView.findViewById(R.id.tvProductos);


        }
    }

}
