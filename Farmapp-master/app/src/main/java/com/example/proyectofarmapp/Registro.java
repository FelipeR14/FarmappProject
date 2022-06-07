package com.example.proyectofarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {
    Button btnRegistro;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ProyectoFarmapp_Login);
        setContentView(R.layout.activity_registro);
        btnRegistro=findViewById(R.id.btnEnviar);
        
        btnRegistro.setOnClickListener(v -> registrarUsuario());
    }

    public void registrarUsuario(){

        EditText txtEmail = findViewById(R.id.editTextEmail);
        EditText txtUsuario = findViewById(R.id.editTextUsuario);
        EditText txtContraseña = findViewById(R.id.editTextContraseña);
        EditText txtTelefono = findViewById(R.id.editTextTelefono);
        CheckBox cb = findViewById(R.id.checkBox);

        String mensaje = "";
        Toast aviso;

        if(txtEmail.length()==0) {
            mensaje = "Escriba su email por favor";
            aviso = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
            aviso.show();
        }else if(txtUsuario.length()==0) {
            mensaje = "Escriba su usuario por favor";
            aviso = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
            aviso.show();
        }else if(txtTelefono.getText().toString().length()==0) {
            mensaje = "Escriba su telefono por favor";
            aviso = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
            aviso.show();
        }else if(txtContraseña.length()==0||txtContraseña.getText().toString().length()<6) {
            mensaje = "Escriba su contraseña por favor";
            aviso = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
            aviso.show();
        }else if(txtContraseña.getText().toString().length()<6) {
            mensaje = "La contraseña debe tener al menos 6 caracteres";
            aviso = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
            aviso.show();
        }else if (!cb.isChecked()) {
            mensaje = "Olvida los términos y condiciones";
            aviso = Toast.makeText(getApplicationContext(), mensaje,Toast.LENGTH_SHORT);
            aviso.show();
        }else{
            auth.createUserWithEmailAndPassword(txtEmail.getText().toString(),txtContraseña.getText().toString()).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user=auth.getCurrentUser();
                        String UID=user.getUid();
                        HashMap<String,Object> userData=new HashMap<>();
                        userData.put("UserName",txtUsuario.getText().toString());
                        userData.put("Email", txtEmail.getText().toString());
                        userData.put("Telefono",txtTelefono.getText().toString());
                        reference.child("Usuarios/"+UID).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Registro.this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                                    Intent i_IniciarSesion = new Intent(Registro.this,BurgerNav.class);
                                    i_IniciarSesion.putExtra("USERID",UID);
                                    startActivity(i_IniciarSesion);
                                    finish();
                                }else{
                                    Toast.makeText(Registro.this, "Error al crear datos en DB: "+task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(Registro.this, "Error al crear usuario"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            
            
        }
    }
}