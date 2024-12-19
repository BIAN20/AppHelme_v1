package com.example.helpme_app_v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpme_app_v1.databinding.FragmentInicioAsesorBinding;
import com.example.helpme_app_v1.databinding.FragmentInicioBinding;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioFragmentAsesor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioFragmentAsesor extends Fragment {
    FragmentInicioAsesorBinding binding;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioFragmentAsesor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InicioFragmentAsesor.
     */
    // TODO: Rename and change types and number of parameters
    public static InicioFragmentAsesor newInstance(String param1, String param2) {
        InicioFragmentAsesor fragment = new InicioFragmentAsesor();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioAsesorBinding.inflate(inflater, container, false);

        //return inflater.inflate(R.layout.fragment_inicio_asesor, container, false);

        if (savedInstanceState == null) {
            replaceFragment(new HomeAsesorFragment());
            // binding.bottomNavigationView.setSelectedItemId(R.id.home);
        }

        binding.bottomNavigationView.setOnItemSelectedListener( item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.homeasesoria){
                replaceFragment(new HomeAsesorFragment());
                return true;
            }
            else if(itemId == R.id.estaasesorias){
                replaceFragment(new EstaAsesoriaFragment() );
                return  true;
            }
            else if( itemId == R.id.economia){
                replaceFragment(new TokensFragment());
                return  true;
            } else if (itemId == R.id.perfil) {
                replaceFragment(new PerfilAsesorFragment());
                return true;
            }
            return false;
        });

        return binding.getRoot();


    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }




}