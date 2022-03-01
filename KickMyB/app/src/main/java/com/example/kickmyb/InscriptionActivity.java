package com.example.kickmyb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kickmyb.databinding.ActivityInscriptionBinding;
import com.example.kickmyb.http.RetrofitUtil;

import org.kickmyb.transfer.SigninRequest;
import org.kickmyb.transfer.SigninResponse;
import org.kickmyb.transfer.SignupRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InscriptionActivity extends AppCompatActivity {
    private ActivityInscriptionBinding binding;

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

        RetrofitUtil.get().inscription(signup).enqueue(new Callback<SigninResponse>() {
            @Override
            public void onResponse(Call<SigninResponse> call, Response<SigninResponse> response) {
                if(response.isSuccessful())
                {
                    Intent retour = new Intent(InscriptionActivity.this,MainActivity.class);
                    startActivity(retour);
                }
                else
                {
                        Toast.makeText(InscriptionActivity.this, "Pas possible", Toast.LENGTH_SHORT).show();

                }
                Log.i("ALLO","oK");
            }

            @Override
            public void onFailure(Call<SigninResponse> call, Throwable t) {

                Log.i("ALLO","non");
            }
        });
    }
}
