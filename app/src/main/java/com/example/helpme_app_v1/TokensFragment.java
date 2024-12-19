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
import com.example.helpme_app_v1.Model.perfil.AsesorPerfilResponse;
import com.example.helpme_app_v1.Model.tokens.ResponseToken;
import com.example.helpme_app_v1.Model.tokens.TokensRequest;
import com.example.helpme_app_v1.databinding.FragmentTokensBinding;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TokensFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TokensFragment extends Fragment {
    private FragmentTokensBinding binding;
    private int tokensglobal;
    private double saldoglobal;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TokensFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TokensFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TokensFragment newInstance(String param1, String param2) {
        TokensFragment fragment = new TokensFragment();
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
        // Inicializa el View Binding
        binding = FragmentTokensBinding.inflate(inflater, container, false);
        fetchData(binding);
        binding.convertButton.setOnClickListener(v -> {
            String tokensStr = binding.tokenInput.getText().toString();
            if (!tokensStr.isEmpty()) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                int user = sharedPreferences.getInt("user", 0);
                int tokens = Integer.parseInt(tokensStr);

                // Validar que la cantidad de tokens a actualizar no sea mayor que los tokens disponibles
                if (tokens > tokensglobal) {
                    binding.resultText.setText("Cantidad de tokens insuficientes.");
                    Toast.makeText(getContext(), "No tienes suficientes tokens.", Toast.LENGTH_SHORT).show();
                } else {
                    int reales = tokensglobal - tokens;  // Tokens restantes después de la conversión
                    double soles = tokens * 0.5;  // Conversión de tokens a soles
                    binding.resultText.setText("Equivale a: " + soles + " Soles");

                    TokensRequest tokencito = new TokensRequest();
                    tokencito.setTokens(reales);
                    tokencito.setIdUsuario(user);
                    tokencito.setSaldo(saldoglobal + soles);  // Actualiza el saldo con los soles obtenidos

                    // Llamada a la API para actualizar los tokens y saldo
                    update(tokencito, binding);
                }
            } else {
                binding.resultText.setText("Ingresa una cantidad válida.");
            }
        });

        // Configura la lógica para el botón de información
        binding.infoButton.setOnClickListener(v -> showConversionInfo());

        return binding.getRoot();
    }
    private void update(TokensRequest tokencito, FragmentTokensBinding binding){
        Retrofit retrofit = getRetrofit();
        MyApi api = retrofit.create(MyApi.class);

        // Llamada a la API para actualizar el precio (suponiendo que el método sea 'actualizarPrecioAsesoria')
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

                            // Navegar a la pantalla de inicio o donde corresponda después de la actualización
                            fetchData(binding);
                        } else {
                            Log.e("API_CALL", "Error del servidor: " + respuesta.getMessage());
                            Toast.makeText(getContext(), "Error del servidor: " + respuesta.getMessage(), Toast.LENGTH_SHORT).show();
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
    private void fetchData(FragmentTokensBinding binding) {
        Retrofit retrofit = getRetrofit();
        MyApi service = retrofit.create(MyApi.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int user = sharedPreferences.getInt("user", 0);

        service.obtenerdata(user).enqueue(new Callback<AsesorPerfilResponse>() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onResponse(Call<AsesorPerfilResponse> call, Response<AsesorPerfilResponse> response) {
                Log.d("API_CALL", "onResponse: Llamada completada con código: " + response.code());

                if (response.isSuccessful()) {
                    // Verifica si el cuerpo de la respuesta es nulo
                    AsesorPerfilResponse userResponse = response.body();
                    if (userResponse != null) {
                        // Verifica el estado de la respuesta para procesar la información
                        if (userResponse.getStatus() == 1) {
                            tokensglobal = userResponse.getData().getTokens();
                            binding.tokenText.setText("Tokens: " + userResponse.getData().getTokens());
                            // Aquí asignas el nombre a la vista si todo está bien
                            binding.saldoText.setText("Saldo: s./" + userResponse.getData().getSaldo());
                            String saldoString = userResponse.getData().getSaldo(); // Asumiendo que getSaldo() devuelve un String
                            saldoglobal = Double.parseDouble(saldoString);

                        } else {
                            Log.e("API_CALL", "Respuesta nula desde el servidor.");
                            Toast.makeText(getContext(), "Respuesta nula desde el servidor", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("API_CALL", "Cuerpo de la respuesta es nulo.");
                        Toast.makeText(getContext(), "Error: Respuesta nula desde el servidor.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si la respuesta no fue exitosa, capturamos el error
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
            public void onFailure(Call<AsesorPerfilResponse> call, Throwable t) {
                // Este bloque captura errores de la red o de la ejecución de la llamada
                Log.e("API_CALL", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}