package com.example.proyectofarmapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvRegistrate;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_ProyectoFarmapp_Login);
        setContentView(R.layout.activity_main);
        tvRegistrate=findViewById(R.id.textRegistro);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> iniciarSesion());
        tvRegistrate.setOnClickListener(v -> irRegistro());
    }

    public void iniciarSesion (){
        EditText txtUsuario = findViewById(R.id.editTextUsuario);
        EditText txtContraseña = findViewById(R.id.editTextContraseña);
        String mensaje ="";
        Toast aviso;

        if((txtUsuario.length()==0) || (txtContraseña.length()==0)) {
            mensaje = "Hay campos vacios";
            aviso = Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT);
            aviso.show();
        }else{
            //FUNCION PARA ENTRAR CON CORREO Y CONTRASEÑA
            auth.signInWithEmailAndPassword(txtUsuario.getText().toString(),txtContraseña.getText().toString()).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user=auth.getCurrentUser();
                        String UID=user.getUid();
                        Intent i=new Intent(MainActivity.this, BurgerNav.class);
                        i.putExtra("USERID",UID);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Error al iniciar sesión: "+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void irRegistro (){
        Intent i_Registro = new Intent(this,Registro.class);
        startActivity(i_Registro);
    }

    //REVISAR SI EXISTE UN TOKEN DE AUTH PREVIO
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= auth.getCurrentUser();
        if(user!=null){
            Intent i=new Intent(MainActivity.this,BurgerNav.class);
            i.putExtra("USERID",user.getUid());
            startActivity(i);
            finish();
        }
    }
}