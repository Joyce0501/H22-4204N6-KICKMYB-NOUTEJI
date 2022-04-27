package com.example.kickmyb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kickmyb.databinding.ActivityAccueilBinding;
import com.example.kickmyb.http.RetrofitUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import org.kickmyb.transfer.AddTaskRequest;
import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SigninRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccueilActivity extends AppCompatActivity {
    private ActivityAccueilBinding binding;
    private ActionBarDrawerToggle abToggle;
    AccueilAdapter adapter;
    ProgressDialog progressD;
    ProgressDialog progressDeconnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setTitle("Accueil");

        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.initRecycler();
        this.remplirRecycler();

        editNom();

        binding.swiperefresh.setOnRefreshListener(
                () -> {
                    Toast.makeText(AccueilActivity.this, "REFRESH", Toast.LENGTH_LONG).show();
                    //Part l'animation de loading
                    binding.swiperefresh.setRefreshing(true);
                    remplirRecycler();
                }
        );



        binding.buttonCreation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent retour = new Intent(AccueilActivity.this,CreationActivity.class);
                startActivity(retour);

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

                    Intent retour = new Intent(AccueilActivity.this,AccueilActivity.class);
                    startActivity(retour);

                }
                else if(R.id.nav_item_two == item.getItemId())
                {
                    Intent retour = new Intent(AccueilActivity.this,CreationActivity.class);
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
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(abToggle.onOptionsItemSelected(item)){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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

    public void initRecycler()
    {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //specify an adapter
        adapter = new AccueilAdapter();
        recyclerView.setAdapter(adapter);
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL));
    }
    private void remplirRecycler(){
        progressD = ProgressDialog.show(AccueilActivity.this, getString(R.string.connexion_progress_introduction ),
                getString(R.string.an_update), true);
        RetrofitUtil.get().listeTache().enqueue(new Callback<List<HomeItemResponse>>() {
            @Override
            public void onResponse(Call<List<HomeItemResponse>> call, Response<List<HomeItemResponse>> response) {
                if(response.isSuccessful()){
                    progressD.dismiss();
                    adapter.list.clear();
                    Toast.makeText(AccueilActivity.this, "Serveur recu", Toast.LENGTH_SHORT).show();

                  //  adapter.list.clear();
                    for(int i = 0; i < response.body().size(); i++){
                        Taches t = new Taches();
                        t.nom = response.body().get(i).name;
                        t.deadline = response.body().get(i).deadline;
                        t.percentageDone = response.body().get(i).percentageDone;
                        t.percentageSpent = response.body().get(i).percentageTimeSpent;
                        t.id = response.body().get(i).id;
                        adapter.list.add(t);
                    }

                    adapter.notifyDataSetChanged();
                    //Arrête l'animation de loading
                    binding.swiperefresh.setRefreshing(false);
                    binding.recyclerview.smoothScrollToPosition(0);        // nécessaire si pas déjà en haut de la liste
                }
                else{
                    Toast.makeText(AccueilActivity.this, "Ouch", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<HomeItemResponse>> call, Throwable t) {
                progressD.dismiss();
                showADialog();
                Toast.makeText(AccueilActivity.this, "Ouch Serveur", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void editNom(){
        View headerView = binding.navView.getHeaderView(0);
        TextView text = (TextView)headerView.findViewById(R.id.username);
        text.setText(SingletonNom.leNom);
    }

    public void deconnexion() {
        progressDeconnexion = ProgressDialog.show(AccueilActivity.this, getString(R.string.connexion_progress_introduction ),
                getString(R.string.a_deconnexion), true);
        RetrofitUtil.get().deconnexion().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    progressDeconnexion.dismiss();
                    Toast.makeText(AccueilActivity.this, "Serveur recu", Toast.LENGTH_SHORT).show();
                     Intent retour = new Intent(AccueilActivity.this,MainActivity.class);
                    startActivity(retour);
                }
                else{
                    Toast.makeText(AccueilActivity.this, "Ouch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDeconnexion.dismiss();
                showADialog();
                Toast.makeText(AccueilActivity.this, "Ouch Serveur", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(abToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menu_refresh:
                Toast.makeText(AccueilActivity.this, "REFRESH EN COURS", Toast.LENGTH_LONG).show();

                //Part l'animation de loading
                binding.swiperefresh.setRefreshing(true);

                remplirRecycler();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if(abToggle.onOptionsItemSelected(item)){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return true;
    }

}
