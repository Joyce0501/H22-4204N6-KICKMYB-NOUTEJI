package com.example.kickmyb;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.databinding.ActivityConsultationBinding;


public class ConsultationActivity extends AppCompatActivity {
    private ActivityConsultationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Consultation");

        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
}
}
