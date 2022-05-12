package com.example.kickmyb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.kickmyb.transfer.HomeItemResponse;
import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    AccueilAdapter adapter;
    ProgressDialog progressD;

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

        // On affiche le dialogue avant de lancer la requete
        progressD = ProgressDialog.show(MainActivity.this, getString(R.string.connexion_progress_introduction),
                getString(R.string.connexion_progress_message), true ) ;
        RetrofitUtil.get().connexion(signin).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                Log.i("ALLO","oK");

                progressD.dismiss();
                if(response.isSuccessful()) {
                    // si on recoit une reponse du serveur, premier truc : on ferme le dialogue
                    progressD.dismiss();
                    Intent accueil = new Intent(MainActivity.this,AccueilActivity.class);
                    startActivity(accueil);
                    SingletonNom.leNom = response.body().username;

                }
                else {
                    progressD.dismiss();
                     showADialogAuthentification();
                    try {
                        String corpsErreur = response.errorBody().string();
                        Log.i("RETROFIT", "le code " + response.code());
                        Log.i("RETROFIT", "le message " + response.message());
                        Log.i("RETROFIT", "le corps " + corpsErreur);
                        if (corpsErreur.contains("\"InternalAuthenticationServiceException\"")) {
                            // TODO remplacer par un objet graphique mieux qu'un toast
                   //    Toast.makeText(MainActivity.this, "Ce message est trop court", Toast.LENGTH_SHORT).show();
                           progressD.dismiss();
                            showADialogAuthentification();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {
                // si on recoit une reponse du serveur, premier truc : on ferme le dialogue
                progressD.dismiss();
              //  Toast.makeText(MainActivity.this, "Ouch Serveur", Toast.LENGTH_SHORT).show();
                Log.i("ALLO","non");
                showADialog();
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
    private void showADialogAuthentification() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.no_authentification);
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        builder.show();
    }

}