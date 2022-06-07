package com.example.proyectofarmapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectofarmapp.databinding.CarritoFragmentBinding;
import com.example.proyectofarmapp.ui.carrito.CarritoFragment;
import com.example.proyectofarmapp.ui.carrito.CarritoViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofarmapp.databinding.ActivityBurgerNavBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class BurgerNav extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityBurgerNavBinding binding;
    //DATOS PARA EL NAV HEADER
    TextView userName,userMail,userTel;
    CircleImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityBurgerNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarBurgerNav.toolbar);
        /*binding.appBarBurgerNav.iconoCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Carrito de compra", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });*/

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_editar, R.id.nav_carrito,R.id.nav_historial,R.id.nav_codigos,R.id.nav_redes)
                .setOpenableLayout(drawer)
                .build();
        View headerView = navigationView.getHeaderView(0);
        userName=headerView.findViewById(R.id.txtUserName);
        userMail=headerView.findViewById(R.id.txtUserMail);
        userTel=headerView.findViewById(R.id.txtUserTel);
        profileImage=headerView.findViewById(R.id.profileImageView);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_burger_nav);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        setNavHeaderData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.burger_nav, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_burger_nav);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void setNavHeaderData(){
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference();
        String imageLink="https://firebasestorage.googleapis.com/v0/b/farmapp-bdcd2.appspot.com/o/userProfilePictures%2F"+user.getUid()+".jpg?alt=media";
        Glide.with(BurgerNav.this).load(imageLink).error(R.drawable.account_circle).diskCacheStrategy(DiskCacheStrategy.NONE).into(profileImage);
        userRef.child("Usuarios/"+user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName.setText(snapshot.child("UserName").getValue(String.class));
                userMail.setText(snapshot.child("Email").getValue(String.class));
                userTel.setText(snapshot.child("Telefono").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}