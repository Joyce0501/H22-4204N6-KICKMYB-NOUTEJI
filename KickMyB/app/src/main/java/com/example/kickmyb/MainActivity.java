package com.example.kickmyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kickmyb.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.accueil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent accueil = new Intent(MainActivity.this,AccueilActivity.class);
                startActivity(accueil);
            }
        });

        binding.inscription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent inscription = new Intent(MainActivity.this,InscriptionActivity.class);
                startActivity(inscription);
            }
        });
    }
}