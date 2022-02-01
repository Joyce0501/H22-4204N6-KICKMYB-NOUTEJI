package com.example.kickmyb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kickmyb.databinding.ActivityAccueilBinding;

public class AccueilActivity extends AppCompatActivity {
    private ActivityAccueilBinding binding;
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
            t.taches = "Tache " + i;
            adapter.list.add(t);
        }
        adapter.notifyDataSetChanged();
    }
}
