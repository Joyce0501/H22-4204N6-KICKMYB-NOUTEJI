package com.example.kickmyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


import com.example.kickmyb.databinding.ActivityCreationBinding;

public class CreationActivity extends AppCompatActivity {
    private ActivityCreationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Creation");

        binding = ActivityCreationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.buttonAccueil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent retour = new Intent(CreationActivity.this,AccueilActivity.class);
                startActivity(retour);
            }
        });
    }
}
