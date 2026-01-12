package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.model.LoginRequest;
import com.example.taskmanager.model.LoginResponse;
import com.example.taskmanager.network.ApiClient;
import com.example.taskmanager.network.ApiService;
import com.example.taskmanager.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        apiService = ApiClient.getClient().create(ApiService.class);

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        LoginRequest request = new LoginRequest(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        );

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    SharedPrefManager.saveToken(LoginActivity.this, token);

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Login failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOGIN_ERROR", t.getMessage());
            }
        });
    }
}
