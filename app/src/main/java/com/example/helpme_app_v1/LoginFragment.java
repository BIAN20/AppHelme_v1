package com.example.helpme_app_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.helpme_app_v1.Interface.Grupo06PyAnyApi;
import com.example.helpme_app_v1.Model.AuthRequest;
import com.example.helpme_app_v1.Model.AuthResponse;
import com.example.helpme_app_v1.databinding.FragmentLoginBinding;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private String token = "";  // Para almacenar el token de forma global
    private String rol = "";
    private int user = 0;
    public LoginFragment() {
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnNewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String NewUser = binding.btnNewLogin.getText().toString();
                LoginFragmentDirections.ActionLoginFragmentToFirstFragment action =
                        LoginFragmentDirections.actionLoginFragmentToFirstFragment(NewUser);
                NavHostFragment.findNavController(LoginFragment.this).navigate(action);

            }
        });


        binding.btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.edtUserName.getText().toString();
                String password = binding.edtPassword.getText().toString();
                accederLogin(username, password);
            }
        });
    }

    private void accederLogin(String p_username, String p_password) {
        // Usar Retrofit para hacer login con la API y obtener el token
        Retrofit retrofit = getRetrofit();
        Grupo06PyAnyApi api = retrofit.create(Grupo06PyAnyApi.class);

        AuthRequest AuthRequest = new AuthRequest();
        AuthRequest.setEmail(p_username);
        AuthRequest.setPassword(p_password);

        Call<AuthResponse> call = api.login(AuthRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        token = authResponse.getToken();  // Guardar el token
                        rol = authResponse.getRol();
                        user = authResponse.getUser();
                        saveTokenlocal(token, rol, user);  // Guardarlo en SharedPreferences

                        if (rol.toString().equals("Asesor")) {
                            Toast.makeText(getContext(), "Bienvenido asesor", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_loginFragment_to_inicioFragmentAsesor);
                        } else {
                            Toast.makeText(getContext(), "Bienvenido estudiante", Toast.LENGTH_SHORT).show();
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_loginFragment_to_inicioFragment);
                        }


                    }
                } else {
                    Toast.makeText(getContext(), "Credenciales Erroneas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Credenciales Erroneas", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveTokenlocal(String token,String rol, int user ) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rol", rol);
        editor.putInt("user", user);
        editor.putString("user_token", token);  // Guardar el token
        Log.d("SharedPreferences", "Token almacenado: " + token);
        Log.d("SharedPreferences", "rol almacenado: " + rol);
        editor.apply();
    }

    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", null);  // Obtén el token guardado

        if (token != null) {
            // Si el token existe, redirige al InicioFragment
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_loginFragment_to_inicioFragment);
        } else {
            // Si no hay token, permanece en LoginFragment
            Toast.makeText(getContext(), "Por favor, inicie sesión", Toast.LENGTH_SHORT).show();
        }
    }


    private Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(request);
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://grupo6tdam2024.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
