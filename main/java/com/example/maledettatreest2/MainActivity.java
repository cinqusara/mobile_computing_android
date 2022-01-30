package com.example.maledettatreest2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.maledettatreest2.db_foto.AppDatabase;
import com.example.maledettatreest2.db_foto.FotoProfiloUtente;
import com.example.maledettatreest2.fragment.HomeFragment;
import com.example.maledettatreest2.fragment.ProfiloFragment;
import com.example.maledettatreest2.fragment.TratteFragment;
import com.example.maledettatreest2.linee.Linea;
import com.example.maledettatreest2.linee.Tratta;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.maledettatreest2.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    public static final String MAINACTIVITY_LOG = "MAINACTIVITY_LOG";
    private ActivityMainBinding binding;

    private boolean avvioSuSchermataTratte = true;

    /**
     * istanza MainActivity
     */
    private static MainActivity istanzaMainActivity;

    /**
     * istanza Model
     */
    MyModel istanzaModel = MyModel.getInstance();

    /**
     * dichiarazione SharedPreferences
     */
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    /**
     * dichiarazione CommunicationController
     */
    CommunicationController cc;

    /**
     * inizializzazione DB
     */
    private AppDatabase db;

    /**
     * durata di un toast
     */
    int duration = Toast.LENGTH_SHORT;

    /**
     * riferimento a bottom navigation
     */
    BottomNavigationView navView;

    /**
     * riferimento a fragment
     */
    HomeFragment homeFragment = null;
    TratteFragment tratteFragment = null;
    ProfiloFragment profiloFragment = null;

    /**
     * funzione per fare il replace dei fragment
     */
    public void replaceFragment(Fragment fragment, int navName) {
        Log.d(MAINACTIVITY_LOG, "replace " + fragment.toString());
        //Scommentare se da problemi di trasparenza tra fragment
        //FrameLayout fl = (FrameLayout) findViewById(R.id.fragmentContainerView);
        //fl.removeAllViews();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainerView, fragment, null)
                .commit();
        navView.getMenu().findItem(navName).setChecked(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navView = findViewById(R.id.nav_view);

        /** controllo internet */
        if (!isNetworkConnected()) {
            showDialog("Errore", "Non disponi di una connessione ad internet");
        }

        /** riferimenti ai vari fragment*/
        homeFragment = new HomeFragment();
        tratteFragment = new TratteFragment();
        profiloFragment = new ProfiloFragment();

        /** istanza main activity */
        istanzaMainActivity = this;

        /** switch sui vai fragment*/
        binding.navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(homeFragment, R.id.navigation_home);
                    break;
                case R.id.navigation_tratte:
                    replaceFragment(tratteFragment, R.id.navigation_tratte);
                    break;
                case R.id.navigation_profilo:
                    replaceFragment(profiloFragment, R.id.navigation_profilo);
                    break;
            }
            return true;
        });

        /** Istanza DB */
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db_fotoProfiloUtente").build();
        //allowMainThreadQueries()

        /** inizializzazione SharedPreferences */
        settings = getSharedPreferences("settings", 0);
        editor = settings.edit();

        /** inizializzazione CommunicationController */
        cc = new CommunicationController(this);

        clearModel();

        /** controllo primo avvio */
        if (checkFirstStart()) {  //PRIMO AVVIO
            isFirstStart();
        } else if (istanzaModel.getDid() == "") {// dal SECONDO AVVIO senza did
            isSecondStartWithoutDid();
        } else if (istanzaModel.getDid() != "") { //dal SECONDO AVVIO con did
            avvioSuSchermataTratte = false;
            Log.d(MAINACTIVITY_LOG, "SECONDO AVVIO con did");
            downloadLinee();
            replaceFragment(homeFragment, R.id.navigation_home);
        } else {//errore generico
            showDialog("Errore", "Errore generico");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveAllInShared();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveAllInShared();
    }

    /**
     * ritorna il riferimento alla main activity
     */
    public static MainActivity getActivity() {
        return istanzaMainActivity;
    }

    /**
     * funzioni per il primo avvio
     */

    public boolean checkFirstStart() {
        if (settings.getBoolean("primoAvvio", true) && istanzaModel.getSid() == "") {
            editor.putBoolean("primoAvvio", false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    private void isFirstStart() {
        avvioSuSchermataTratte = true;
        Log.d(MAINACTIVITY_LOG, "PRIMO AVVIO");
        replaceFragment(tratteFragment, R.id.navigation_tratte);
        navView.getMenu().getItem(0).setEnabled(false);  // disabilita bottone Home

        register();
    }


    /**
     * funzioni per il secondo avvio
     */

    private void isSecondStartWithoutDid() {
        Log.d(MAINACTIVITY_LOG, "SECONDO AVVIO senza did");
        clearModel();
        avvioSuSchermataTratte = true;
        replaceFragment(tratteFragment, R.id.navigation_tratte);
        navView.getMenu().getItem(0).setEnabled(false);  // disabilita bottone Home
        downloadLinee();
    }

    /**
     * funzione per ritornare alla schermata home
     */
    public void goToHomePage() {
        replaceFragment(homeFragment, R.id.navigation_home);
    }

    /**
     * metodi sul model
     */
    public void clearModel() {
        istanzaModel.setSid(settings.getString("sid", ""));
        istanzaModel.setDid(settings.getString("did", ""));
        istanzaModel.setNome(settings.getString("nome", ""));
        istanzaModel.setFoto(settings.getString("foto", ""));
        Log.d("test12", "ripristinaMOdel -> " + istanzaModel.getFoto());

        Thread thread = new Thread() {
            public void run() {
                List<FotoProfiloUtente> arrayfpu = db.fotoProfiloUtentiDAO().getAll();
                istanzaModel.setFotoProfiloUtenti((ArrayList<FotoProfiloUtente>) arrayfpu);
            }
        };
        thread.start();
    }

    /**
     * salvataggio persistente
     */
    public void saveAllInShared() {
        Log.d("MTE", "salva ttutto");
        editor.putString("sid", istanzaModel.getSid());
        editor.putString("nome", istanzaModel.getNome());
        editor.putString("foto", istanzaModel.getFoto());
        editor.putString("did", istanzaModel.getDid());
        editor.commit();
    }

    /**
     * chiamate di rete
     */
    public void downloadLinee() {
        Log.d(MAINACTIVITY_LOG, "download linee   SID-> " + istanzaModel.getSid());
        cc.getLines(
                istanzaModel.getSid(),
                response -> {
                    Log.d(MAINACTIVITY_LOG, "linee da risposta " + response.toString());
                    createLineFromJSON(response);
                    if (avvioSuSchermataTratte) {
                        tratteFragment.notifyAdapterLinee();
                        saveAllInShared();
                    } else {
                        homeFragment.refreshHomeScreen();
                        saveAllInShared();
                    }
                },
                error -> Log.e(MAINACTIVITY_LOG, "errore download linee  " + error.getLocalizedMessage())
        );
    }

    private void register() {
        Log.d(MAINACTIVITY_LOG, "register");
        cc.register(
                response -> {
                    try {
                        istanzaModel.setSid(response.getString("sid"));
                        Log.d(MAINACTIVITY_LOG, "sid salvato (model e shared) ->  " + istanzaModel.getSid());
                        saveAllInShared();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    downloadLinee();
                },
                error -> {
                    Log.e(MAINACTIVITY_LOG, "errore register  " + error.getLocalizedMessage());
                }
        );
    }

    /**
     * crea oggetto linea da JSON
     */
    public void createLineFromJSON(JSONObject netRisposta) {
        try {
            JSONArray arrayLineeJson = netRisposta.getJSONArray("lines");
            istanzaModel.clearLines();
            for (int i = 0; i < arrayLineeJson.length(); i++) {
                JSONObject lines = arrayLineeJson.getJSONObject(i);

                JSONObject terminus1 = lines.getJSONObject("terminus1");
                String nomeTratta1 = terminus1.getString("sname");
                String didTratta1 = terminus1.getString("did");

                JSONObject terminus2 = lines.getJSONObject("terminus2");
                String nomeTratta2 = terminus2.getString("sname");
                String didTratta2 = terminus2.getString("did");

                Tratta t1 = new Tratta(nomeTratta1, didTratta1);
                Tratta t2 = new Tratta(nomeTratta2, didTratta2);
                Linea l = new Linea(t1, t2);
                istanzaModel.addLine(l);
            }
            Log.d(MAINACTIVITY_LOG, "linee model  ->  " + istanzaModel.getLinee().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * notifiche ai vari adapter
     */
    public void notifyAdapterPostFromMainActivity() {
        homeFragment.notifyAdapterPost();
        homeFragment.notifyAdapterOfficialPost();
    }

    public void notifyAdapterLineFromMainActivity() {
        tratteFragment.notifyAdapterLinee();
    }

    /**
     * getter fragment
     */
    public Fragment getHomeFragment() {
        return homeFragment;
    }


    /**
     * inserimento dati nel DB
     */
    public void insertPictureInDB(FotoProfiloUtente fpu) {
        Log.d(MAINACTIVITY_LOG, "insertFotoProfiloDB");
        Thread thread = new Thread() {
            public void run() {
                db.fotoProfiloUtentiDAO().delete(fpu);
                db.fotoProfiloUtentiDAO().insertFotoProfiloUtente(fpu);
            }
        };
        thread.start();
    }

    /**
     * metodi per mostrare/nascondere in automatico la tastiera
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showKeyboard(View view, EditText mEtSearch) {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * controllo per vedere se c'è internet
     */
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;

    }

    /**
     * metodo per settare pop-up
     */
    public void showDialog(String titolo, String messaggio) {
        new AlertDialog.Builder(this)
                .setTitle(titolo)
                .setMessage(messaggio)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}



