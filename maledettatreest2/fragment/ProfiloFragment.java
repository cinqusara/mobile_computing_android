package com.example.maledettatreest2.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.maledettatreest2.R;
import com.example.maledettatreest2.databinding.FragmentProfiloBinding;

public class ProfiloFragment extends Fragment {

    private static final int TYPE_TEXT_VARIATION_PERSON_NAME = 96;
    public static final String MTE_LOG = "MTE_LOG";
    private FragmentProfiloBinding binding;

    private ImageView fotoProfilo;
    TextView nomeProfilo;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfiloBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onStart() {
        super.onStart();
        fotoProfilo = getActivity().findViewById(R.id.fotoProfilo);
        fotoProfilo.setOnClickListener(v -> choosePicture());

        nomeProfilo= getActivity().findViewById(R.id.nomeProfilo);
        nomeProfilo.setInputType(TYPE_TEXT_VARIATION_PERSON_NAME);

    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    Log.d("onActivityResult:", "RES........" + String.valueOf(uri));
                    //TODO

                }
            });


    public void choosePicture() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            // permessi non (ancora) concessi
            Log.d(MTE_LOG, "permessi non concessi");
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        } else {
            mGetContent.launch("image/*");
        }
    }



}