package com.example.kickmyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kickmyb.databinding.ActivityMainBinding;
import com.example.kickmyb.http.RetrofitUtil;

import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Connexion");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.accueil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                useUser();
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

    public void useUser(){
        SigninRequest signin = new SigninRequest();
        signin.username = binding.connexionname.getText().toString();
        signin.password = binding.connexionpassword.getText().toString();

        RetrofitUtil.get().connexion(signin).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                Log.i("ALLO","oK");
            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                Log.i("ALLO","non");
            }
        });
    }
}