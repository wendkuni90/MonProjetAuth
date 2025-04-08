package com.elieltech.authenticator.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.elieltech.authenticator.R;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AlertDialog;
import com.elieltech.authenticator.utils.SharedPrefManager;
import com.elieltech.authenticator.api.RetrofitClient;
import com.elieltech.authenticator.api.ApiService;
import com.elieltech.authenticator.models.UserProfileResponse;
import com.elieltech.authenticator.models.LogoutRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView usernameTextView, emailTextView;
    private MaterialButton btnLogout;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        btnLogout = findViewById(R.id.btn_logout);

        SharedPrefManager prefManager = new SharedPrefManager(MainActivity.this);
        String accessToken = prefManager.getAccessToken();

        if (accessToken != null) {
            getUserProfile(accessToken);
        } else {
            Toast.makeText(this, "Token introuvable", Toast.LENGTH_SHORT).show();
        }

        btnLogout.setOnClickListener(view -> showLogoutConfirmation());
    }

    private void getUserProfile(String token) {
        ApiService apiService = RetrofitClient.getInstance().getApi();
        Call<UserProfileResponse> call = apiService.getUserProfile("Bearer " + token);

        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileResponse user = response.body();
                    usernameTextView.setText(user.getUsername());
                    emailTextView.setText(user.getEmail());
                } else {
                    Toast.makeText(MainActivity.this, "Échec de la récupération", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Es-tu sûr de vouloir te déconnecter ?")
                .setPositiveButton("Oui", (dialog, which) -> performLogout())
                .setNegativeButton("Annuler", null)
                .show();
    }

    private void performLogout() {
        SharedPrefManager prefManager = new SharedPrefManager(MainActivity.this);
        String refreshToken = prefManager.getRefreshToken();
        String accessToken = prefManager.getAccessToken();

        if (refreshToken == null || accessToken == null) {
            Toast.makeText(this, "Token manquant", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getInstance().getApi();
        Call<Void> call = apiService.logoutUser("Bearer " + accessToken, new LogoutRequest(refreshToken));

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    prefManager.clearSession();
                    Toast.makeText(MainActivity.this, "Déconnecté avec succès", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ConnexionActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors de la déconnexion", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}