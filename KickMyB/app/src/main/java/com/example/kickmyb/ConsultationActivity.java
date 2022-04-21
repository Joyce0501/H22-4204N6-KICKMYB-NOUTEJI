package com.example.kickmyb;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kickmyb.databinding.ActivityConsultationBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.TaskDetailResponse;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConsultationActivity extends AppCompatActivity {
    private ActivityConsultationBinding binding;
    private ActionBarDrawerToggle abToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Consultation");



        binding = ActivityConsultationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        editNom();

        detailTache();

        binding.sauvegardeAvancement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Long valeur = Long.parseLong(binding.avancement.getText().toString());
                if(valeur < 0 || valeur > 100)
                {
                    binding.avancement.setError("La valeur inscrite n'est pas bonne ");
                }
                else
                {
                    changerPourcentage();
                }

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

                    Intent retour = new Intent(ConsultationActivity.this,AccueilActivity.class);
                    startActivity(retour);

                }
                else if(R.id.nav_item_two == item.getItemId())
                {
                    Intent retour = new Intent(ConsultationActivity.this,CreationActivity.class);
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

    public void editNom(){
        View headerView = binding.navView.getHeaderView(0);
        TextView text = (TextView)headerView.findViewById(R.id.username);
        text.setText(SingletonNom.leNom);
    }

    public void detailTache(){
        Long idTache = getIntent().getLongExtra("idTache",0);
        String pattern = "EEE , MM/dd/yy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        RetrofitUtil.get().detailTache(idTache).enqueue(new Callback<TaskDetailResponse>() {
            @Override
            public void onResponse(Call<TaskDetailResponse> call, Response<TaskDetailResponse> response) {
                if (response.isSuccessful()) {
                    TaskDetailResponse data = response.body();

                    binding.editTache.setText(data.name);

                  //  binding.infoDate.setText("Date limite :" + data.deadline.toString());
                    binding.infoDate.setText("Date limite : " + simpleDateFormat.format(data.deadline));

                    binding.infoPourcentage.setText("Pourcentage fait : " + data.percentageDone + "%");

                    binding.infoTemps.setText("Temps passé : " + data.percentageTimeSpent + "%");
                }
            }

            @Override
            public void onFailure(Call<TaskDetailResponse> call, Throwable t) {
                Log.i("coucou", "");
            }
        });
    }



    public void changerPourcentage(){
        Long idTache = getIntent().getLongExtra("idTache",0);
        Long valeur = Long.parseLong(binding.avancement.getText().toString());

        RetrofitUtil.get().changerPourcentage(idTache,valeur).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i("coucou", "");
                    Intent retour = new Intent(ConsultationActivity.this,AccueilActivity.class);
                    startActivity(retour);

                }
                else{
                    Log.i("recommences", "");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.i("ouch", "");
                showADialog();
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
        RetrofitUtil.get().deconnexion().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ConsultationActivity.this, "Serveur recu", Toast.LENGTH_SHORT).show();
                    Intent retour = new Intent(ConsultationActivity.this,MainActivity.class);
                    startActivity(retour);
                }
                else{
                    Toast.makeText(ConsultationActivity.this, "Ouch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ConsultationActivity.this, "Ouch Serveur", Toast.LENGTH_SHORT).show();
            }
        });
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
