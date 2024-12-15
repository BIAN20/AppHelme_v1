package com.example.helpme_app_v1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.helpme_app_v1.Especialidades.ResponseEspecialidades;
import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.Asesor;
import com.example.helpme_app_v1.Model.Disponibilidad;
import com.example.helpme_app_v1.Model.Estudiante;
import com.example.helpme_app_v1.Model.InteresesAcademic.ResponseInteresesAcademic;
import com.example.helpme_app_v1.Model.Persona;
import com.example.helpme_app_v1.Model.Usuario;
import com.example.helpme_app_v1.databinding.FragmentAcademicInterestsBinding;
import com.example.helpme_app_v1.databinding.FragmentEspecialidadesBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EspecialidadesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EspecialidadesFragment extends Fragment {
    private FragmentEspecialidadesBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EspecialidadesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EspecialidadesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EspecialidadesFragment newInstance(String param1, String param2) {
        EspecialidadesFragment fragment = new EspecialidadesFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentEspecialidadesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtener argumentos (usuario, persona, y especialidades seleccionadas)
        Usuario usuario = EspecialidadesFragmentArgs.fromBundle(getArguments()).getArgUsuario();
        Persona persona = EspecialidadesFragmentArgs.fromBundle(getArguments()).getArgPersona();
        Disponibilidad disponibilidad = EspecialidadesFragmentArgs.fromBundle(getArguments()).getArgDisponibilidad();
        String[] especialidadesSeleccionadas = EspecialidadesFragmentArgs.fromBundle(getArguments()).getSelectedespecialides();
        Asesor asesor = EspecialidadesFragmentArgs.fromBundle(getArguments()).getArgAsesor();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupo6tdam2024.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApi apiService = retrofit.create(MyApi.class);

        // Llamada a la API
        apiService.getEspecialidades().enqueue(new Callback<ResponseEspecialidades>() {
            @Override
            public void onResponse(Call<ResponseEspecialidades> call, Response<ResponseEspecialidades> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResponseEspecialidades.Especialidades> especialidades = response.body().getData();
                    populateEspecialidades(especialidades, especialidadesSeleccionadas);
                } else {
                    Toast.makeText(getContext(), "Error al cargar especialidades. Intente nuevamente.", Toast.LENGTH_LONG).show();
                    Log.e("API Error", "Error en la respuesta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseEspecialidades> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API Failure", "Error de conexión: ", t);
            }
        });

        // Configurar el botón siguiente
        binding.btnSiguiente.setOnClickListener(v -> {
            List<String> selectedInterests = getSelectedInterests();
            String[] interestsArray = selectedInterests.toArray(new String[0]);

            EspecialidadesFragmentDirections.ActionEspecialidadesFragmentToRAseEducationFragment action =
                    EspecialidadesFragmentDirections.actionEspecialidadesFragmentToRAseEducationFragment(asesor, disponibilidad, interestsArray, usuario, persona);
            NavHostFragment.findNavController(EspecialidadesFragment.this).navigate(action);
        });
    }

    private List<String> getSelectedInterests() {
        LinearLayout container = binding.especialidadesContainer;
        List<String> selectedInterests = new ArrayList<>();

        for (int i = 0; i < container.getChildCount(); i++) {
            TextView textView = (TextView) container.getChildAt(i);
            if (textView.getTag() != null && (boolean) textView.getTag()) {
                selectedInterests.add(textView.getText().toString());
            }
        }

        return selectedInterests;
    }

    private void populateEspecialidades(List<ResponseEspecialidades.Especialidades> especialidades, String[] especialidadesSeleccionadas) {
        LinearLayout container = binding.especialidadesContainer;

        // Convertir especialidades seleccionadas a una lista para fácil comparación
        List<String> selectedEspecialidadesList = especialidadesSeleccionadas != null
                ? Arrays.asList(especialidadesSeleccionadas)
                : new ArrayList<>();

        for (ResponseEspecialidades.Especialidades especialidad : especialidades) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(especialidad.getNombreEspecialidad());
            textView.setPadding(16, 16, 16, 16);
            textView.setTextSize(16);
            textView.setBackgroundResource(android.R.drawable.list_selector_background);

            // Verificar si la especialidad ya está seleccionada
            if (selectedEspecialidadesList.contains(especialidad.getNombreEspecialidad())) {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_box_24, 0);
                textView.setTag(true); // Marcar como seleccionada
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_box_outline_blank_24, 0);
                textView.setTag(false); // No seleccionada
            }

            textView.setOnClickListener(v -> toggleSelection(textView, especialidad));
            container.addView(textView);
        }
    }

    private void populateEspecialidades(List<ResponseEspecialidades.Especialidades> Especialidades) {
        LinearLayout container = binding.especialidadesContainer;
        for (ResponseEspecialidades.Especialidades Especialidad : Especialidades) {
            TextView textView = new TextView(getContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(Especialidad.getNombreEspecialidad());
            textView.setPadding(16, 16, 16, 16);
            textView.setTextSize(16);
            textView.setBackgroundResource(android.R.drawable.list_selector_background);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_box_outline_blank_24, 0);
            textView.setOnClickListener(v -> toggleSelection(textView, Especialidad));
            container.addView(textView);
        }
    }

    private void toggleSelection(TextView textView, ResponseEspecialidades.Especialidades Especialidad) {
        boolean isSelected = textView.getTag() != null && (boolean) textView.getTag();
        if (isSelected) {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_box_outline_blank_24, 0);
            textView.setTag(false);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_box_24, 0);
            textView.setTag(true);
        }
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Limpiamos el binding para evitar fugas de memoria
        binding = null;
    }

}