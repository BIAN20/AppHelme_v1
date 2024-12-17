package com.example.helpme_app_v1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.Asesores.AsesorPerfilResponse;
import com.example.helpme_app_v1.Model.AsesoriaPrecio;
import com.example.helpme_app_v1.Model.AsesoriasPrecios.ResponseAsesoriaPrecio;
import com.example.helpme_app_v1.Model.AsesoriasPrecios.ResponseAsesoriaPreciov2;
import com.example.helpme_app_v1.databinding.FragmentHomeAsesorBinding;
import com.example.helpme_app_v1.databinding.FragmentPerfilAsesorBinding;
import com.example.helpme_app_v1.databinding.FragmentRAseTokensBinding;

import java.io.IOException;
import java.util.List;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilAsesorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilAsesorFragment extends Fragment {
    private FragmentPerfilAsesorBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilAsesorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilAsesorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilAsesorFragment newInstance(String param1, String param2) {
        PerfilAsesorFragment fragment = new PerfilAsesorFragment();
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPerfilAsesorBinding.inflate(inflater, container, false);
        LinearLayout itemContainer = binding.horizontalItemContainer;
        fetchData(binding);
        fetchData2(itemContainer);

        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear(); // Borra todos los datos
                editor.apply();

                NavHostFragment.findNavController(PerfilAsesorFragment.this)
                        .navigate(R.id.action_inicioAsesor_to_loginFragment);
            }
        });

        return binding.getRoot();
    }

    private void fetchData2(LinearLayout itemContainer) {
        Retrofit retrofit = getRetrofit();
        MyApi service = retrofit.create(MyApi.class);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int user = sharedPreferences.getInt("user", 0);

        service.listarAsesoriaPrecio(user).enqueue(new Callback<ResponseAsesoriaPrecio>() {
            @Override
            public void onResponse(Call<ResponseAsesoriaPrecio> call, Response<ResponseAsesoriaPrecio> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AsesoriaPrecio> asesoriaPrecios = response.body().getData();
                    for (AsesoriaPrecio asesoria : asesoriaPrecios) {
                        // Crear vista de cada ítem (usando LayoutInflater)
                        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, itemContainer, false);

                        // Configurar contenido de cada ítem
                        ImageView imageView = itemView.findViewById(R.id.itemImage);
                        TextView titleView = itemView.findViewById(R.id.itemTitle);
                        TextView subtitleView = itemView.findViewById(R.id.itemSubtitle);

                        Glide.with(getContext()).load(asesoria.getUrl()).into(imageView);
                        titleView.setText("Título: " + asesoria.getTipoasesoria());
                        subtitleView.setText(asesoria.getTokens() + " Tokens - " + asesoria.getDuracion() + " Horas");

                        // Agregar la vista al contenedor horizontal
                        itemContainer.addView(itemView);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAsesoriaPrecio> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void fetchData(FragmentPerfilAsesorBinding binding) {
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
                            // Aquí asignas el nombre a la vista si todo está bien
                            binding.profileName.setText(userResponse.getData().getNombre()+ " "+ userResponse.getData().getApellidos());
                            binding.profileDescription.setText(userResponse.getData().getPresentacion());
                            binding.tokens.setText(userResponse.getData().getTokens() + " tokens");
                            binding.soles.setText(userResponse.getData().getSaldo() + " soles");
                            binding.experiencia.setText(userResponse.getData().getAniosExperiencia() + " exp");
                            binding.majorButton.setText("Enseñanza Preferida: " + userResponse.getData().getEnsenanzaPreferida());
                            binding.universityButton.setText( "Colegiatura: " + userResponse.getData().getCodigoColegiatura());
                            List<String> especialidades = userResponse.getData().getEspecialidades(); // Suponiendo que especialidades es una lista de cadenas
                            LinearLayout badgesContainer = binding.badgesContainer; // Asegúrate de tener el binding para el contenedor de badges

                            // Limpiar el contenedor de badges en caso de que ya tenga elementos

                            // Limpiar el contenedor de badges en caso de que ya tenga elementos
                            badgesContainer.removeAllViews();

                            // Si no hay especialidades, muestra un mensaje
                            if (especialidades == null || especialidades.isEmpty()) {
                                // Usar getContext() para obtener el contexto de este Fragment
                                TextView noSpecialtiesMessage = new TextView(getContext());
                                noSpecialtiesMessage.setText("No tiene especialidades");
                                noSpecialtiesMessage.setTextColor(Color.GRAY);
                                noSpecialtiesMessage.setPadding(16, 8, 16, 8);
                                noSpecialtiesMessage.setGravity(Gravity.CENTER); // Centrar el texto dentro del TextView

                                // Asegurarse de que el LinearLayout esté configurado para centrar el contenido
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                params.gravity = Gravity.CENTER; // Centrar el TextView dentro del LinearLayout

                                noSpecialtiesMessage.setLayoutParams(params); // Asignar los parámetros al TextView

                                badgesContainer.addView(noSpecialtiesMessage); // Agregar el TextView al LinearLayout

                            } else {
                                // Agregar un TextView por cada especialidad
                                for (String especialidad : especialidades) {
                                    TextView badge = new TextView(getContext()); // Usar getContext() para el contexto
                                    badge.setText(especialidad);
                                    badge.setBackgroundResource(R.color.white); // Asume que tienes un fondo para los badges
                                    badge.setTextColor(Color.GRAY);
                                    badge.setPadding(16, 8, 16, 8);
                                    badge.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                    ));

                                    badgesContainer.addView(badge);
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