package com.example.helpme_app_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.ActualizarEstadoRequest;
import com.example.helpme_app_v1.Model.Asesorias.ResponseAsesoriaFiltre;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EstaAsesoriaFragment extends Fragment {

    private RecyclerView recyclerView;
    private EstaAsesoriaAdapter adapter;
    private List<Item> itemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_esta_asesoria, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewEsta);

        // Configuración del RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EstaAsesoriaAdapter(itemList, this );
        recyclerView.setAdapter(adapter);

        fetchData();
        return view;
    }

    private void fetchData() {
        Retrofit retrofit = getRetrofit();
        MyApi service = retrofit.create(MyApi.class);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int user = sharedPreferences.getInt("user", 0);

        service.obtenerdataxasesoria(user).enqueue(new Callback<ResponseAsesoriaFiltre>() {
            @Override
            public void onResponse(Call<ResponseAsesoriaFiltre> call, Response<ResponseAsesoriaFiltre> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Acceder al objeto Data de la respuesta
                    ResponseAsesoriaFiltre.Data data = response.body().getData();

                    // Asegúrate de que data no sea nulo y que tenga una lista de asesorías
                    if (data != null && data.getAsesorias() != null) {
                        // Recorrer la lista de asesorías y crear los elementos para el adaptador
                        for (ResponseAsesoriaFiltre.Asesoria asesoria : data.getAsesorias()) {
                            itemList.add(new Item(
                                    asesoria.getUrl(),
                                    "Tipo: " + asesoria.getTipoasesoria(),
                                    "Fecha: " + asesoria.getFecha() +
                                            " | " + asesoria.getHorainicio() +
                                            " - " + asesoria.getHorafinal(),
                                    asesoria.getDuracion() + " Horas - " +
                                            asesoria.getTokens() + " Tokens",
                                    asesoria.getEstado(),
                                    asesoria.getIdAsesoria()
                            ));
                        }

                        // Notificar al adaptador que los datos han cambiado
                        adapter.notifyDataSetChanged();
                    } else {
                        // Manejar caso en el que la lista de asesorías está vacía o nula
                        Log.e("API Response", "La lista de asesorías está vacía o nula");
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAsesoriaFiltre> call, Throwable t) {
                t.printStackTrace();
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
                .baseUrl("https://grupo6tdam2024.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static class Item {
        String imageUrl, title, details, duration, estado;
        int idAsesoria;

        public Item(String imageUrl, String title, String details, String duration, String estado, int idAsesoria) {
            this.imageUrl = imageUrl;
            this.title = title;
            this.details = details;
            this.duration = duration;
            this.estado = estado;
            this.idAsesoria = idAsesoria;
        }
    }

    public static class EstaAsesoriaAdapter extends RecyclerView.Adapter<EstaAsesoriaAdapter.ViewHolder> {
        private List<Item> itemList;
        private EstaAsesoriaFragment fragment;
        public EstaAsesoriaAdapter(List<Item> itemList, EstaAsesoriaFragment fragment) {
            this.itemList = itemList;
            this.fragment = fragment;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_esta_asesoria, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Item item = itemList.get(position);

            // Establecer los datos de la asesoría
            Glide.with(holder.itemView.getContext()).load(item.imageUrl).into(holder.itemImage);
            holder.itemTitle.setText(item.title);
            holder.itemDetails.setText(item.details);
            holder.itemDetails2.setText(item.duration);
            holder.itemStatus.setText(item.estado);
            // Aquí deberíamos verificar el estado de la asesoría
            if (item.estado.equals("Pendiente")) {
                // Mostrar los botones si la asesoría está pendiente
                holder.buttonAccept.setVisibility(View.VISIBLE);
                holder.buttonReject.setVisibility(View.VISIBLE);

                // Acción del botón Aceptar
                holder.buttonAccept.setOnClickListener(v -> {
                    Log.d("Acción", "Aceptado: " + item.title);
                    aceptarAsesoria(holder.itemView.getContext(), item);
                    // Notificamos al adaptador que el ítem en esa posición ha cambiado

                });

                // Acción del botón Rechazar
                holder.buttonReject.setOnClickListener(v -> {
                    Log.d("Acción", "Rechazado: " + item.title);
                    rechazarAsesoria(holder.itemView.getContext(), item);
                });
            } else {
                // Si no está pendiente (aceptado o rechazado), mostrar el estado y ocultar los botones
                holder.buttonAccept.setVisibility(View.GONE);
                holder.buttonReject.setVisibility(View.GONE);
               // Mostrar el estado

                // Mostrar el estado como "Aceptado" o "Rechazado"
                if (item.estado.equals("Aceptado")) {
                    holder.itemStatus.setText("Estado: Aceptado");
                    holder.itemStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.soles_green)); // Puedes usar cualquier color que prefieras
                } else if (item.estado.equals("Rechazado")) {
                    holder.itemStatus.setText("Estado: Rechazado");
                    holder.itemStatus.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.token_red)); // Similar al caso anterior
                }
            }
        }


        @Override
        public int getItemCount() {
            return itemList.size();
        }

        // Método para aceptar la asesoría
        // Método para aceptar la asesoría
        private void aceptarAsesoria(Context context, Item item) {
            // Obtenemos el token y el ID del usuario
            SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("user_token", null);
            int user = sharedPreferences.getInt("user", 0);

            // Creamos el cuerpo de la solicitud con el estado "Aceptada"
            ActualizarEstadoRequest request = new ActualizarEstadoRequest("Aceptada");

            // Creamos el cliente OkHttp con el interceptor para la autorización
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Chain chain) throws IOException {
                            Request newRequest = chain.request().newBuilder()
                                    .addHeader("Authorization", "Bearer " + token)
                                    .build();
                            return chain.proceed(newRequest);
                        }
                    }).build();

            // Construimos Retrofit con el cliente OkHttp (autenticado)
            Retrofit retrofitWithAuth = new Retrofit.Builder()
                    .baseUrl("https://grupo6tdam2024.pythonanywhere.com/")
                    .client(client)  // Usamos el cliente autenticado
                    .addConverterFactory(GsonConverterFactory.create())  // Conversor Gson
                    .build();

            // Creamos el servicio de Retrofit
            MyApi service = retrofitWithAuth.create(MyApi.class);

            // Realizamos la llamada PUT para actualizar el estado de la asesoría
            Call<ResponseAsesoriaFiltre> call = service.actualizarEstadoAsesoria(item.idAsesoria, request);

            // Hacer la solicitud asíncrona
            call.enqueue(new Callback<ResponseAsesoriaFiltre>() {
                @Override
                public void onResponse(Call<ResponseAsesoriaFiltre> call, Response<ResponseAsesoriaFiltre> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseAsesoriaFiltre responseAsesoria = response.body();

                        if (responseAsesoria.getStatus() == 1) { // Asumimos que "status: 1" indica éxito
                            // Procesar la respuesta exitosa
                            Toast.makeText(context, "Asesoría aceptada con éxito.", Toast.LENGTH_SHORT).show();
                            Log.d("API_CALL", "Actualización exitosa: " + responseAsesoria.getMessage());

                            // Opcional: Actualiza la UI, recarga datos o navega a otra pantalla
                            // Ejemplo: Reload la lista actual de asesorías
                            // cargarAsesorias();
                        } else {
                            // Si el servidor responde pero con un error específico
                            Toast.makeText(context, "Error: " + responseAsesoria.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("API_CALL", "Error en el servidor: " + responseAsesoria.getMessage());
                        }
                    } else {
                        // Respuesta fallida aunque llegó desde el servidor
                        Toast.makeText(context, "Error al aceptar asesoría. Código: " + response.code(), Toast.LENGTH_SHORT).show();
                        Log.e("API_CALL", "Respuesta no exitosa. Código: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseAsesoriaFiltre> call, Throwable t) {
                    Toast.makeText(context, "Error al aceptar asesoría. Código: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("API_CALL", "Respuesta no exitosa. Código: " + t.getMessage());
                }

            });
        }

        // Método para rechazar la asesoría
        private void rechazarAsesoria(Context context, Item item) {
            // Realiza la llamada a la API para rechazar la asesoría
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://grupo6tdam2024.pythonanywhere.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MyApi service = retrofit.create(MyApi.class);

            // Obtenemos el token y el ID del usuario
            SharedPreferences sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("user_token", null);
            int user = sharedPreferences.getInt("user", 0);

            // Realizamos la solicitud de rechazo de asesoría
            // Lógica para realizar la solicitud de rechazo a la API
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView itemTitle, itemDetails, itemDetails2, itemStatus; // Añadido itemStatus
            Button buttonAccept, buttonReject;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.itemImage);
                itemTitle = itemView.findViewById(R.id.itemTitle);
                itemDetails = itemView.findViewById(R.id.itemDetails);
                itemDetails2 = itemView.findViewById(R.id.itemDetails2);
                itemStatus = itemView.findViewById(R.id.itemStatus);  // Añadido para el estado
                buttonAccept = itemView.findViewById(R.id.buttonAccept);
                buttonReject = itemView.findViewById(R.id.buttonReject);
            }
        }

    }
}
