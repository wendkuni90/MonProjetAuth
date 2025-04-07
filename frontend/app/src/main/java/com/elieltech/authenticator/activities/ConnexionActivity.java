package com.elieltech.authenticator.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.elieltech.authenticator.R;
import com.elieltech.authenticator.api.RetrofitClient;
import com.elieltech.authenticator.api.ApiService;
import com.elieltech.authenticator.models.LoginRequest;
import com.elieltech.authenticator.models.LoginResponse;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnexionActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private MaterialButton btnConnexion;

    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_connexion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.connexion), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        btnConnexion = findViewById(R.id.btn_connexion);

        sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);

        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        findViewById(R.id.tv_inscription).setOnClickListener(v -> {
            Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        ApiService apiService = RetrofitClient.getInstance().getApi();
        Call<LoginResponse> call = apiService.loginUser(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String access = response.body().getAccess();
                    String refresh = response.body().getRefresh();

                    // Stocker les tokens
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("access_token", access);
                    editor.putString("refresh_token", refresh);
                    editor.apply();

                    Toast.makeText(ConnexionActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();

                    // Rediriger vers une autre la page d'accueil
                    Intent intent = new Intent(ConnexionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ConnexionActivity.this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(ConnexionActivity.this, "Erreur de connexion : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginError", t.getMessage());
            }
        });
    }
}
