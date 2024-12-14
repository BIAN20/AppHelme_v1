package com.example.helpme_app_v1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.Persona;
import com.example.helpme_app_v1.Model.Usuario;
import com.example.helpme_app_v1.databinding.FragmentRegistroAsesorBinding;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegistroAsesorFragment extends Fragment {
    private FragmentRegistroAsesorBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistroAsesorFragment() {
    }


    public static RegistroAsesorFragment newInstance(String param1, String param2){
        RegistroAsesorFragment fragment = new RegistroAsesorFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegistroAsesorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Usuario usuario = RegistroAsesorFragmentArgs.fromBundle(getArguments()).getArgUsuario();
        Persona persona = new Persona();
        String[] selectespecialidades = new ArrayList<>().toArray(new String[0]);
        String emailFormat = getString(R.string.welconCode, usuario.getEmail());

        binding.tvSubTitle.setText(emailFormat);

        binding.etFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePicker();
            }
        });

        binding.btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.etNombres.getText().toString().trim().isEmpty() ||
                        binding.etApellidos.getText().toString().trim().isEmpty() ||
                        binding.etDocumento.getText().toString().trim().isEmpty() ||
                        binding.etFechaNacimiento.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                usuario.setRol("Asesor");
                persona.setNombre(binding.etNombres.getText().toString().trim());
                persona.setApellidos(binding.etApellidos.getText().toString().trim());
                persona.setDni(binding.etDocumento.getText().toString().trim());
                Date fecha = obtenerFechaNacimiento(binding.etFechaNacimiento);
                if (fecha == null) return; // Validación de la fecha
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String fechaFormateada = sdf.format(fecha);

// Ahora que tienes la fecha en formato "yyyy-MM-dd", puedes insertarla en la persona
                persona.setFechanacimiento(fechaFormateada);

                if (!binding.cbAceptarTerminos.isChecked()) {
                    Toast.makeText(getContext(), "Debes aceptar los términos", Toast.LENGTH_SHORT).show();
                    return;
                }
                verificardni(persona.getDni(), new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                int status = jsonResponse.getInt("status");

                                if (status == 1) { // El DNI ya está registrado
                                    Toast.makeText(getContext(), "La persona ya está asociada", Toast.LENGTH_SHORT).show();
                                    NavHostFragment.findNavController(RegistroAsesorFragment.this)
                                            .navigate(R.id.action_registroAsesorFragment_to_loginFragment);
                                } else {
                                    // Si el DNI no está registrado, redirigir a personalización académica
                                    navegarAPersonalizacion();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            private void navegarAPersonalizacion() {
                RegistroAsesorFragmentDirections.ActionRegistroAsesorFragmentToRAseEducationFragment action =
                        RegistroAsesorFragmentDirections.actionRegistroAsesorFragmentToRAseEducationFragment(selectespecialidades, usuario, persona);
                NavHostFragment.findNavController(RegistroAsesorFragment.this).navigate(action);
            }
        });

    }

    private void verificardni(String dni, Callback<ResponseBody> callback){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupo6tdam2024.pythonanywhere.com/") // Cambia esto por la URL base de tu servidor
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi api = retrofit.create(MyApi.class);

        // Crear un objeto JSON para enviar el email
        Map<String, String> body = new HashMap<>();
        body.put("dni", dni);

        // Llamar al método POST
        Call<ResponseBody> call = api.verificardni(body);
        call.enqueue(callback);
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

        if (edad < 18) {
            Toast.makeText(getContext(), "Debe tener más de 18 años", Toast.LENGTH_SHORT).show();
            return null;
        }

        return fechaNacimiento;
    }

}
