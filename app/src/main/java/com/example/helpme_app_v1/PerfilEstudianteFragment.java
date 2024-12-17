package com.example.helpme_app_v1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.Estudiantes.EstudiantePerfilResponse;
import com.example.helpme_app_v1.databinding.FragmentPerfilEstudianteBinding;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PerfilEstudianteFragment extends Fragment {
    private FragmentPerfilEstudianteBinding binding;

    public PerfilEstudianteFragment() {
        // Required empty public constructor
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static PerfilEstudianteFragment newInstance(String param1, String param2) {
        PerfilEstudianteFragment fragment = new PerfilEstudianteFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPerfilEstudianteBinding.inflate(inflater, container, false);

        fetchData2(binding);

        binding.editProfileButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Borra todos los datos
            editor.apply();

            NavHostFragment.findNavController(PerfilEstudianteFragment.this)
                    .navigate(R.id.action_inicioEstudiante_to_loginFragment);
        });

        return binding.getRoot();
    }

    private void fetchData2(FragmentPerfilEstudianteBinding binding) {
        Retrofit retrofit = getRetrofit();
        MyApi service = retrofit.create(MyApi.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int user = sharedPreferences.getInt("user", 0);

        service.obtenerPerfilEstudiante(user).enqueue(new Callback<EstudiantePerfilResponse>() {
            @Override
            public void onResponse(Call<EstudiantePerfilResponse> call, Response<EstudiantePerfilResponse> response) {
                Log.d("API_CALL", "onResponse: Llamada completada con código: " + response.code());

                if (response.isSuccessful()) {
                    EstudiantePerfilResponse studentResponse = response.body();
                    if (studentResponse != null) {
                        if (studentResponse.getStatus() == 1) {
                            // Asignar los datos a los elementos de la vista
                            binding.profileName.setText(studentResponse.getData().getNombre() + " " + studentResponse.getData().getApellidos());
                            binding.carreraButtom.setText(studentResponse.getData().getCarrera());
                            binding.dni.setText("DNI: " + studentResponse.getData().getDni());
                            binding.universityButton.setText(studentResponse.getData().getUniversidad());

                            // Mostrar intereses académicos
                            List<String> intereses = studentResponse.getData().getInteresesAcademicos();
                            LinearLayout interestsContainer = binding.badgesContainerintereses;

                            interestsContainer.removeAllViews(); // Limpiar los intereses previos

                            if (intereses == null || intereses.isEmpty()) {
                                TextView noInterestsMessage = new TextView(getContext());
                                noInterestsMessage.setText("No tiene intereses registrados");
                                noInterestsMessage.setTextColor(Color.GRAY);
                                noInterestsMessage.setPadding(16, 8, 16, 8);
                                noInterestsMessage.setGravity(Gravity.CENTER);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.gravity = Gravity.CENTER;
                                noInterestsMessage.setLayoutParams(params);

                                interestsContainer.addView(noInterestsMessage);
                            } else {
                                for (String interes : intereses) {
                                    TextView badge = new TextView(getContext());
                                    badge.setText(interes);
                                    badge.setBackgroundResource(R.color.white);
                                    badge.setTextColor(Color.BLACK);
                                    badge.setPadding(16, 8, 16, 8);
                                    badge.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    ));

                                    interestsContainer.addView(badge);
                                }
                            }
                        } else {
                            Log.e("API_CALL", "Respuesta nula desde el servidor.");
                            Toast.makeText(getContext(), "Respuesta nula desde el servidor", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API_CALL", "Cuerpo de la respuesta es nulo.");
                        Toast.makeText(getContext(), "Error: Respuesta nula desde el servidor.", Toast.LENGTH_SHORT).show();
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
