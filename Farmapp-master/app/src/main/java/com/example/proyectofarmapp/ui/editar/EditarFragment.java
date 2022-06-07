package com.example.proyectofarmapp.ui.editar;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.proyectofarmapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarFragment extends Fragment {

    private EditarViewModel mViewModel;
    CircleImageView profilePicture;
    EditText nombre,email, telefono,calle, numint,numext,colonia,cp;
    Button btnEditar;
    int state=0;
    String userId="";
    public static EditarFragment newInstance() {
        return new EditarFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.editar_fragment,container,false);
        CircleImageView profileImage=view.findViewById(R.id.profilePicture);
        nombre=view.findViewById(R.id.etNombre);
        email=view.findViewById(R.id.etMail);
        telefono=view.findViewById(R.id.etTel);
        btnEditar=view.findViewById(R.id.btnEditar);
        calle=view.findViewById(R.id.etCalle);
        numint=view.findViewById(R.id.etNumInt);
        numext=view.findViewById(R.id.etNumExt);
        colonia=view.findViewById(R.id.etColonia);
        cp=view.findViewById(R.id.etCP);
        userId=getUID();
        String imageLink="https://firebasestorage.googleapis.com/v0/b/farmapp-bdcd2.appspot.com/o/userProfilePictures%2F"+userId+".jpg?alt=media";
        Glide.with(view).load(imageLink).error(R.drawable.account_circle).diskCacheStrategy(DiskCacheStrategy.NONE).into(profileImage);
        loadData();
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==0){
                    nombre.setEnabled(true);
                    email.setEnabled(true);
                    telefono.setEnabled(true);
                    calle.setEnabled(true);
                    colonia.setEnabled(true);
                    numint.setEnabled(true);
                    numext.setEnabled(true);
                    cp.setEnabled(true);
                    btnEditar.setText("Actualizar");
                    state=1;
                }else{
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> userData=new HashMap<>();
                    userData.put("UserName",nombre.getText().toString());
                    userData.put("Email",email.getText().toString());
                    userData.put("Telefono",telefono.getText().toString());
                    userData.put("Calle",calle.getText().toString());
                    userData.put("NumInt",numint.getText().toString());
                    userData.put("NumExt",numext.getText().toString());
                    userData.put("Colonia",colonia.getText().toString());
                    userData.put("CodPostal",cp.getText().toString());
                    ref.child("Usuarios/"+userId).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(view.getContext(),"Datos Actualizados", Toast.LENGTH_SHORT).show();
                                btnEditar.setText("Editar");
                                state=0;
                                nombre.setEnabled(false);
                                email.setEnabled(false);
                                telefono.setEnabled(false);
                                calle.setEnabled(false);
                                colonia.setEnabled(false);
                                numint.setEnabled(false);
                                numext.setEnabled(false);
                                cp.setEnabled(false);
                            }else{
                                Toast.makeText(view.getContext(), "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return view;
    }

    public String getUID(){
        FirebaseAuth auth=FirebaseAuth.getInstance();
        FirebaseUser user=auth.getCurrentUser();
        return user.getUid();
    }
    public void loadData(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Usuarios/"+userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nombre.setText(snapshot.child("UserName").getValue(String.class));
                email.setText(snapshot.child("Email").getValue(String.class));
                telefono.setText(snapshot.child("Telefono").getValue(String.class));
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
}