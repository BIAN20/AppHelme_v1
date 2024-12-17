package com.example.helpme_app_v1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.Asesores.ResponseAsesor;
import com.example.helpme_app_v1.Model.AsesoriaPrecio;
import com.example.helpme_app_v1.Model.AsesoriasPrecios.ResponseAsesoriaPrecio;
import com.example.helpme_app_v1.Model.AsesoriasPrecios.ResponseAsesoriaPreciov2;
import com.example.helpme_app_v1.databinding.FragmentRAseTokensBinding;

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
 * Use the {@link RAse_TokensFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RAse_TokensFragment extends Fragment {
    private FragmentRAseTokensBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RAse_TokensFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RAse_TokensFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RAse_TokensFragment newInstance(String param1, String param2) {
        RAse_TokensFragment fragment = new RAse_TokensFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRAseTokensBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.edTokens.setOnClickListener(v -> showTokensForSesion() );
        binding.edDurationSesion.setOnClickListener(c -> showDurationForSesion());


        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                int user = sharedPreferences.getInt("user", 0);
                String tipoasesoria = binding.ednameAsesoria.getText().toString();
                String tokens = binding.edTokens.getText().toString();
                tokens = tokens.replace(" tokens", "");
                int tokensconverter = Integer.parseInt(tokens);

                String horas = binding.edDurationSesion.getText().toString();
                horas = horas.replace(" horas", "");
                int horasconverter = Integer.parseInt(horas);

                AsesoriaPrecio asesoriape = new AsesoriaPrecio();
                asesoriape.setAsesor(user);
                asesoriape.setTokens(tokensconverter);
                asesoriape.setTipoasesoria(tipoasesoria);
                asesoriape.setDuracion(horasconverter);
                asesoriape.setUrl(binding.edImageUrl.getText().toString());

                registrarprecio(asesoriape);

            }
        });

    }
    private void registrarprecio( AsesoriaPrecio asesoriape){
        Retrofit retrofit = getRetrofit();
        MyApi api = retrofit.create(MyApi.class);
        Call<ResponseAsesoriaPreciov2> call = api.guardarprecioasesoria(asesoriape);
        call.enqueue(new Callback<ResponseAsesoriaPreciov2>() {
            @Override
            public void onResponse(Call<ResponseAsesoriaPreciov2> call, Response<ResponseAsesoriaPreciov2> response) {
                Log.d("API_CALL", "onResponse: Llamada completada con código: " + response.code());

                if (response.isSuccessful()) {
                    ResponseAsesoriaPreciov2 respuesta = response.body();
                    if (respuesta != null) {
                        Log.d("API_CALL", "Respuesta recibida: " + respuesta.toString());
                        if (respuesta.getStatus() == 1) {
                            Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Log.d("API_CALL", "Registro exitoso: Navegando a siguiente pantalla");
                          NavHostFragment.findNavController(RAse_TokensFragment.this)
                            .navigate(R.id.action_RAse_TokensFragment_to_inicioAsesor);
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
            public void onFailure(Call<ResponseAsesoriaPreciov2> call, Throwable t) {
                Log.e("API_CALL", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Retrofit getRetrofit() {
        // Obtener el token desde SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("user_token", null);  // null es el valor por defecto si no existe el token

        if (token == null) {
            // Manejar el caso donde no se encontró el token (puedes lanzar una excepción o gestionar otro comportamiento)
            throw new IllegalStateException("Token no encontrado");
        }

        // Usar el token recuperado para crear el cliente OkHttp
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


    private void showTokensForSesion() {
        // Opciones para el diálogo
        String[] tokensOptions = {"10 tokens", "20 tokens", "30 tokens",
                "40 tokens", "50 tokens", "60 tokens",
                "70 tokens", "80 tokens", "90 tokens",
                "100 tokens", "150 tokens", "200 tokens"};

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        // Configurar el título y las opciones
        builder.setTitle("Selecciona Tokens")
                .setIcon(R.drawable.money) // Añadir un icono (si tienes uno en tu drawable)
                .setItems(tokensOptions, (dialog, which) -> {
                    // Establecer la opción seleccionada en el EditText
                    binding.edTokens.setText(tokensOptions[which]);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());

        // Crear el diálogo
        AlertDialog dialog = builder.create();

        // Cambiar el fondo del diálogo (después de haberlo creado)
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.white); // Fondo blanco, puedes cambiarlo

        // Mostrar el diálogo
        dialog.show();

        // Personalizar el texto y los botones (puedes cambiar tamaños y colores)
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.secondary)); // Color de texto personalizado
        negativeButton.setTextSize(16); // Tamaño del texto

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.secondary)); // Color de texto personalizado
        positiveButton.setTextSize(16); // Tamaño del texto
    }

    private void showDurationForSesion() {
        // Opciones para el diálogo
        String[] hoursOptions = {"1 horas", "2 horas","3 horas"};

        // Crear y mostrar el diálogo
        new AlertDialog.Builder(requireContext())
                .setTitle("Tokens")
                .setItems(hoursOptions, (dialog, which) -> {
                    // Establecer la opción seleccionada en el EditText
                    binding.edDurationSesion.setText(hoursOptions[which]);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}