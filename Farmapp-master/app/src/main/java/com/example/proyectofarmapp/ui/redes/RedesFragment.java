package com.example.proyectofarmapp.ui.redes;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.proyectofarmapp.R;
import com.example.proyectofarmapp.ui.historial.HistorialViewModel;

import java.net.URI;

public class RedesFragment extends Fragment {


    private RedesViewModel mViewModel;

    public static RedesFragment newInstance() {
        return new RedesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.redes_fragment, container, false);

        /*public void onTwitter (View view){
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("https://"));
            startActivity(i);
        }
        public View onFacebook (View view){
            Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("https://"));
            startActivity(i);
        }*/

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RedesViewModel.class);
        // TODO: Use the ViewModel
    }

}





