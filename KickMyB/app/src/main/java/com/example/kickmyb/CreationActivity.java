package com.example.kickmyb;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import com.example.kickmyb.databinding.ActivityCreationBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.AddTaskRequest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreationActivity extends AppCompatActivity {
    private ActivityCreationBinding binding;
    private ActionBarDrawerToggle abToggle;
    EditText dp1;
    Calendar calendar;
    ProgressDialog progressD;
    ProgressDialog progressDeconnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        setContentView(R.layout.activity_header);
        setTitle("Création");

        binding = ActivityCreationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        editNom();

        binding.buttonAccueil.setOnClickListener(new View.OnClickListener(){
            @Override
           public void onClick(View view) {
                    ajoutTache();

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
        DrawerLayout dl = binding.drawerlayout;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        abToggle = new ActionBarDrawerToggle(this,dl,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        dl.addDrawerListener(abToggle);
        abToggle.syncState();
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
                    deconnexion();
                }
                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(abToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        abToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        abToggle.onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);

    }
    public void deconnexion() {
        progressDeconnexion = ProgressDialog.show(CreationActivity.this, getString(R.string.connexion_progress_introduction),
                getString(R.string.a_deconnexion), true);
        RetrofitUtil.get().deconnexion().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    progressDeconnexion.dismiss();
                    Toast.makeText(CreationActivity.this, "Serveur recu", Toast.LENGTH_SHORT).show();
                    Intent retour = new Intent(CreationActivity.this,MainActivity.class);
                    finishAffinity();
                    startActivity(retour);
                }
                else{
                    Toast.makeText(CreationActivity.this, "Ouch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDeconnexion.dismiss();
                Toast.makeText(CreationActivity.this, "Ouch Serveur", Toast.LENGTH_SHORT).show();
                showADialog();
            }
        });
    }

    public void editNom(){
        View headerView = binding.navView.getHeaderView(0);
        TextView text = (TextView)headerView.findViewById(R.id.username);
        text.setText(SingletonNom.leNom);
    }

    public void ajoutTache(){

        String pattern = "MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date aujourd = new Date();
        try {
            Date date = simpleDateFormat.parse(binding.editDate.getText().toString());
            AddTaskRequest add = new AddTaskRequest();
            add.name = binding.editTache.getText().toString();
            add.deadline = date;

            progressD = ProgressDialog.show(CreationActivity.this, getString(R.string.connexion_progress_introduction ),
                    getString(R.string.an_update), true);
            RetrofitUtil.get().ajoutTache(add).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        progressD.dismiss();
                        Intent retour = new Intent(CreationActivity.this,AccueilActivity.class);
                        startActivity(retour);
                        Toast.makeText(CreationActivity.this, "Serveur recu", Toast.LENGTH_SHORT).show();
                    }
                    else{
                         progressD.dismiss();
                        try {
                            String corpsErreur = response.errorBody().string();
                            Log.i("RETROFIT", "le code " + response.code());
                            Log.i("RETROFIT", "le message " + response.message());
                            Log.i("RETROFIT", "le corps " + corpsErreur);
                            if (corpsErreur.equals("\"Existing\"")) {
                                // TODO remplacer par un objet graphique mieux qu'un toast
                                progressD.dismiss();
                                binding.editTache.setError(getString(R.string.creation_errormessage_nametasktake));
                                binding.editTache.requestFocus();
                             //   Toast.makeText(CreationActivity.this, " Nom de tache deja utilise", Toast.LENGTH_SHORT).show();
                            }
                            else if (corpsErreur.equals("\"TooShort\"")) {
                                // TODO remplacer par un objet graphique mieux qu'un toast
                                progressD.dismiss();
                                binding.editTache.setError(getString(R.string.creation_errormessage_nametaskshort));
                                binding.editTache.requestFocus();
                                //   Toast.makeText(CreationActivity.this, " Nom de tache deja utilise", Toast.LENGTH_SHORT).show();
                            }
                            else if (corpsErreur.equals("\"Empty\"")) {
                                // TODO remplacer par un objet graphique mieux qu'un toast
                                progressD.dismiss();
                                binding.editTache.setError(getString(R.string.creation_errormessage_nametaskempty));
                                binding.editTache.requestFocus();
                                //   Toast.makeText(CreationActivity.this, " Nom de tache deja utilise", Toast.LENGTH_SHORT).show();
                            }
                          /*  else if(binding.editDate.getText().toString().isEmpty())
                            {
                                progressD.dismiss();
                                binding.editDate.setError(getString(R.string.creation_errormessage_datetaskempty));
                                binding.editDate.requestFocus();
                            }*/

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressD.dismiss();
                     Toast.makeText(CreationActivity.this, "Ouch Serveur", Toast.LENGTH_SHORT).show();
                    showADialog();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void showADialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.no_network);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        builder.show();
    }

}
