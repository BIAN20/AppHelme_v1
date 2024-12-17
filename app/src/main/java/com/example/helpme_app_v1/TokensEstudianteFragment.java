package com.example.helpme_app_v1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.Estudiantes.EstudiantePerfilResponse;
import com.example.helpme_app_v1.Model.tokens.ResponseToken;
import com.example.helpme_app_v1.Model.tokens.TokensRequest;
import com.example.helpme_app_v1.databinding.FragmentTokensEstudianteBinding;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TokensEstudianteFragment extends Fragment {
    private FragmentTokensEstudianteBinding binding;
    private int tokensglobal;
    private double saldoglobal;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TokensEstudianteFragment() {
        // Required empty public constructor
    }

    public static TokensEstudianteFragment newInstance(String param1, String param2) {
        TokensEstudianteFragment fragment = new TokensEstudianteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void showConversionInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tabla de Conversión");
        builder.setMessage("1 Token = 0.5 Soles\n2 Tokens = 1 Sol\n10 Tokens = 5 Soles");
        builder.setPositiveButton("Entendido", null);
        builder.create().show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTokensEstudianteBinding.inflate(inflater, container, false);
        fetchData(binding);

        binding.convertButton.setOnClickListener(v -> {
            String tokensStr = binding.tokenInput.getText().toString();
            if (!tokensStr.isEmpty()) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                int user = sharedPreferences.getInt("user", 0);
                int tokens = Integer.parseInt(tokensStr);
                int reales = tokensglobal - tokens;
                double soles = tokens * 0.5;
                binding.resultText.setText("Equivale a: " + soles + " Soles");
                TokensRequest tokencito = new TokensRequest();
                tokencito.setTokens(reales);
                tokencito.setIdUsuario(user);
                tokencito.setSaldo(saldoglobal + soles);

                update(tokencito, binding);
            } else {
                binding.resultText.setText("Ingresa una cantidad válida.");
            }
        });

        binding.infoButton.setOnClickListener(v -> showConversionInfo());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void update(TokensRequest tokencito, FragmentTokensEstudianteBinding binding) {
        Retrofit retrofit = getRetrofit();
        MyApi api = retrofit.create(MyApi.class);

        Call<ResponseToken> call = api.actualizarToken(tokencito);

        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                Log.d("API_CALL", "onResponse: Llamada completada con código: " + response.code());

                if (response.isSuccessful()) {
                    ResponseToken respuesta = response.body();
                    if (respuesta != null) {
                        Log.d("API_CALL", "Respuesta recibida: " + respuesta.toString());
                        if (respuesta.getStatus() == 1) {
                            Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                            Log.d("API_CALL", "Actualización exitosa: Navegando a siguiente pantalla");
                            binding.tokenInput.setText("");

                            fetchData(binding);
                        } else {
                            Log.e("API_CALL", "Error del servidor: " + respuesta.getMenssage());
                            Toast.makeText(getContext(), "Error del servidor: " + respuesta.getMenssage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API_CALL", "Respuesta nula desde el servidor.");
                        Toast.makeText(getContext(), "Respuesta nula desde el servidor", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_CALL", "Error no exitoso con código: " + response.code() + ", cuerpo del error: " + errorBody);
                        Toast.makeText(getContext(), "Error: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("API_CALL", "Error al leer el cuerpo del error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {
                Log.e("API_CALL", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Retrofit getRetrofit() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", null);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://grupo6tdam2024.pythonanywhere.com/") // Asegúrate de que esta URL sea correcta
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    private void fetchData(FragmentTokensEstudianteBinding binding) {
        Retrofit retrofit = getRetrofit();
        MyApi service = retrofit.create(MyApi.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int user = sharedPreferences.getInt("user", 0);

        service.obtenerPerfilEstudiante(user).enqueue(new Callback<EstudiantePerfilResponse>() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<EstudiantePerfilResponse> call, Response<EstudiantePerfilResponse> response) {
                Log.d("API_CALL", "onResponse: Llamada completada con código: " + response.code());

                if (response.isSuccessful()) {
                    EstudiantePerfilResponse userResponse = response.body();
                    assert response.body() != null;
                    EstudiantePerfilResponse.EstudiantePerfilData userRespons2e = response.body().getData();
                    if (userResponse != null) {
                        if (userResponse.getStatus() == 1) {
                            EstudiantePerfilResponse.EstudiantePerfilData data = userResponse.getData();
                            if (data != null) {
                                tokensglobal = data.getTokens();
                                binding.tokenText.setText("Tokens: " + tokensglobal);
                                String saldoString = data.getSaldo();
                                if (saldoString != null && !saldoString.trim().isEmpty()) {
                                    try {
                                        saldoglobal = Double.parseDouble(saldoString);
                                        binding.saldoText.setText("Saldo: s./" + saldoglobal);
                                    } catch (NumberFormatException e) {
                                        Log.e("API_CALL", "Error al convertir saldo a Double: " + e.getMessage());
                                    }
                                }
                            } else {
                                Log.e("API_CALL", "Data es nulo en la respuesta.");
                            }
                        } else {
                            Log.e("API_CALL", "Error en status: " + userResponse.getMessage());
                        }
                    } else {
                        Log.e("API_CALL", "Respuesta del servidor es nula.");
                    }

                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API_CALL", "Error no exitoso con código: " + response.code() + ", cuerpo del error: " + errorBody);
                        Toast.makeText(getContext(), "Error: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e("API_CALL", "Error al leer el cuerpo del error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<EstudiantePerfilResponse> call, Throwable t) {
                Log.e("API_CALL", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}