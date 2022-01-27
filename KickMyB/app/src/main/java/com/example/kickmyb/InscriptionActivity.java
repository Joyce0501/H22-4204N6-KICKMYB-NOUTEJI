package com.example.kickmyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.databinding.ActivityAccueilBinding;
import com.example.kickmyb.databinding.ActivityInscriptionBinding;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Inscription");

        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.retour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent retour = new Intent(InscriptionActivity.this,MainActivity.class);
                startActivity(retour);
            }
        });
    }
}
