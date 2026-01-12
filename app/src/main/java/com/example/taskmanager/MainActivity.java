package com.example.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager.model.Task;
import com.example.taskmanager.network.ApiClient;
import com.example.taskmanager.network.ApiService;
import com.example.taskmanager.utils.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getClient().create(ApiService.class);

        String token = SharedPrefManager.getToken(this);

        if (token == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        fetchTasks(token);
    }

    private void fetchTasks(String token) {
        apiService.getTasks("Bearer " + token)
                .enqueue(new Callback<List<Task>>() {
                    @Override
                    public void onResponse(Call<List<Task>> call,
                                           Response<List<Task>> response) {

                        if (response.isSuccessful()) {
                            for (Task task : response.body()) {
                                Log.d("TASK", task.getTitle());
                            }
                            Toast.makeText(MainActivity.this,
                                    "Tasks loaded", Toast.LENGTH_SHORT).show();
                        } else {
                            SharedPrefManager.clear(MainActivity.this);
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Task>> call, Throwable t) {
                        Log.e("TASK_ERROR", t.getMessage());
                    }
                });
    }
}
