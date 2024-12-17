package com.example.helpme_app_v1.Interface.api;

import com.example.helpme_app_v1.Especialidades.ResponseEspecialidades;
import com.example.helpme_app_v1.Model.ActualizarEstadoRequest;
import com.example.helpme_app_v1.Model.Asesores.AsesorPerfilResponse;
import com.example.helpme_app_v1.Model.Asesoria;
import com.example.helpme_app_v1.Model.AsesoriaPrecio;
import com.example.helpme_app_v1.Model.Asesorias.ResponseAsesoriaFiltre;
import com.example.helpme_app_v1.Model.Asesorias.ResponseAsesorias;
import com.example.helpme_app_v1.Model.AsesoriasPrecios.ResponseAsesoriaPrecio;
import com.example.helpme_app_v1.Model.AsesoriasPrecios.ResponseAsesoriaPreciov2;
import com.example.helpme_app_v1.Model.Estudiantes.EstudianteRequest;
import com.example.helpme_app_v1.Model.Estudiantes.EstudianteResponse;
import com.example.helpme_app_v1.Model.InteresesAcademic.ResponseInteresesAcademic;

import com.example.helpme_app_v1.Model.Asesores.ResponseAsesor;
import com.example.helpme_app_v1.Model.Asesores.AsesorRequest;
import com.example.helpme_app_v1.Model.tokens.ResponseToken;
import com.example.helpme_app_v1.Model.tokens.TokensRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyApi {
        @GET("/interesesacademicos")
        Call<ResponseInteresesAcademic> getIntereses();
        @GET("/especialidades")
        Call<ResponseEspecialidades> getEspecialidades();
        @POST("/verificar_correo")
        Call<ResponseBody> verificarcorreo(@Body Object datos);
        @POST("/verificar_dni")
        Call<ResponseBody> verificardni(@Body Object datos);
        @GET("listar_asesoria_precio")
        Call<ResponseAsesoriaPrecio> listarAsesoriaPrecio(@Query("usuario") int usuario);
        @GET("listar_asesoria_precio_get")
        Call<ResponseAsesoriaPrecio> listarAsesoriaPreciov2();

        @GET("obtener_datos_asesor")
        Call<AsesorPerfilResponse> obtenerdata(@Query("usuario") int usuario);

        @GET("obtener_asesorias")
        Call<ResponseAsesoriaFiltre> obtenerdataxasesoria(@Query("id_usuario") int usuario);


        @PUT("actualizar_token")
        Call<ResponseToken> actualizarToken(@Body TokensRequest datos);

        @POST("/crearestudiante")
        Call<EstudianteResponse> guardarEstudiante(@Body EstudianteRequest estudianteRequest);
        @POST("/guardar_asesoria_precio")
        Call<ResponseAsesoriaPreciov2> guardarprecioasesoria(@Body AsesoriaPrecio asesoriaprecio);
        @POST("/crearasesorapp")
        Call<ResponseAsesor> guardarasesor(@Body AsesorRequest asesor);

        @PUT("actualizar_estado_asesoria/{id_asesoria}")
        Call<ResponseAsesoriaFiltre> actualizarEstadoAsesoria(
                @Path("id_asesoria") int idAsesoria,
                @Body ActualizarEstadoRequest request
        );

        @POST("/crearasesoria_auth")
        Call<ResponseAsesorias> guardarAsesoria(@Body Asesoria asesoria);
}
