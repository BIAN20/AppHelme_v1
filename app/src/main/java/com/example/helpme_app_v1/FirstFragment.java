package com.example.helpme_app_v1;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.helpme_app_v1.Interface.api.MyApi;
import com.example.helpme_app_v1.Model.Usuario;
import com.example.helpme_app_v1.databinding.FragmentFirstBinding;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SpannableString spannable = new SpannableString(getString(R.string.txtcontidions));
        ClickableSpan privacySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                mostrarModal("Políticas de privacidad", generarTextoPoliticas());
            }
        };

        ClickableSpan termsSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                mostrarModal("Términos de uso", generarTextoTerminos());
            }
        };

        int privacyStart = spannable.toString().indexOf("Políticas de privacidad");
        int privacyEnd = privacyStart + "Políticas de privacidad".length();

        int termsStart = spannable.toString().indexOf("Términos de uso");
        int termsEnd = termsStart + "Términos de uso".length();

        spannable.setSpan(privacySpan, privacyStart, privacyEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(termsSpan, termsStart, termsEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.txtContidions.setText(spannable);
        binding.txtContidions.setMovementMethod(LinkMovementMethod.getInstance());


        binding.btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.edtEmail.getText().toString().trim(); // Eliminar espacios al inicio y final

                // Verificar si el campo está vacío
                if (email.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor ingresa un email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Realizar la llamada al método POST para verificar el email
                verificarEmail(email, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            try {
                                // Parsear la respuesta JSON
                                JSONObject jsonResponse = new JSONObject(response.body().string());
                                String message = jsonResponse.getString("message");
                                int status = jsonResponse.getInt("status");

                                // Verificar si el correo existe
                                if (status == 1) { // El correo ya está registrado
                                    Toast.makeText(getContext(), "El correo ya está registrado", Toast.LENGTH_SHORT).show();
                                } else { // El correo no existe
                                    Toast.makeText(getContext(), "El correo no existe en la base de datos", Toast.LENGTH_SHORT).show();

                                    // Crear objeto usuario
                                    Usuario usuario = new Usuario();
                                    usuario.setEmail(email);

                                    // Navegar al siguiente fragmento
                                    FirstFragmentDirections.ActionFirstFragmentToSecondFragment action =
                                            FirstFragmentDirections.actionFirstFragmentToSecondFragment(usuario);
                                    NavHostFragment.findNavController(FirstFragment.this).navigate(action);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error al procesar la respuesta del servidor", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error al verificar el correo: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
    private String generarTextoPoliticas() {
        return "📜 Políticas de Privacidad\n\n" +
                "🔒 1. Información recopilada: Recopilamos únicamente los datos necesarios para ofrecerte un servicio personalizado y de calidad. ¡Nada más, nada menos! 😊\n\n" +
                "💾 2. Uso de datos: Tus datos nos ayudan a brindarte la mejor experiencia. Prometemos usarlos responsablemente. 💪\n\n" +
                "🛡️ 3. Almacenamiento: Guardamos tus datos en un entorno seguro, ¡nunca serán compartidos sin tu permiso! 🚀\n\n" +
                "📩 ¿Dudas? Escríbenos o consulta más detalles en nuestra página web. ¡Estamos aquí para ayudarte! 💌";
    }

    private String generarTextoTerminos() {
        return "📄 Términos de Uso\n\n" +
                "✅ 1. Aceptación: Al usar nuestra app, aceptas con gusto estos términos diseñados para ti. ¡Bienvenid@ a la familia! 🤗\n\n" +
                "🤝 2. Responsabilidades: Ayúdanos ayudándote, comparte información precisa para que nuestras asesorías sean efectivas. 🙌\n\n" +
                "💡 3. Uso del servicio: Esta app está hecha con amor 💖 para brindarte asesorías y consultas. Por favor, úsala con el mismo cariño. 😊\n\n" +
                "📲 Si tienes alguna pregunta o duda, revisa nuestros términos completos en la web o contáctanos. ¡Siempre felices de ayudarte! 🌟";
    }


    private void mostrarModal(String titulo, String contenido) {
        new AlertDialog.Builder(requireContext())
                .setTitle(titulo)
                .setMessage(contenido)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private void verificarEmail(String email, Callback<ResponseBody> callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupo6tdam2024.pythonanywhere.com/") // Cambia esto por la URL base de tu servidor
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi api = retrofit.create(MyApi.class);

        // Crear un objeto JSON para enviar el email
        Map<String, String> body = new HashMap<>();
        body.put("email", email);

        // Llamar al método POST
        Call<ResponseBody> call = api.verificarcorreo(body);
        call.enqueue(callback);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}