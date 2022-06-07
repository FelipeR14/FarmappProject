package com.example.proyectofarmapp;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Productadapter> {

    List<Product> lista;
    String userId;
    private final Context mainContext;

    public ProductAdapter(List<Product> lista, Context context,String userId) {
        this.lista = lista;
        this.mainContext = context;
        this.userId=userId;
    }

    @Override
    public ProductAdapter.Productadapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_model, parent, false);
        ProductAdapter.Productadapter holder = new ProductAdapter.Productadapter(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductAdapter.Productadapter holder, int position) {
        final Product item = lista.get(position);
        holder.nombre.setText(item.getNombre());
        holder.btnPrecio.setText("$"+item.getPrecio());
        holder.ingrediente.setText(item.getIngrediente());
        holder.contenido.setText(item.getContenido());
        String imgUrl="https://firebasestorage.googleapis.com/v0/b/farmapp-bdcd2.appspot.com/o/ProductPictures%2F"+item.getImgUrl()+"?alt=media&";
        Glide.with(mainContext).load(imgUrl).into(holder.producto);
        holder.btnPrecio.setOnClickListener(v -> addToCart(userId,item.getNombre(),
                item.getIngrediente(),item.getContenido(),item.getPrecio(),item.getImgUrl(),item.getId(),item.getExistencia()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class Productadapter extends RecyclerView.ViewHolder {
        TextView nombre, ingrediente, contenido;
        Button btnPrecio;
        ImageView producto;
        public Productadapter(View itemView) {
            super(itemView);
            this.nombre=itemView.findViewById(R.id.tvNombreProd);
            this.producto=itemView.findViewById(R.id.productImage);
            this.ingrediente=itemView.findViewById(R.id.tvIngredProd);
            this.contenido=itemView.findViewById(R.id.tvcontProd);
            this.btnPrecio=itemView.findViewById(R.id.btnPrecio);

        }
    }

    public void addToCart(String userId,String nombre, String ingrediente, String contenido,
                          String precio, String imgUrl,String id, String existencia){

        HashMap<String, Object> cartData=new HashMap<>();
        cartData.put("nombre",nombre);
        cartData.put("ingrediente",ingrediente);
        cartData.put("contenido",contenido);
        cartData.put("precio",precio);
        cartData.put("existencia",existencia);
        cartData.put("imgUrl",imgUrl);
        cartData.put("id",id);
        Dialog dialogo=new Dialog(mainContext);
        dialogo.setCancelable(true);
        dialogo.setContentView(R.layout.cantidad_layout);
        TextView tvExistencia=dialogo.findViewById(R.id.tvExistencia);
        tvExistencia.setText("Existencia: "+existencia);
        EditText etCantidad=dialogo.findViewById(R.id.etCantidad);
        Button btnAñadirCarrito=dialogo.findViewById(R.id.btnAgregarAlCarrito);
        dialogo.show();
        btnAñadirCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cantidad=etCantidad.getText().toString();
                if(Integer.parseInt(cantidad)>Integer.parseInt(existencia)){
                    Toast.makeText(mainContext, "No hay suficiente existencia, elije una cantidad menor", Toast.LENGTH_SHORT).show();
                }else{
                    cartData.put("cantidad",cantidad);
                    DatabaseReference cartRef= FirebaseDatabase.getInstance().getReference();
                    cartRef.child("Usuarios/"+userId+"/carrito/"+id).updateChildren(cartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(mainContext, "Añadido al carrito", Toast.LENGTH_SHORT).show();
                                dialogo.dismiss();
                            }else{
                                Toast.makeText(mainContext, "Error al añadir al carrito", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
