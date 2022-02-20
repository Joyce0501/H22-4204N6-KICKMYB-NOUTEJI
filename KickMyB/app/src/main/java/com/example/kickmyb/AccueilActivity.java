package com.example.kickmyb;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kickmyb.databinding.ActivityAccueilBinding;
import com.google.android.material.navigation.NavigationView;

public class AccueilActivity extends AppCompatActivity {
    private ActivityAccueilBinding binding;
    private ActionBarDrawerToggle abToggle;
    AccueilAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Accueil");

        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.initRecycler();
        this.remplirRecycler();

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
                    Intent retour = new Intent(AccueilActivity.this,MainActivity.class);
                    startActivity(retour);
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
        for(int i = 1; i <= 200; i++)
        {
            Taches t = new Taches();
            t.taches = "Tâche " + i;
            adapter.list.add(t);
        }
        adapter.notifyDataSetChanged();
    }
}
