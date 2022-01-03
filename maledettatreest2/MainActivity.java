package com.example.maledettatreest2;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.maledettatreest2.linee.Linea;
import com.example.maledettatreest2.linee.Tratta;
import com.example.maledettatreest2.posts.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.maledettatreest2.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    public static final String MTE_LOG = "MTE_LOG";
    private ActivityMainBinding binding;

    /** istanza MainActivity */
    private static MainActivity istanzaActivity;

    /** istanza Model */
    MyModel istanza = MyModel.getInstance();

    /** dichiarazione SharedPreferences */
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    /** dichiarazione CommunicationController */
    CommunicationController cc;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_tratte, R.id.navigation_profilo)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        /////
        istanzaActivity = this;

        /** inizializzazione SharedPreferences */
        settings = getSharedPreferences("settings", 0);
        editor = settings.edit();

        /** inizializzazione CommunicationController */
        cc = new CommunicationController(this);

        /** controllo primo avvio */
        if (primoAvvio()) {
            Log.d(MTE_LOG, "*******PRIMO AVVIO*****  ");
            // Chiamata register
            cc.register(
                    response-> {
                        //salvataggio sid
                        try {
                            editor.putString("sid", response.getString("sid"));
                            editor.commit();
                            istanza.setSid(response.getString("sid"));
                            Log.d(MTE_LOG, "sid salvato (model e shared) ->  "+istanza.getSid());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        downloadLinee();
                    },
                    error -> {
                        Log.e(MTE_LOG, "errore register  "+ error.getLocalizedMessage());
                    }

            );
            //modifica prima schermata
            BottomNavigationView bottomNavigationView;
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
            bottomNavigationView.setSelectedItemId(R.id.navigation_tratte);
        }else{
            Log.d(MTE_LOG, "*******SECONDO + AVVIO*****  ");
            /**popolamento model */
            ripristinaModel();
            downloadLinee();
            downloadPosts();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.putString("sid", istanza.getSid());
        editor.putString("nome", istanza.getNome());
        editor.putString("foto", istanza.getFoto());
        editor.putString("did", istanza.getDid());
        editor.commit();

    }

    public static MainActivity getActivity() {
        return istanzaActivity;
    }

    public void passaAHome(){
        BottomNavigationView bottomNavigationView;
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }


    public void ripristinaModel(){
        istanza.setSid(settings.getString("sid", "sid error"));
        istanza.setDid(settings.getString("did", "did error"));
        //istanza.setNome(settings.getString("nome", "nome error"));
        //istanza.setFoto(settings.getString("foto", "foto error"));

    }

    public boolean primoAvvio(){
        if (settings.getBoolean("primoAvvio", true)){
            editor.putBoolean("primoAvvio", false);
            editor.commit();
            return true;
        }else {
            return false;
        }
    }



    public void setUltimaTrattaVista(String did){
        editor.putString("did", did);
        editor.commit();
    }

    public void downloadLinee(){
        Log.d(MTE_LOG,"download linee");
        cc.getLines(
                istanza.getSid(),
                response-> {
                    Log.d(MTE_LOG,"linee da risposta "+response.toString());
                    initLineeFromJson(response);
                    popolaBacheca();

                },
                error -> Log.e(MTE_LOG, "errore download linee  "+ error.getLocalizedMessage())
        );
    }

    public void initLineeFromJson(JSONObject netRisposta){
        try {
            JSONArray arrayLineeJson = netRisposta.getJSONArray("lines");
            istanza.azzeraLinee();
            for (int i =0; i<arrayLineeJson.length();i++){
                JSONObject linea = arrayLineeJson.getJSONObject(i);

                JSONObject terminus1 = linea.getJSONObject("terminus1");
                String nomeTratta1 = terminus1.getString("sname");
                String didTratta1 = terminus1.getString("did");

                JSONObject terminus2 = linea.getJSONObject("terminus2");
                String nomeTratta2 = terminus2.getString("sname");
                String didTratta2 = terminus2.getString("did");

                Tratta t1 = new Tratta(nomeTratta1, didTratta1);
                Tratta t2 = new Tratta(nomeTratta2, didTratta2);
                Linea l = new Linea(t1, t2);
                istanza.addLinea(l);
            }

            Log.d(MTE_LOG, "linee model  ->  "+istanza.getLinee().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void downloadPosts(){
        Log.d(MTE_LOG,"download post");
        cc.getPosts(
                istanza.getSid(),
                istanza.getDid(),
                response-> {
                    Log.d(MTE_LOG,"post da risposta "+response.toString());
                    initPostsFromJson(response);
                    //TODO
                    //adapter.notifyDataSetChanged();

                },
                error -> Log.e(MTE_LOG, "errore download posts  "+ error.getLocalizedMessage())
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initPostsFromJson(JSONObject netRisposta){
        try {
            Log.d(MTE_LOG, "INIT POST FROM JSON -------------");
            JSONArray arrayPostsJson = netRisposta.getJSONArray("posts");
            istanza.azzeraPosts();

            for (int i =0; i<arrayPostsJson.length(); i++){

                JSONObject postObj = arrayPostsJson.getJSONObject(i);

                String nomeAutore = postObj.getString("authorName");
                String uidAutore = postObj.getString("author");
                String dataOra = postObj.getString("datetime");
                String versioneFoto = postObj.getString("pversion");

                Boolean followAutore = postObj.getBoolean("followingAuthor");

                //opzionali
                String ritardo;
                String stato;
                String commento;
                try {
                    ritardo = postObj.getString("delay");
                }catch(Exception e) {
                    ritardo = "";
                }

                try {
                    stato = postObj.getString("status");
                }catch(Exception e) {
                    stato = "";
                }

                try {
                    commento = postObj.getString("comment");
                }catch(Exception e) {
                    commento = "";
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n");
                LocalDateTime dt = LocalDateTime.parse(dataOra, formatter);

                //LocalDateTime dt = LocalDateTime.now();
                Post post = new Post(nomeAutore,uidAutore, dt, versioneFoto, followAutore, ritardo, stato, commento);
                istanza.addPost(post);
            }

            Log.d(MTE_LOG, "posts model  ->  "+istanza.getPosts().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public void popolaBacheca(){
        TextView ultimaDirezione;
        TextView ultimaLinea;
        ultimaDirezione = this.findViewById(R.id.textUltimaDirezione);
        ultimaLinea = this.findViewById(R.id.textUltimaLinea);
        ultimaDirezione.setText(istanza.getUltimaDirezione());
        ultimaLinea.setText(istanza.getUltimaLinea());
    }


}



