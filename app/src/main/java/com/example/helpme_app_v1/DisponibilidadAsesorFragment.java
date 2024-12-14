package com.example.helpme_app_v1;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.helpme_app_v1.Model.Disponibilidad;
import com.example.helpme_app_v1.databinding.FragmentDisponibilidadAsesorBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
/*
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisponibilidadAsesorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class DisponibilidadAsesorFragment extends Fragment {

    private FragmentDisponibilidadAsesorBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDisponibilidadAsesorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupDaySelectionListeners();

        binding.btnSiguiente.setOnClickListener(v -> {
            // Recoger los días seleccionados
            List<String> selectedDays = getSelectedDays();
            String[] daysArray = selectedDays.toArray(new String[0]);

            // Recoger las horas de inicio y fin
            LocalTime horaInicio = parseHoraToLocalTime(binding.horaInicioButton.getText().toString());
            LocalTime horaFin = parseHoraToLocalTime(binding.horaFinButton.getText().toString());

            // Crear el objeto Disponibilidad
            Disponibilidad disponibilidad = new Disponibilidad();
            disponibilidad.setDisponibilidad(daysArray);
            disponibilidad.setHoraInicio(horaInicio);
            disponibilidad.setHoraFin(horaFin);

            // Aquí puedes pasar el objeto Disponibilidad a otro fragmento o actividad
            Toast.makeText(getContext(), "Días seleccionados: " + disponibilidad, Toast.LENGTH_LONG).show();

            // Ejemplo de cómo pasar el objeto a otro fragmento
            // Bundle bundle = new Bundle();
            // bundle.putSerializable("disponibilidad", disponibilidad);
            // OtroFragment otroFragment = new OtroFragment();
            // otroFragment.setArguments(bundle);
            // getActivity().getSupportFragmentManager().beginTransaction()
            //         .replace(R.id.container, otroFragment)
            //         .commit();
        });

        binding.horaInicioButton.setOnClickListener(v -> showBottomSheetDialogHoraInicio());
        binding.horaFinButton.setOnClickListener(v -> showBottomSheetDialogHoraFin());
    }

    private void toggleDaySelection(TextView dayTextView) {
        boolean isSelected = (boolean) dayTextView.getTag();

        if (isSelected) {
            dayTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_box_outline_blank_24, 0);
            dayTextView.setTag(false);
        } else {
            dayTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.baseline_check_box_24, 0);
            dayTextView.setTag(true);
        }
    }

    private List<String> getSelectedDays() {
        LinearLayout daysContainer = binding.daysContainer;
        List<String> selectedDays = new ArrayList<>();

        for (int i = 0; i < daysContainer.getChildCount(); i++) {
            TextView dayTextView = (TextView) daysContainer.getChildAt(i);
            if ((boolean) dayTextView.getTag()) {
                selectedDays.add(dayTextView.getText().toString());
            }
        }

        return selectedDays;
    }

    private void setupDaySelectionListeners() {
        LinearLayout daysContainer = binding.daysContainer;

        for (int i = 0; i < daysContainer.getChildCount(); i++) {
            TextView dayTextView = (TextView) daysContainer.getChildAt(i);

            // Configurar estado inicial como no seleccionado
            dayTextView.setTag(false);

            // Listener para alternar selección
            dayTextView.setOnClickListener(v -> toggleDaySelection(dayTextView));
        }
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
            // Formatear la hora seleccionada incluyendo AM/PM
            String horaFormateada = String.format("%02d:%02d %s", horaSeleccionada, minutoSeleccionado, ampmSeleccionado);
            binding.horaInicioButton.setText(horaFormateada);
        };

        numberPickerHora1.setOnValueChangedListener(onValueChangeListener);
        numberPickerMinuto1.setOnValueChangedListener(onValueChangeListener);
        numberPickerAMPM1.setOnValueChangedListener(onValueChangeListener);

        bottomSheetDialogHoraInicio.show();
    }

    private void showBottomSheetDialogHoraFin() {
        View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.boottom_shet_layout_ase_hf, null);
        BottomSheetDialog bottomSheetDialogHoraFin = new BottomSheetDialog(requireContext());
        bottomSheetDialogHoraFin.setContentView(bottomSheetView);

        NumberPicker numberPickerHora = bottomSheetView.findViewById(R.id.numberPickerHorafin);
        NumberPicker numberPickerMinuto = bottomSheetView.findViewById(R.id.numberPickerMinutoFin);
        NumberPicker numberPickerAMPM = bottomSheetView.findViewById(R.id.numberPicker1AMPM);

        numberPickerHora.setMinValue(1);
        numberPickerHora.setMaxValue(12);
        numberPickerHora.setWrapSelectorWheel(true);

        numberPickerMinuto.setMinValue(0);
        numberPickerMinuto.setMaxValue(59);
        numberPickerMinuto.setWrapSelectorWheel(true);

        String[] ampmValues = {"AM", "PM"};
        numberPickerAMPM.setMinValue(0);
        numberPickerAMPM.setMaxValue(1);
        numberPickerAMPM.setDisplayedValues(ampmValues);
        numberPickerAMPM.setWrapSelectorWheel(true);

        // Listener para actualizar automáticamente la hora seleccionada
        NumberPicker.OnValueChangeListener onValueChangeListener = (picker, oldVal, newVal) -> {
            int horaSeleccionada = numberPickerHora.getValue();
            int minutoSeleccionado = numberPickerMinuto.getValue();
            String ampmSeleccionado = ampmValues[numberPickerAMPM.getValue()];
            // Formatear la hora seleccionada incluyendo AM/PM
            String horaFormateada = String.format("%02d:%02d %s", horaSeleccionada, minutoSeleccionado, ampmSeleccionado);
            binding.horaFinButton.setText(horaFormateada);
        };

        numberPickerHora.setOnValueChangedListener(onValueChangeListener);
        numberPickerMinuto.setOnValueChangedListener(onValueChangeListener);
        numberPickerAMPM.setOnValueChangedListener(onValueChangeListener);

        bottomSheetDialogHoraFin.show();
    }

    // Método para convertir la hora de tipo String a LocalTime
    private LocalTime parseHoraToLocalTime(String hora) {
        // Suponemos que el formato de la hora es "hh:mm AM/PM"
        String[] parts = hora.split(" ");
        String[] timeParts = parts[0].split(":");
        int horaInt = Integer.parseInt(timeParts[0]);
        int minutoInt = Integer.parseInt(timeParts[1]);
        String ampm = parts[1];

        // Si es PM y la hora no es 12, sumamos 12 para convertir a formato de 24 horas
        if ("PM".equals(ampm) && horaInt != 12) {
            horaInt += 12;
        }

        // Si es AM y la hora es 12, la convertimos a 0 (medianoche)
        if ("AM".equals(ampm) && horaInt == 12) {
            horaInt = 0;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return LocalTime.of(horaInt, minutoInt);
        }
        return null;
    }

}