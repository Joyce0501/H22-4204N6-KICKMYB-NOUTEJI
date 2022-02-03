package com.example.kickmyb;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.kickmyb.databinding.ActivityCreationBinding;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreationActivity extends AppCompatActivity {
    private ActivityCreationBinding binding;
    EditText dp1;
    Calendar calendar;

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

        dp1 = findViewById(R.id.editDate);
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                updateCalendar();

            }

            private void updateCalendar()
            {
                String Format = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.CANADA);

                dp1.setText(sdf.format(calendar.getTime()));
            }
        };
        dp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CreationActivity.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        NavigationView nv = binding.navView;
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (R.id.nav_item_one == item.getItemId())
                {
                   Intent retour = new Intent(CreationActivity.this,AccueilActivity.class);
                   startActivity(retour);

                }
                else if(R.id.nav_item_two == item.getItemId())
                {
                    Intent retour = new Intent(CreationActivity.this,CreationActivity.class);
                    startActivity(retour);
                }
                else if(R.id.nav_item_three==item.getItemId())
                {
                    Intent retour = new Intent(CreationActivity.this,MainActivity.class);
                    startActivity(retour);
                }
                return true;
            }
        });

    }

}
