package com.example.maledettatreest2.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.maledettatreest2.MainActivity;
import com.example.maledettatreest2.MappaActivity;
import com.example.maledettatreest2.Stazione;
import com.example.maledettatreest2.db_foto.AppDatabase;
import com.example.maledettatreest2.db_foto.FotoProfiloUtente;
import com.example.maledettatreest2.linee.Linea;
import com.example.maledettatreest2.linee.Tratta;
import com.example.maledettatreest2.posts.AdapterPost;
import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.posts.CreaPostActivity;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public static final String MTE_LOG = "MTE_LOG";



    /** istanza Model */
    MyModel istanza = MyModel.getInstance();

    /** dichiarazione CommunicationController */
    CommunicationController cc;

    /** RecyclerView */
    RecyclerView recyclerView;
    AdapterPost adapterPost ;

    /** inizializzazione DB */
    private AppDatabase db;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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

        /** istanza CommunicationController */
        cc = new CommunicationController(getContext());

        /** Istanza DB */
        db = Room.databaseBuilder(getActivity().getApplicationContext(), AppDatabase.class,"students_db").build();

        TextView ultimaDirezione;
        TextView ultimaLinea;
        ultimaDirezione = getActivity().findViewById(R.id.textUltimaDirezione);
        ultimaLinea = getActivity().findViewById(R.id.textUltimaLinea);
        ultimaDirezione.setText(istanza.getUltimaDirezione());
        ultimaLinea.setText(istanza.getUltimaLinea());

        Button btnInvertiDirezione = getActivity().findViewById(R.id.btnInvertiDirezione);
        btnInvertiDirezione.setOnClickListener((v) ->{
            istanza.invertiDirezione();
            ultimaDirezione.setText(istanza.getUltimaDirezione());
            ultimaLinea.setText(istanza.getUltimaLinea());
        });

        Button btnDettagliTratta = getActivity().findViewById(R.id.btnDettagliTratta);
        btnDettagliTratta.setOnClickListener((v) ->{
            downloadStazioni();
        });


        Button btnCreaPost = getActivity().findViewById(R.id.btnCreaPost);
        btnCreaPost.setOnClickListener((v) ->{
            Intent intent = new Intent(getContext(), CreaPostActivity.class);
            startActivity(intent);
        });


        //recycleView
        recyclerView = getActivity().findViewById(R.id.recycleViewPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPost = new AdapterPost(getContext());
        recyclerView.setAdapter(adapterPost);



        new Thread(()->{

        }).start();


    }


    public void downloadStazioni(){
        Log.d(MTE_LOG,"download stazioni");
        cc.getStations(
                istanza.getSid(),
                istanza.getDid(),
                response-> {
                    Log.d(MTE_LOG,"stazioni da risposta "+response.toString());
                    initStazioniFromJson(response);
                    Intent intent = new Intent(getContext(), MappaActivity.class);
                    startActivity(intent);

                },
                error -> Log.e(MTE_LOG, "errore download stazioni  "+ error.getLocalizedMessage())
        );
    }

    public void initStazioniFromJson(JSONObject netRisposta){
        try {
            JSONArray arrayStazioniJson = netRisposta.getJSONArray("stations");
            istanza.azzeraStazioni();
            for (int i =0; i<arrayStazioniJson.length();i++){
                JSONObject stazione = arrayStazioniJson.getJSONObject(i);

                String nome = stazione.getString("sname");
                Double latitudine = stazione.getDouble("lat");
                Double longitudine = stazione.getDouble("lon");

                Stazione s = new Stazione(nome, latitudine,longitudine);
                istanza.addStazione(s);
            }

            Log.d(MTE_LOG, "stazioni model  ->  "+istanza.getStazioni().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }









}