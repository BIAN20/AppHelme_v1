package com.example.helpme_app_v1.Interface;

import com.example.helpme_app_v1.Model.Asesores.AsesorRequest;
import com.example.helpme_app_v1.Model.Asesores.ResponseAsesor;
import com.example.helpme_app_v1.Model.Asesoria;
import com.example.helpme_app_v1.Model.AuthRequest;
import com.example.helpme_app_v1.Model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Grupo06PyAnyApi {
    @POST("/login")
    Call<AuthResponse> login(@Body AuthRequest authRequest);

    @POST("/crearasesor")
    Call<ResponseAsesor> nuevoAsesor(@Body AsesorRequest asesorRequest);

    @POST("/guardar_asesoria")
    Call<Void> guardarAsesoria(@Body Asesoria asesoria);
}

