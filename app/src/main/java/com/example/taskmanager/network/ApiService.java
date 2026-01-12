package com.example.taskmanager.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import com.example.taskmanager.model.LoginRequest;
import com.example.taskmanager.model.LoginResponse;
import com.example.taskmanager.model.Task;

public interface ApiService {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @GET("tasks")
    Call<List<Task>> getTasks(
            @Header("Authorization") String token
    );
}
