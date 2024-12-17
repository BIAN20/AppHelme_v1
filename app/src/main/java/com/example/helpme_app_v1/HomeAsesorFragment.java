package com.example.helpme_app_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.AsesoriaPrecio;
import com.example.helpme_app_v1.Model.AsesoriasPrecios.ResponseAsesoriaPrecio;
import com.example.helpme_app_v1.databinding.FragmentHomeAsesorBinding;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeAsesorFragment extends Fragment {

    private FragmentHomeAsesorBinding binding;
    private RecyclerView recyclerView;
    private GridAdapter adapter;
    private List<Item> itemList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeAsesorBinding.inflate(inflater, container, false);
        recyclerView = binding.recyclerView; // Aquí obtenemos el RecyclerView desde el binding

        // Configura el GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 columnas
        recyclerView.setLayoutManager(gridLayoutManager);

        // Carga los datos dinámicamente desde la API
        fetchData();

        // Configura el adaptador
        adapter = new GridAdapter(itemList);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void fetchData() {
        Retrofit retrofit = getRetrofit();
        MyApi service = retrofit.create(MyApi.class);

        int userId = 35; // Reemplázalo por el ID dinámico
        service.listarAsesoriaPrecio(userId).enqueue(new Callback<ResponseAsesoriaPrecio>() {
            @Override
            public void onResponse(Call<ResponseAsesoriaPrecio> call, Response<ResponseAsesoriaPrecio> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AsesoriaPrecio> asesoriaPrecios = response.body().getData();
                    for (AsesoriaPrecio asesoria : asesoriaPrecios) {
                        itemList.add(new Item(
                                asesoria.getUrl(),
                                "Duración: " + asesoria.getDuracion() + " Horas",
                                asesoria.getTokens() + " Tokens"
                        ));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseAsesoriaPrecio> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeAsesorFragment.this)
                        .navigate(R.id.action_inicioAsesor_to_RAse_TokensFragment);
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

    // Clase interna para el modelo de datos
    public static class Item {
        private String imageUrl;
        private String title;
        private String subtitle;

        public Item(String imageUrl, String title, String subtitle) {
            this.imageUrl = imageUrl;
            this.title = title;
            this.subtitle = subtitle;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }
    }

    // Clase interna para el adaptador
    public static class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

        private List<Item> itemList;

        public GridAdapter(List<Item> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Item item = itemList.get(position);
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .into(holder.itemImage);
            holder.itemTitle.setText(item.getTitle());
            holder.itemSubtitle.setText(item.getSubtitle());
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView itemTitle, itemSubtitle;

            public ViewHolder(View itemView) {
                super(itemView);
                itemImage = itemView.findViewById(R.id.itemImage);
                itemTitle = itemView.findViewById(R.id.itemTitle);
                itemSubtitle = itemView.findViewById(R.id.itemSubtitle);
            }
        }
    }
}
