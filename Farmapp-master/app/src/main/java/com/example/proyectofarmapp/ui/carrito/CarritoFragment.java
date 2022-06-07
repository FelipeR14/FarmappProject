package com.example.proyectofarmapp.ui.carrito;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofarmapp.CarritoAdapter;
import com.example.proyectofarmapp.ComprarActivity;
import com.example.proyectofarmapp.Product;
import com.example.proyectofarmapp.ProductAdapter;
import com.example.proyectofarmapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarritoFragment extends Fragment {

    private CarritoViewModel mViewModel;
    //CARGAR LISTA DE PRODUCTOS
    RecyclerView recycler;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    List<Product> lista;
    CarritoAdapter adapter;
    TextView tvTotalPagar;
    Button btnComprar;
    int totalPagar=0;
    int itemCount=0;
    public static CarritoFragment newInstance() {
        return new CarritoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.carrito_fragment, container, false);
        tvTotalPagar=view.findViewById(R.id.tvTotalPagar);
        btnComprar=view.findViewById(R.id.btnComprar);
        //INICIALIZAR VARIABLES
        lista=new ArrayList<>();
        adapter=new CarritoAdapter(lista,view.getContext(),getUid());
        recycler=view.findViewById(R.id.recyclerCarrito);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);
        loadData(view);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemCount==0){
                    Toast.makeText(view.getContext(), "El carrito esta vacío", Toast.LENGTH_SHORT).show();
                }else{
                    Intent i=new Intent(view.getContext(), ComprarActivity.class);
                    startActivity(i);
                    tvTotalPagar.setText("Total: $0");
                }

            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CarritoViewModel.class);
        // TODO: Use the ViewModel
    }
    public void loadData(View root){
        reference.child("Usuarios/"+getUid()+"/carrito").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.removeAll(lista);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    itemCount=itemCount+1;
                    Product listap = snapshot.getValue(Product.class);
                    lista.add(listap);
                    int precio=Integer.parseInt(listap.getPrecio());
                    int cantidad=Integer.parseInt(listap.getCantidad());
                    int totalProd=precio*cantidad;
                    /*Toast.makeText(root.getContext(), "Precio: "+precio
                            +"\nCantidad: "+cantidad
                            +"\nTotal Producto: "+totalProd, Toast.LENGTH_SHORT).show();*/
                    totalPagar=totalPagar+totalProd;
                }
                adapter.notifyDataSetChanged();
                tvTotalPagar.setText("Total: $"+totalPagar);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public String getUid(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user= auth.getCurrentUser();
        return user.getUid();
    }
}