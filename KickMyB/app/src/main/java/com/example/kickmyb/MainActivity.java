package com.example.kickmyb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kickmyb.databinding.ActivityHeaderBinding;
import com.example.kickmyb.databinding.ActivityMainBinding;
import com.example.kickmyb.http.RetrofitUtil;

import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import java.text.BreakIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActivityHeaderBinding binding2;


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
            }
        });

        // TODO https://stackoverflow.com/questions/35871454/why-findviewbyid-return-null-for-a-view-in-a-drawer-header

        binding.inscription.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent inscription = new Intent(MainActivity.this,InscriptionActivity.class);
                startActivity(inscription);
            }
        });
    }
    // MenuItem lemenu = @layout/activity_header(R.id.username);

    public void useUser(){
        SigninRequest signin = new SigninRequest();
        SingletonNom.leNom = "";
        signin.username = binding.connexionname.getText().toString();
        signin.password = binding.connexionpassword.getText().toString();


        RetrofitUtil.get().connexion(signin).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                Log.i("ALLO","oK");

                if(response.isSuccessful()) {
                    Intent accueil = new Intent(MainActivity.this,AccueilActivity.class);
                    startActivity(accueil);
                    SingletonNom.leNom = response.body().username;
                }
                else {
                    Toast.makeText(MainActivity.this, "Ouch", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Ouch Serveur", Toast.LENGTH_SHORT).show();
                Log.i("ALLO","non");
            }
        });
    }
}