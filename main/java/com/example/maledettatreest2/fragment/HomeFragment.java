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

import com.example.maledettatreest2.MainActivity;
import com.example.maledettatreest2.MappaActivity;
import com.example.maledettatreest2.Stazione;
import com.example.maledettatreest2.posts.AdapterOfficialPost;
import com.example.maledettatreest2.posts.AdapterPost;
import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.posts.CreaPostActivity;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.databinding.FragmentHomeBinding;
import com.example.maledettatreest2.posts.InfoOfficialPost;
import com.example.maledettatreest2.posts.OfficialPost;
import com.example.maledettatreest2.posts.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    public static final String HOME_LOG = "HOME_LOG";
    public static final String ESAME = "ESAME";

    /**
     * istanza Model
     */
    MyModel istanzaModel = MyModel.getInstance();

    /**
     * dichiarazione CommunicationController
     */
    CommunicationController cc;

    /**
     * RecyclerView
     */
    RecyclerView recyclerViewPost;
    AdapterPost adapterPost;

    RecyclerView recyclerViewOffPost;
    AdapterOfficialPost adapterOffPost;

    /**
     * Array FotoProfiloUtenti
     */
    //private ArrayList<FotoProfiloUtente> arrayFotoProfiloUtente = new ArrayList<>();

    /**
     * Riferimenti alle direzioni
     */
    TextView ultimaDirezione;
    TextView ultimaLinea;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /** istanza CommunicationController */
        cc = new CommunicationController(getContext());

        /** riferimento alla navView */
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.getMenu().getItem(0).setEnabled(true);  // attiva bottone Home

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
        downloadPosts();
        downloadOfficialPosts();

        /** riferimenti agli elementi nel layout*/
        ultimaDirezione = getActivity().findViewById(R.id.textUltimaDirezione);
        ultimaLinea = getActivity().findViewById(R.id.textUltimaLinea);
        ultimaDirezione.setText(istanzaModel.getLastDirection());
        ultimaLinea.setText(istanzaModel.getLastLine());

        Button btnInvertiDirezione = getActivity().findViewById(R.id.btnInvertiDirezione);
        btnInvertiDirezione.setOnClickListener((v) -> {
            reverseDirection();
        });

        Button btnDettagliTratta = getActivity().findViewById(R.id.btnDettagliTratta);
        btnDettagliTratta.setOnClickListener((v) -> {
            downloadStazioni();
        });

        Button btnCreaPost = getActivity().findViewById(R.id.btnCreaPost);
        btnCreaPost.setOnClickListener((v) -> {
            Intent intent = new Intent(getContext(), CreaPostActivity.class);
            startActivity(intent);
        });

        /** recycle view post */
        recyclerViewPost = getActivity().findViewById(R.id.recyclerViewPost);
        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterPost = new AdapterPost(getContext());
        recyclerViewPost.setAdapter(adapterPost);

        /** recycle view off post */
        recyclerViewOffPost = getActivity().findViewById(R.id.recycleViewOfficialPost);
        recyclerViewOffPost.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterOffPost = new AdapterOfficialPost(getContext(), this);
        recyclerViewOffPost.setAdapter(adapterOffPost);
    }

    /**
     * metodi sul click dei bottoni
     */

    private void reverseDirection() {
        istanzaModel.changeDirection();
        MainActivity.getActivity().saveAllInShared();
        ultimaDirezione.setText(istanzaModel.getLastDirection());
        ultimaLinea.setText(istanzaModel.getLastLine());
        downloadPosts();
        downloadOfficialPosts();
    }

    public void downloadStazioni() {
        Log.d(HOME_LOG, "download stazioni");
        cc.getStations(
                istanzaModel.getSid(),
                istanzaModel.getDid(),
                response -> {
                    Log.d(HOME_LOG, "stazioni da risposta " + response.toString());
                    createStazioniFromJSON(response);
                    Intent intent = new Intent(getContext(), MappaActivity.class);
                    startActivity(intent);
                },
                error -> Log.e(HOME_LOG, "errore download stazioni  " + error.getLocalizedMessage())
        );
    }

    /** chiamata di rete per download post*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloadPosts() {
        Log.d(HOME_LOG, "download post");

        cc.getPosts(
                istanzaModel.getSid(),
                istanzaModel.getDid(),
                response -> {
                    Log.d(HOME_LOG, "post da risposta " + response.toString());
                    createPostFromJSON(response);
                    refreshHomeScreen();

                },
                error -> Log.e(HOME_LOG, "errore download posts  " + error.getLocalizedMessage())
        );
    }

    public void downloadOfficialPosts(){
        Log.d(HOME_LOG, "download official post");
        Log.d(HOME_LOG, "esamegennaio did: " +  istanzaModel.getDid());
        cc.getOfficialPosts(
                istanzaModel.getDid(),
                response -> {
                    Log.d(HOME_LOG, "post da risposta " + response.toString());
                    createOfficialPostFromJSON(response);
                    refreshHomeScreen();
                },
                error -> {
                    Log.e(HOME_LOG, "errore download official posts  " + error.getLocalizedMessage());
                }
        );

    }



    /** metodi creazioni oggetti dal JSON*/

    public void createStazioniFromJSON(JSONObject responseStazioni) {
        try {
            JSONArray arrayStazioniJson = responseStazioni.getJSONArray("stations"); //provo a prendere l'array dal json
            istanzaModel.azzeraStazioni();

            for (int i = 0; i < arrayStazioniJson.length(); i++) {
                JSONObject stazione = arrayStazioniJson.getJSONObject(i);

                String nome = stazione.getString("sname");
                Double latitudine = stazione.getDouble("lat");
                Double longitudine = stazione.getDouble("lon");

                Stazione s = new Stazione(nome, latitudine, longitudine);
                istanzaModel.addStazione(s);
            }
            Log.d(HOME_LOG, "stazioni model  ->  " + istanzaModel.getStazioni().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createPostFromJSON(JSONObject postsResponse) {
        try {
            Log.d(HOME_LOG, "create post from json");
            JSONArray arrayPostsJson = postsResponse.getJSONArray("posts"); //dalla risposta prende array dei post
            istanzaModel.clearPosts();

            for (int i = 0; i < arrayPostsJson.length(); i++) {
                JSONObject postObj = arrayPostsJson.getJSONObject(i);

                String nomeAutore = postObj.getString("authorName");
                String uidAutore = postObj.getString("author");
                String dataOra = postObj.getString("datetime");
                int versioneFoto = postObj.getInt("pversion");

                Boolean followAutore = postObj.getBoolean("followingAuthor");

                /** controllo presenza dei dati opzionali */
                String ritardo;
                String stato;
                String commento;
                try {
                    ritardo = postObj.getString("delay");
                } catch (Exception e) {
                    ritardo = "";
                }

                try {
                    stato = postObj.getString("status");
                } catch (Exception e) {
                    stato = "";
                }

                try {
                    commento = postObj.getString("comment");
                } catch (Exception e) {
                    commento = "";
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");
                LocalDateTime dt = LocalDateTime.parse(dataOra, formatter);

                Post post = new Post(nomeAutore, uidAutore, dt, versioneFoto, followAutore, ritardo, stato, commento);
                istanzaModel.addPost(post);
            }
            Log.d(HOME_LOG, "posts model  ->  " + istanzaModel.getPosts().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createOfficialPostFromJSON(JSONObject response) {
        try {
            Log.d(HOME_LOG, "create official post from json");
            JSONArray arrayOfficialPostsJson = response.getJSONArray("officialposts"); //dalla risposta prende array dei post
            Log.d(ESAME, "esamegennaio numero di post ufficiali: " +  arrayOfficialPostsJson.length());
            istanzaModel.clearOffPosts();

            for (int i = 0; i < arrayOfficialPostsJson.length(); i++) {
                JSONObject postObj = arrayOfficialPostsJson.getJSONObject(i);

                String title = postObj.getString("title");
                String timestamp = postObj.getString("timestamp");
                String description = postObj.getString("description");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");
                LocalDateTime dt = LocalDateTime.parse(timestamp, formatter);

                OfficialPost officialPost = new OfficialPost(title, dt, description);

                Log.d(ESAME, "esamegennaio title: " + officialPost.getTitle() + " timestamp: " + officialPost.getTimestamp());
                istanzaModel.addOfficialPost(officialPost);
            }
            Log.d(HOME_LOG, "official posts model  ->  " + istanzaModel.getPosts().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /** popola schermata home */
    public void refreshHomeScreen() {
        TextView ultimaDirezione = getActivity().findViewById(R.id.textUltimaDirezione);
        TextView ultimaLinea = getActivity().findViewById(R.id.textUltimaLinea);
        ultimaDirezione.setText(istanzaModel.getLastDirection());
        ultimaLinea.setText(istanzaModel.getLastLine());
        notifyAdapterPost();
        notifyAdapterOfficialPost();
    }

    /** metodo notify per aggiornare schermata home */
    public void notifyAdapterPost() {
        adapterPost.notifyDataSetChanged();
    }

    public void notifyAdapterOfficialPost() {
        adapterOffPost.notifyDataSetChanged();
    }

    /** metodo per restare in ascolto sugli official post*/
    public void onClickViewHolder(View view, int adapterPosition) {
        Log.d(HOME_LOG, "click on item " + adapterPosition);
        Bundle args = new Bundle();
        OfficialPost officialPostselected = istanzaModel.getAllOfficialPosts().get(adapterPosition);
        istanzaModel.setOfficialPostSelected(officialPostselected);
        Log.d(HOME_LOG, "post selected " + istanzaModel.getOfficialPostSelected());
        Intent intent = new Intent(getContext(), InfoOfficialPost.class);
        startActivity(intent);
    }
}