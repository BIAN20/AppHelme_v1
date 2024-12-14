package com.example.helpme_app_v1.Interface.api;

import com.example.helpme_app_v1.Especialidades.ResponseEspecialidades;
import com.example.helpme_app_v1.Model.Estudiantes.EstudianteRequest;
import com.example.helpme_app_v1.Model.Estudiantes.EstudianteResponse;
import com.example.helpme_app_v1.Model.InteresesAcademic.ResponseInteresesAcademic;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MyApi {
        @GET("/interesesacademicos")
        Call<ResponseInteresesAcademic> getIntereses();
        @GET("/especialidades")
        Call<ResponseEspecialidades> getEspecialidades();
        @POST("/verificar_correo")
        Call<ResponseBody> verificarcorreo(@Body Object datos);
        @POST("/verificar_dni")
        Call<ResponseBody> verificardni(@Body Object datos);
        @POST("/crearestudiante")
        Call<EstudianteResponse> guardarEstudiante(@Body EstudianteRequest estudianteRequest);
}
