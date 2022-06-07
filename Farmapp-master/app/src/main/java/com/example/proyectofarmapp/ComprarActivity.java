package com.example.proyectofarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class ComprarActivity extends AppCompatActivity {
    EditText calle, colonia, numint, numext, cp;
    Button btnConfirmar;
    int prodCont=0;
    int totalPiezas=0;
    int totalProd=0;
    int totalPagado=0;
    String fecha, total;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar);
        userID=getUID();
        calle=findViewById(R.id.etCalle);
        colonia=findViewById(R.id.etColonia);
        numint=findViewById(R.id.etNumInt);
        numext=findViewById(R.id.etNumExt);
        cp=findViewById(R.id.etCP);
        btnConfirmar=findViewById(R.id.btnConfirmarCompra);
        Calendar c = Calendar.getInstance();
        int dia=c.get(Calendar.DATE);
        int mes=(c.get(Calendar.MONTH))+1;
        int año=c.get(Calendar.YEAR);
        fecha=dia+"-"+mes+"-"+año;
        loadData();
        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAndBuy();
            }
        });
    }

    public String getUID(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        return user.getUid();
    }

    public void loadData(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Usuarios/"+getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String strCalle=snapshot.child("Calle").getValue(String.class);
                calle.setText(strCalle);
                String strNumInt=snapshot.child("NumInt").getValue(String.class);
                numint.setText(strNumInt);
                String strNumExt=snapshot.child("NumExt").getValue(String.class);
                numext.setText(strNumExt);
                String strCol=snapshot.child("Colonia").getValue(String.class);
                colonia.setText(strCol);
                String strCp=snapshot.child("CodPostal").getValue(String.class);
                cp.setText(strCp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void updateAndBuy(){
        //AQUI VAN LAS VALIDACIONES
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> userData=new HashMap<>();
        userData.put("Calle",calle.getText().toString());
        userData.put("NumInt",numint.getText().toString());
        userData.put("NumExt",numext.getText().toString());
        userData.put("Colonia",colonia.getText().toString());
        userData.put("CodPostal",cp.getText().toString());
        ref.child("Usuarios/"+userID).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ComprarActivity.this,"Datos de envio actualizados", Toast.LENGTH_SHORT).show();
                    copyCartData();
                }else{
                    Toast.makeText(ComprarActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void copyCartData(){
        DatabaseReference cartRef=FirebaseDatabase.getInstance().getReference();
        cartRef.child("Usuarios/"+userID+"/carrito").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot :
                        dataSnapshot.getChildren()) {
                    Product listap = snapshot.getValue(Product.class);
                    int precio=Integer.parseInt(listap.getPrecio());
                    int cantidad=Integer.parseInt(listap.getCantidad());
                    totalProd=precio*cantidad;
                    prodCont=prodCont+1;
                    totalPiezas=totalPiezas+cantidad;
                    totalPagado=totalPagado+totalProd;
                    /*Toast.makeText(ComprarActivity.this, "Precio: "+precio
                            +"\nCantidad: "+cantidad
                            +"\nTotal Producto: "+totalProd
                            +"\nTotal Acumulado: "+totalPagado, Toast.LENGTH_SHORT).show();*/
                    Log.d("PRUEBA-COMPRAS", "onDataChange: "+"Precio: "+precio
                            +"\nCantidad: "+cantidad
                            +"\nTotal Producto: "+totalProd
                            +"\nTotal Acumulado: "+totalPagado);
                    changeStock(listap.getId(),cantidad);
                }
                total=String.valueOf(totalProd);
                saveNewMove();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void saveNewMove(){
        DatabaseReference historial=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> movementData=new HashMap<>();
        movementData.put("Fecha",fecha);
        movementData.put("Productos",String.valueOf(prodCont));
        movementData.put("TotalPiezas",String.valueOf(totalPiezas));
        movementData.put("TotalPagado",String.valueOf(totalPagado));
        historial.child("Usuarios/"+userID+"/Historial").push().setValue(movementData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    deleteCart();
                }else{

                }
            }
        });
    }

    public void deleteCart(){
        DatabaseReference deleteRef=FirebaseDatabase.getInstance().getReference();
        deleteRef.child("Usuarios/"+userID+"/carrito").setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ComprarActivity.this, "Orden Creada, Gracias por su compra", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(ComprarActivity.this, "Error al vaciar el carrito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void changeStock(String id, int piezas){
        DatabaseReference prodReference=FirebaseDatabase.getInstance().getReference();
        prodReference.child("Productos/"+id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int existenciaAnterior=Integer.parseInt(snapshot.child("existencia").getValue(String.class));
                int existenciaNueva=existenciaAnterior-piezas;
                HashMap<String, Object> existencia=new HashMap<>();
                existencia.put("existencia",String.valueOf(existenciaNueva));
                prodReference.child("Productos/"+id).updateChildren(existencia).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("Existencias", "onComplete: Existencia de: "+id+" actualizado a: "+existenciaNueva);
                        }else{
                            Toast.makeText(ComprarActivity.this, "Error al actualizar Stock: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}