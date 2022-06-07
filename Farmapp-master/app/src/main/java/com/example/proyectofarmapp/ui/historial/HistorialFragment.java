package com.example.proyectofarmapp.ui.historial;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofarmapp.HistorialAdapter;
import com.example.proyectofarmapp.HistorialList;
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

public class HistorialFragment extends Fragment {

    private HistorialViewModel mViewModel;
    //CARGAR LISTA DE PRODUCTOS
    RecyclerView recycler;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    List<HistorialList> lista;
    HistorialAdapter adapter;
    public static HistorialFragment newInstance() {
        return new HistorialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.historial_fragment, container, false);
        //INICIALIZAR VARIABLES
        lista=new ArrayList<>();
        adapter=new HistorialAdapter(lista,view.getContext());
        recycler=view.findViewById(R.id.recyclerHistorial);
        recycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recycler.setAdapter(adapter);
        loadData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HistorialViewModel.class);
        // TODO: Use the ViewModel
    }

    public void loadData(){
        reference.child("Usuarios/"+getUid()+"/Historial").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lista.removeAll(lista);
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    HistorialList listap = snapshot.getValue(HistorialList.class);
                    lista.add(listap);
                }
                adapter.notifyDataSetChanged();
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