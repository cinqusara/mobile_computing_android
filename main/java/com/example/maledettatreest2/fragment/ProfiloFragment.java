package com.example.maledettatreest2.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.ImageUtil;
import com.example.maledettatreest2.MainActivity;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.databinding.FragmentProfiloBinding;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ProfiloFragment extends Fragment {

    public static final String PROFILO_LOG = "PROFILO_LOG";
    private FragmentProfiloBinding binding;

    /**
     * dichiarazione CommunicationController
     */
    CommunicationController cc;

    /**
     * istanza Model
     */
    MyModel istanzaModel = MyModel.getInstance();

    /**
     * durata di un toast
     */
    int duration = Toast.LENGTH_LONG;

    /**
     * elementi layout
     */
    private ImageView fotoProfilo;
    private String fotoProfiloBase64;
    private TextView visualizzaNomeProfilo;
    private TextView modificaNomeProfilo;
    private TextInputLayout modificaNomeProfiloLayout;
    private Button bottoneSetFotoProfilo;
    private Button bottoneSetNomeProfilo;
    private Button bottoneInviaNomeProfilo;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfiloBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /** istanza CommunicationController */
        cc = new CommunicationController(getContext());

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

        /** riferimenti oggetti layout */
        fotoProfilo = getActivity().findViewById(R.id.fotoProfilo);
        visualizzaNomeProfilo = getActivity().findViewById(R.id.nomeProfilo);
        modificaNomeProfilo = getActivity().findViewById(R.id.modificaNomeProfilo);
        modificaNomeProfiloLayout = getActivity().findViewById(R.id.modificaNomeProfiloLayout);

        bottoneSetFotoProfilo = getActivity().findViewById(R.id.bottoneModificaFotoProfilo);
        bottoneSetNomeProfilo = getActivity().findViewById(R.id.bottoneModificaNomeProfilo);
        bottoneInviaNomeProfilo = getActivity().findViewById(R.id.bottoneInviaNomeProfilo);

        refreshUserName();
        refreshPictureUser();

        /** bottone cambia immagine profilo */
        bottoneSetFotoProfilo.setOnClickListener(v -> {
            choosePicture();
        });

        /** bottone set nome profilo -> al click rendo visibile la textView per modificare il nome e il pulsante invio*/
        bottoneSetNomeProfilo.setOnClickListener(v -> {
            setVisibilityBtnSetNome(v);
        });

        bottoneInviaNomeProfilo.setOnClickListener(v -> {
            setVisibilityBtnInviaNome(v);
        });

        /** quando si clicca su invio modifica, la tastiera viene abbassata automaticamente */
        this.modificaNomeProfilo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    MainActivity.getActivity().hideKeyboard(v);
                }
            }
        });
    }

    /**
     * metodi sul click dei bottoni
     */

    private void setVisibilityBtnSetNome(View v) {
        //form modifica VISIBLE
        bottoneInviaNomeProfilo.setVisibility(View.VISIBLE);
        modificaNomeProfilo.setVisibility(View.VISIBLE);
        modificaNomeProfiloLayout.setVisibility(View.VISIBLE);

        //form modifica INVISIBLE
        bottoneSetNomeProfilo.setVisibility(View.INVISIBLE);
        visualizzaNomeProfilo.setVisibility(View.INVISIBLE);

        //appena clicchiamo sul pulsante per settare il nome viene aperta la tastiera
        MainActivity.getActivity().showKeyboard(v, (EditText) modificaNomeProfilo);
    }

    private void setVisibilityBtnInviaNome(View v) {
        try {
            uploadNewUserName();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //form modifica INVISIBLE
        bottoneInviaNomeProfilo.setVisibility(View.INVISIBLE);
        modificaNomeProfilo.setVisibility(View.INVISIBLE);
        modificaNomeProfiloLayout.setVisibility(View.INVISIBLE);

        //form modifica VISIBLE
        bottoneSetNomeProfilo.setVisibility(View.VISIBLE);
        visualizzaNomeProfilo.setVisibility(View.VISIBLE);
    }

    /**
     * funzioni per settare schermata profilo
     */
    @SuppressLint("SetTextI18n")
    public void refreshUserName() {
        Log.d(PROFILO_LOG, "popolaNomePorfilo");
        if (istanzaModel.getNome() != "") {
            Log.d(PROFILO_LOG, "nome NON null");
            visualizzaNomeProfilo.setText(istanzaModel.getNome());
        } else {
            visualizzaNomeProfilo.setText("utente_" + istanzaModel.getSid().substring(0, 3));
        }
    }

    public void refreshPictureUser() {
        Log.d(PROFILO_LOG, "popolaFotoPorfilo");
        if (istanzaModel.getFoto() != "") {
            Log.d(PROFILO_LOG, "foto NON null");
            fotoProfilo.setImageBitmap(ImageUtil.convertToBitmap(istanzaModel.getFoto()));
        } else {
            fotoProfilo.setImageBitmap(ImageUtil.drawableToBitmap(ContextCompat.getDrawable(getActivity(), R.drawable.default_user_profile_picture_hvoncb)));
        }
    }

    /**
     * funzioni per accedere alla galleria
     */
    public void choosePicture() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            // permessi non (ancora) concessi
            Log.d(PROFILO_LOG, "permessi non concessi");
            mPermissionResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            mGetContent.launch("image/*");
        }
    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Log.e(PROFILO_LOG, "onActivityResult: PERMISSION GRANTED");
                        choosePicture();
                    } else {
                        Log.e(PROFILO_LOG, "onActivityResult: PERMISSION DENIED");
                        Toast toast = Toast.makeText(getContext(), "Permessi galleria non concessi", duration);
                        toast.show();
                    }
                }
            });


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    Log.d(PROFILO_LOG, "response: " + uri);
                    if (uri != null) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getContext(), "Errore foto", duration);
                            toast.show();
                        }
                        String base64 = ImageUtil.convertToBase64(bitmap);

                        /** controllu sulla dimensione dell'immagine */
                        if (base64.length() < 137000 && bitmap.getHeight() == bitmap.getWidth()) { //deve essere minore di tot caratteri ed essere quadrata
                            fotoProfiloBase64 = base64;
                            try {
                                uploadNewProfilePicture();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast toast = Toast.makeText(getContext(), "Errore foto", duration);
                            toast.show();

                        }
                    }
                }
            });

    /**
     * metodi per settare le modifiche apportate dall'utente
     */

    public void uploadNewUserName() throws JSONException {
        if (this.modificaNomeProfilo.getText().toString().trim().length() > 0) {
            JSONObject nomeJson = new JSONObject();
            nomeJson.put("name", this.modificaNomeProfilo.getText().toString());
            cc.setProfile(
                    istanzaModel.getSid(),
                    nomeJson,
                    response -> {
                        MainActivity.getActivity().saveAllInShared();
                        istanzaModel.setNome(modificaNomeProfilo.getText().toString());
                        refreshUserName();
                        MainActivity.getActivity().saveAllInShared();
                    },
                    error -> {
                        Log.e(PROFILO_LOG, "errore setNome  " + error.getLocalizedMessage());
                        Toast toast = Toast.makeText(getContext(), "Errore caricamento nome", duration);
                        toast.show();
                    }
            );
        } else { //quando non inseriamo nessun nome
            Toast toast = Toast.makeText(getContext(), "Errore nome", duration);
            toast.show();
        }
    }

    public void uploadNewProfilePicture() throws JSONException {
        JSONObject fotoJson = new JSONObject();
        fotoJson.put("picture", this.fotoProfiloBase64);
        cc.setProfile(
                istanzaModel.getSid(),
                fotoJson,
                response -> {
                    MainActivity.getActivity().saveAllInShared();
                    istanzaModel.setFoto(fotoProfiloBase64);
                    refreshPictureUser();
                    Log.d(PROFILO_LOG, "setFoto OK");
                    MainActivity.getActivity().saveAllInShared();
                },
                error -> {
                    Log.e(PROFILO_LOG, "errore setFoto  " + error.getLocalizedMessage());
                    Toast toast = Toast.makeText(getContext(), "Errore caricamento foto", duration);
                    toast.show();
                }
        );
    }

}