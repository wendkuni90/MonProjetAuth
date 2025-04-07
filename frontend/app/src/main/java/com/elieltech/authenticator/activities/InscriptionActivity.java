package com.elieltech.authenticator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.elieltech.authenticator.R;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.elieltech.authenticator.api.ApiService;
import com.elieltech.authenticator.api.RetrofitClient;
import com.elieltech.authenticator.models.RegisterRequest;
import com.elieltech.authenticator.models.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.elieltech.authenticator.R;

public class InscriptionActivity extends AppCompatActivity{

    private EditText username, email, password, confirmPassword;
    private Button btnInscrire;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inscription);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.inscription), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Liaison avec le XML
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        btnInscrire = findViewById(R.id.btn_inscrire);
        tvLogin = findViewById(R.id.tv_login);

        // Action du bouton S'inscrire
        btnInscrire.setOnClickListener(v -> {
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                return;
            }

            String user = username.getText().toString();
            String mail = email.getText().toString().trim();
            String pass = password.getText().toString();

            if (user.isEmpty() || mail.isEmpty() || pass.isEmpty()) {
                Toast.makeText(InscriptionActivity.this, "Tous les champs sont requis", Toast.LENGTH_SHORT).show();
            } else {
                // Ici on va appeler l'API
                Toast.makeText(InscriptionActivity.this, "Inscription en cours...", Toast.LENGTH_SHORT).show();

                RegisterRequest request = new RegisterRequest(
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString()
                );

                ApiService apiService = RetrofitClient.getInstance().getApi();
                Call<RegisterResponse> call = apiService.registerUser(request);
                call.enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                            // Naviguer vers l'écran de connexion
                            startActivity(new Intent(InscriptionActivity.this, ConnexionActivity.class));
                            finish();
                        } else {
                            Toast.makeText(InscriptionActivity.this, "Erreur : " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Toast.makeText(InscriptionActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Redirection vers l'écran de connexion
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(InscriptionActivity.this, ConnexionActivity.class));
            finish();
        });
    }

}
