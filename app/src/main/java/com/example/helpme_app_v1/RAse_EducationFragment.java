package com.example.helpme_app_v1;

import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpme_app_v1.Model.Asesor;
import com.example.helpme_app_v1.Model.Disponibilidad;
import com.example.helpme_app_v1.Model.Persona;
import com.example.helpme_app_v1.Model.Tokens;
import com.example.helpme_app_v1.Model.Usuario;
import com.example.helpme_app_v1.databinding.FragmentRAseEducationBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Arrays;


public class RAse_EducationFragment extends Fragment {
    private FragmentRAseEducationBinding binding;

    private String aniosExperienciaSeleccionada;
    private String especialidadSeleccionada;
    private String enseniazaPreferidaSeleccionada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRAseEducationBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Usuario usuario = RAse_EducationFragmentArgs.fromBundle(getArguments()).getArgUsuario();
        Persona persona = RAse_EducationFragmentArgs.fromBundle(getArguments()).getArgPersona();
        Disponibilidad disponibilidad = RAse_EducationFragmentArgs.fromBundle(getArguments()).getArgDisponibilidad();
        String[] selectespecialidades = RAse_EducationFragmentArgs.fromBundle(getArguments()).getSelectedespecialides();
        Asesor asesor = new Asesor();
        Tokens tokens = new Tokens();

        //verificar que seleccionamos
        Toast.makeText(requireContext(), Arrays.toString(selectespecialidades), Toast.LENGTH_SHORT).show();
        binding.edEspecialidad.setOnClickListener(v -> showBottomSheetDialogEspecialidad());
        binding.edAniosEx.setOnClickListener(v -> showBottomSheetDialogAniosExperiencia());
        binding.edtipoEnsenianza.setOnClickListener(v -> showBottomSheetDialogTipoEnseñanza());
        //  binding.edDisponibilidad.setOnClickListener(v -> showBottomSheetDialogDisponibilidad());
        // binding.edTokens.setOnClickListener(v -> showBottomSheetDialogToken());
        if (selectespecialidades != null && selectespecialidades.length > 0) {

            binding.edEspecialidad.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.soles_green))
            );
            binding.edEspecialidad.setTextColor(ContextCompat.getColor(requireContext(), R.color.black)); // Cambia el texto a blanco
        }
        binding.edEspecialidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RAse_EducationFragmentDirections.ActionRAseEducationFragmentToEspecialidadesFragment action =
                            RAse_EducationFragmentDirections.actionRAseEducationFragmentToEspecialidadesFragment(disponibilidad, selectespecialidades, usuario, persona);
                    NavHostFragment.findNavController(RAse_EducationFragment.this).navigate(action);
                }
        });

        Toast.makeText(requireContext(), "si pasa ga", Toast.LENGTH_SHORT).show();
        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if ((especialidadSeleccionada != null && enseniazaPreferidaSeleccionada != null && aniosExperienciaSeleccionada != null) || binding.edCertifications.getText().toString().trim().isEmpty() ||  binding.edPresentation.getText().toString().trim().isEmpty()){

                    int anioExpInt = Integer.parseInt(aniosExperienciaSeleccionada.replace("Años", ""));
                    asesor.setPresentacion(binding.edPresentation.getText().toString());
                    asesor.setEnseniazaPreferida(enseniazaPreferidaSeleccionada);
                    asesor.setAniosExperiencia(anioExpInt);
                    asesor.setCodigoColegiatura(binding.edcodColegiatura.getText().toString());
                    asesor.setCertigoogledrive(binding.edCertifications.getText().toString());
                    NavHostFragment.findNavController(RAse_EducationFragment.this)
                            .navigate(R.id.action_RAse_EducationFragment_to_loadingfragment);

                }else {
                    Toast.makeText(requireContext(), "Por favor selecciona todos los campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.edDisponibilidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RAse_EducationFragmentDirections.ActionRAseEducationFragmentToDisponibilidadAsesorFragment action =
                        RAse_EducationFragmentDirections.actionRAseEducationFragmentToDisponibilidadAsesorFragment(usuario, persona, asesor, disponibilidad);
                NavHostFragment.findNavController(RAse_EducationFragment.this).navigate(action);
            }
        });



    }

    private void showBottomSheetDialogEspecialidad() {
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout0_ase, null);

        BottomSheetDialog bottomSheetDialogEspecialidad = new BottomSheetDialog(requireContext());
        bottomSheetDialogEspecialidad.setContentView(bottomSheetView);

        String[] especialidades = {
                "Ingeniería de Sistemas (UNMSM)",
                "Arquitectura (PUCP)",
                "Derecho (USMP)",
                "Medicina (UNSA)",
                "Psicología (URP)",
                "Administración de Empresas (ESAN)",
                "Ingeniería Industrial (USS)",
                "Ciencias Sociales (UL)",
                "Ciencias de la Salud (UPCH)",
                "Ingeniería Civil (UNI)",
                "Otro"

        };
        NumberPicker numberPicker = bottomSheetView.findViewById(R.id.numberPickerEspecialidad);
        numberPicker.setMinValue(0); // Índice mínimo
        numberPicker.setMaxValue(especialidades.length - 1); // Índice máximo
        numberPicker.setDisplayedValues(especialidades); // Muestra las instituciones en el picker
        numberPicker.setWrapSelectorWheel(true); // Hacer cíclico (opcional)

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            especialidadSeleccionada = especialidades[newVal];
            binding.edEspecialidad.setText(especialidadSeleccionada);
            //String institucionSeleccionada = especialidades[newVal];
            //binding.edEspecialidad.setText(institucionSeleccionada);
            //Toast.makeText(requireContext(), "Seleccionaste: " + institucionSeleccionada, Toast.LENGTH_SHORT).show();
        });
        bottomSheetDialogEspecialidad.setOnDismissListener(dialogInterface ->
                Toast.makeText(requireContext(), "Bottom sheet cerrado", Toast.LENGTH_SHORT).show()
        );
        bottomSheetDialogEspecialidad.show();
    }

    private void showBottomSheetDialogAniosExperiencia() {
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout1_ase, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        bottomSheetDialog.setContentView(bottomSheetView);

        NumberPicker numberPicker = bottomSheetView.findViewById(R.id.numberPickerExperiencia);
        numberPicker.setMinValue(1); // Valor mínimo
        numberPicker.setMaxValue(15); // Valor máximo
        numberPicker.setWrapSelectorWheel(true); // Comportamiento cíclico
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            aniosExperienciaSeleccionada = newVal + " años" ;
            binding.edAniosEx.setText(aniosExperienciaSeleccionada);

            //String valorElegido = "Experiencia "+ "mas de " + newVal+ " años";
            //binding.edAniosEx.setText(valorElegido); // Actualizar l botón con el valor
            // Toast.makeText(requireContext(), valorElegido, Toast.LENGTH_SHORT).show();
        });
        bottomSheetDialog.setOnDismissListener(dialogInterface ->
                Toast.makeText(requireContext(), "Bottom sheet cerrado", Toast.LENGTH_SHORT).show()
        );
        bottomSheetDialog.show();
    }

    private void showBottomSheetDialogTipoEnseñanza() {
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_layout2_ase, null);

        BottomSheetDialog bottomSheetDialogEspecialidad = new BottomSheetDialog(requireContext());
        bottomSheetDialogEspecialidad.setContentView(bottomSheetView);

        String[] tipoEnseñanza  = {"Virtual","Presencial","Híbrida" };

        NumberPicker numberPicker = bottomSheetView.findViewById(R.id.numberPickerEnseñanza);
        numberPicker.setMinValue(0); // Índice mínimo
        numberPicker.setMaxValue(tipoEnseñanza.length - 1); // Índice máximo
        numberPicker.setDisplayedValues(tipoEnseñanza); // Muestra las instituciones en el picker
        numberPicker.setWrapSelectorWheel(true); // Hacer cíclico (opcional)

        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            enseniazaPreferidaSeleccionada = tipoEnseñanza[newVal];
            binding.edtipoEnsenianza.setText(enseniazaPreferidaSeleccionada);

            //String ensenianzaSeleccionada = tipoEnseñanza[newVal];
            //binding.edtipoEnsenianza.setText(ensenianzaSeleccionada);
            //Toast.makeText(requireContext(), "Seleccionaste: " + institucionSeleccionada, Toast.LENGTH_SHORT).show();
        });
        bottomSheetDialogEspecialidad.setOnDismissListener(dialogInterface ->
                Toast.makeText(requireContext(), "Bottom sheet cerrado", Toast.LENGTH_SHORT).show()
        );
        bottomSheetDialogEspecialidad.show();
    }

}