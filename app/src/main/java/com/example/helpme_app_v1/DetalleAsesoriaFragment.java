package com.example.helpme_app_v1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.Asesoria;
import com.example.helpme_app_v1.Model.AsesoriaPrecio;
import com.example.helpme_app_v1.Model.Asesorias.ResponseAsesorias;
import com.example.helpme_app_v1.Model.AsesoriasPrecios.ResponseAsesoriaPreciov2;
import com.example.helpme_app_v1.Model.Disponibilidad;
import com.example.helpme_app_v1.Model.Persona;
import com.example.helpme_app_v1.Model.Usuario;
import com.example.helpme_app_v1.databinding.DetalleBusquedaAsesoriaBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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


        AsesoriaPrecio ase = DetalleAsesoriaFragmentArgs.fromBundle(getArguments()).getAsesoriaprecio();
        String imageUrl = DetalleAsesoriaFragmentArgs.fromBundle(getArguments()).getImageUrl();
        String title = DetalleAsesoriaFragmentArgs.fromBundle(getArguments()).getTitle();
        String subtitle = DetalleAsesoriaFragmentArgs.fromBundle(getArguments()).getSubtitle();


        binding.etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });
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

        // Configurar botones de hora
        binding.horaInicioButton.setOnClickListener(v -> showBottomSheetDialogHoraInicio());

        binding.btnContinue.setOnClickListener(v -> {
            // Recoger las horas de inicio
            String horaInicio2 = (binding.horaInicioButton.getText().toString());

            String horaInicio = horaInicio2.replace(" AM", "").replace(" PM", ""); // Eliminar AM/PM

            String horas = binding.edDurationSesion.getText().toString();  // Ejemplo: "2 horas"
            horas = horas.replace(" horas", "");
            int horasASumar = Integer.parseInt(horas);

// Extraer hora y minutos
            String[] partesHora = horaInicio.split(":"); // Divide "2:00" en ["2", "00"]
            int hora = Integer.parseInt(partesHora[0]);
            int minutos = Integer.parseInt(partesHora[1]);

            boolean esAM = horaInicio2.contains("AM") || horaInicio.contains("am"); // Verificar si era AM
            boolean esPM = !esAM;

// Sumar las horas
            hora += horasASumar;

// Ajustar la hora al formato de 12 horas
            if (hora >= 12) {
                if (esAM) {
                    esAM = false; // Cambia a PM
                } else {
                    esAM = true;  // Cambia a AM si cruzas 12 horas
                }
                hora = hora % 12; // Asegurarse de que esté en formato de 12 horas
            }

// Si hora es 0 después del módulo, ajustarla a 12
            if (hora == 0) {
                hora = 12;
            }

// Reconstruir la hora final
            String horaFinal = String.format("%02d:%02d %s", hora, minutos, esAM ? "AM" : "PM");


            Date fecha = obtenerFechaNacimiento(binding.etFechaNacimiento);

            // Validar si la hora de inicio está vacía
            if (horaInicio.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, selecciona la hora de inicio", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            int user = sharedPreferences.getInt("user", 0);
            Asesoria aseso = new Asesoria();
            aseso.setAsesoriaprecio(ase.getIdAsesoriaPrecio());
            aseso.setEstado("Pendiente");
            aseso.setDuracion(horasASumar);
            aseso.setHorafinal(horaFinal);
            aseso.setHorainicio(horaInicio2);
            aseso.setFecha(fecha);
            aseso.setEstudiante(user);
            aseso.setUrl(binding.edImageUrl.getText().toString());
            registrar(aseso);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    private void registrar( Asesoria asesoriape){
        Retrofit retrofit = getRetrofit();
        MyApi api = retrofit.create(MyApi.class);
        Call<ResponseAsesorias> call = api.guardarAsesoria(asesoriape);
        call.enqueue(new Callback<ResponseAsesorias>() {
            @Override
            public void onResponse(Call<ResponseAsesorias> call, Response<ResponseAsesorias> response) {
                Log.d("API_CALL", "onResponse: Llamada completada con código: " + response.code());

                if (response.isSuccessful()) {
                    ResponseAsesorias respuesta = response.body();
                    if (respuesta != null) {
                        Log.d("API_CALL", "Respuesta recibida: " + respuesta.toString());
                        if (respuesta.getStatus() == 1) {
                            Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Log.d("API_CALL", "Registro exitoso: Navegando a siguiente pantalla");
                            NavHostFragment.findNavController(DetalleAsesoriaFragment.this)
                                    .navigate(R.id.action_detalle_busqueda_to_inicioFragment);
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
            public void onFailure(Call<ResponseAsesorias> call, Throwable t) {
                Log.e("API_CALL", "Fallo en la llamada: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void mostrarDatePicker() {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fechaSeleccionada = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
                        binding.etFechaNacimiento.setText(fechaSeleccionada);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }
    public Date obtenerFechaNacimiento(EditText etFechaNacimiento) {
        String fechaStr = etFechaNacimiento.getText().toString();

        if (fechaStr.isEmpty()) {
            Toast.makeText(getContext(), "Por favor ingresa una fecha", Toast.LENGTH_SHORT).show();
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fechaNacimiento = null;

        try {
            fechaNacimiento = sdf.parse(fechaStr);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Fecha inválida, usa el formato correcto (dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        int edad = calendar.get(Calendar.YEAR) - (fechaNacimiento.getYear() + 1900);


        return fechaNacimiento;
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
