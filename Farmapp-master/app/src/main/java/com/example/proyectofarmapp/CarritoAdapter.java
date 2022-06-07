package com.example.proyectofarmapp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class CarritoAdapter extends RecyclerView.Adapter<CarritoAdapter.Carritoadapter> {

        List<Product> lista;
        String userId;
        private final Context mainContext;

        public CarritoAdapter(List<Product> lista, Context context,String userId) {
                this.lista = lista;
                this.mainContext = context;
                this.userId=userId;
                }

        @Override
        public CarritoAdapter.Carritoadapter onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.carrito_model, parent, false);
            CarritoAdapter.Carritoadapter holder = new CarritoAdapter.Carritoadapter(v);
                return holder;
                }

        @Override
        public void onBindViewHolder(final CarritoAdapter.Carritoadapter holder, int position) {
        final Product item = lista.get(position);
                holder.nombre.setText(item.getNombre());
                holder.btnPrecio.setText("$"+item.getPrecio());
                holder.ingrediente.setText(item.getIngrediente());
                holder.contenido.setText(item.getContenido());
                holder.cantidad.setText(item.getCantidad()+" pz.");
                String imgUrl="https://firebasestorage.googleapis.com/v0/b/farmapp-bdcd2.appspot.com/o/ProductPictures%2F"+item.getImgUrl()+"?alt=media&";
                Glide.with(mainContext).load(imgUrl).into(holder.producto);
                }

        @Override
        public int getItemCount() {
                return lista.size();
                }

        public static class Carritoadapter extends RecyclerView.ViewHolder {
            TextView nombre, ingrediente, contenido, cantidad;
            Button btnPrecio;
            ImageView producto;
            public Carritoadapter(View itemView) {
                super(itemView);
                this.nombre=itemView.findViewById(R.id.tvNombreProd);
                this.producto=itemView.findViewById(R.id.productImage);
                this.ingrediente=itemView.findViewById(R.id.tvIngredProd);
                this.contenido=itemView.findViewById(R.id.tvcontProd);
                this.btnPrecio=itemView.findViewById(R.id.btnPrecio);
                this.cantidad=itemView.findViewById(R.id.tvCantidadCom);

            }
        }

}
