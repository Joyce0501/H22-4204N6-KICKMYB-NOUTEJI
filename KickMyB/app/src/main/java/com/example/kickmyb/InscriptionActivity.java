package com.example.kickmyb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.databinding.ActivityInscriptionBinding;
import com.example.kickmyb.http.RetrofitUtil;

import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;
    ProgressDialog progressD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Inscription");

        binding = ActivityInscriptionBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.retour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                createUser();
            }

        });

    }


    public void createUser() {
        SignupRequest signup = new SignupRequest();
        signup.username = binding.inscriptionname.getText().toString();
        signup.password = binding.inscriptionpassword.getText().toString();
        SingletonNom.leNom = "";

        // On affiche le dialogue avant de lancer la requete
        progressD = ProgressDialog.show(InscriptionActivity.this, "Please wait",
                "The registration operation is in progress", true);
        RetrofitUtil.get().inscription(signup).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                if(response.isSuccessful())
                {
                    progressD.dismiss();
                    Intent retour = new Intent(InscriptionActivity.this,AccueilActivity.class);
                    startActivity(retour);
                    SingletonNom.leNom = response.body().username;
                }
                else
                {
                    try {
                        String corpsErreur = response.errorBody().string();
                        Log.i("RETROFIT", "le code " + response.code());
                        Log.i("RETROFIT", "le message " + response.message());
                        Log.i("RETROFIT", "le corps " + corpsErreur);
                        if (corpsErreur.equals("\"PasswordTooShort\"")) {
                            // TODO remplacer par un objet graphique mieux qu'un toast
                            //binding.inscriptionpassword.setError(corpsErreur);
                            binding.inscriptionpassword.setError("The password given is too short");
                            binding.inscriptionpassword.requestFocus();
                         // Toast.makeText(InscriptionActivity.this, "The password given is too short", Toast.LENGTH_SHORT).show();
                        }
                        else if(corpsErreur.equals("\"UsernameTooShort\"")){
                            // binding.inscriptionname.setError(corpsErreur);
                            binding.inscriptionname.setError("The username given is too short");
                            binding.inscriptionname.requestFocus();
                        }
                        else if(corpsErreur.equals("\"UsernameAlreadyTaken\"")){
                            // binding.inscriptionname.setError(corpsErreur);
                            binding.inscriptionname.setError("The username given is already taken");
                            binding.inscriptionname.requestFocus();
                        }

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                       // Toast.makeText(InscriptionActivity.this, "Pas possible", Toast.LENGTH_SHORT).show();
                }

                Log.i("ALLO","oK");
            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                progressD.dismiss();
                Log.i("ALLO","non");
            }
        });
    }

}
