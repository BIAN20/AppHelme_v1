package com.example.helpme_app_v1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.helpme_app_v1.Model.AsesoriaPrecio;
import com.example.helpme_app_v1.Model.Disponibilidad;
import com.example.helpme_app_v1.Model.Persona;
import com.example.helpme_app_v1.Model.Usuario;
import com.example.helpme_app_v1.databinding.DetalleBusquedaAsesoriaBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetalleAsesoriaFragment extends Fragment {

    private DetalleBusquedaAsesoriaBinding binding;
    private String enseniazaPreferidaSeleccionada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DetalleBusquedaAsesoriaBinding.inflate(inflater, container, false);
        binding.edDurationSesion.setOnClickListener(v -> showDurationForSesion());

        // Recuperar datos del bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String imageUrl = bundle.getString("imageUrl");
            String title = bundle.getString("title");
            String subtitle = bundle.getString("subtitle");

            if (imageUrl != null && title != null && subtitle != null) {
                ImageView imageView = binding.itemImage;
                TextView titleView = binding.itemTitle;
                TextView subtitleView = binding.itemSubtitle;

                Glide.with(requireContext()).load(imageUrl).into(imageView);
                titleView.setText(title);
                subtitleView.setText(subtitle);
            } else {
                Toast.makeText(getContext(), "Datos incompletos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "No se pasaron datos", Toast.LENGTH_SHORT).show();
        }

        // Configurar botones de hora
        binding.horaInicioButton.setOnClickListener(v -> showBottomSheetDialogHoraInicio());

        binding.btnContinue.setOnClickListener(v -> {
            // Recoger las horas de inicio
            String horaInicio = binding.horaInicioButton.getText().toString().replace(" AM", "").replace(" PM", "");

            // Validar si la hora de inicio está vacía
            if (horaInicio.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, selecciona la hora de inicio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Configurar la disponibilidad
            Disponibilidad disponibilidad = new Disponibilidad();
            disponibilidad.setHoraInicio(horaInicio);

            // Aquí iría el código para enviar la disponibilidad a la API o hacer cualquier acción
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.edtipoEnsenianza.setOnClickListener(v -> showBottomSheetDialogTipoEnseñanza());
    }

    private void showBottomSheetDialogHoraInicio() {
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.boottom_shet_layout_ase_hi, null);
        BottomSheetDialog bottomSheetDialogHoraInicio = new BottomSheetDialog(requireContext());
        bottomSheetDialogHoraInicio.setContentView(bottomSheetView);

        NumberPicker numberPickerHora1 = bottomSheetView.findViewById(R.id.numberPickerHoraInicio);
        NumberPicker numberPickerMinuto1 = bottomSheetView.findViewById(R.id.numberPickerMinutoInicio);
        NumberPicker numberPickerAMPM1 = bottomSheetView.findViewById(R.id.numberPickerAMPM);

        numberPickerHora1.setMinValue(1);
        numberPickerHora1.setMaxValue(12);
        numberPickerHora1.setWrapSelectorWheel(true);

        numberPickerMinuto1.setMinValue(0);
        numberPickerMinuto1.setMaxValue(59);
        numberPickerMinuto1.setWrapSelectorWheel(true);

        String[] ampmValues = {"AM", "PM"};
        numberPickerAMPM1.setMinValue(0);
        numberPickerAMPM1.setMaxValue(1);
        numberPickerAMPM1.setDisplayedValues(ampmValues);
        numberPickerAMPM1.setWrapSelectorWheel(true);

        // Listener para actualizar automáticamente la hora seleccionada
        NumberPicker.OnValueChangeListener onValueChangeListener = (picker, oldVal, newVal) -> {
            int horaSeleccionada = numberPickerHora1.getValue();
            int minutoSeleccionado = numberPickerMinuto1.getValue();
            String ampmSeleccionado = ampmValues[numberPickerAMPM1.getValue()];
            @SuppressLint("DefaultLocale") String horaFormateada = String.format("%02d:%02d %s", horaSeleccionada, minutoSeleccionado, ampmSeleccionado);
            binding.horaInicioButton.setText(horaFormateada);
        };

        numberPickerHora1.setOnValueChangedListener(onValueChangeListener);
        numberPickerMinuto1.setOnValueChangedListener(onValueChangeListener);
        numberPickerAMPM1.setOnValueChangedListener(onValueChangeListener);

        bottomSheetDialogHoraInicio.show();
    }

    private void showDurationForSesion() {
        String[] hoursOptions = {"1 horas", "2 horas", "3 horas"};

        new AlertDialog.Builder(requireContext())
                .setTitle("Selecciona duración")
                .setItems(hoursOptions, (dialog, which) -> {
                    binding.edDurationSesion.setText(hoursOptions[which]);

                    String horas = hoursOptions[which].replace(" horas", "");
                    int horasConverter = Integer.parseInt(horas);
                    AsesoriaPrecio asesoriaPrecio = new AsesoriaPrecio();
                    asesoriaPrecio.setDuracion(horasConverter);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void showBottomSheetDialogTipoEnseñanza() {
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout2_ase, null);

        BottomSheetDialog bottomSheetDialogEspecialidad = new BottomSheetDialog(requireContext());
        bottomSheetDialogEspecialidad.setContentView(bottomSheetView);

        String[] tipoEnseñanza  = {"Virtual","Presencial","Híbrida"};

        NumberPicker numberPicker = bottomSheetView.findViewById(R.id.numberPickerEnseñanza);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(tipoEnseñanza.length - 1);
        numberPicker.setDisplayedValues(tipoEnseñanza);
        numberPicker.setWrapSelectorWheel(true);

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            enseniazaPreferidaSeleccionada = tipoEnseñanza[newVal];
            binding.edtipoEnsenianza.setText(enseniazaPreferidaSeleccionada);
        });

        bottomSheetDialogEspecialidad.show();
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

}
