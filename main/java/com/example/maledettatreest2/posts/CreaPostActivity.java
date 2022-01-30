package com.example.maledettatreest2.posts;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.MainActivity;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;

import org.json.JSONException;
import org.json.JSONObject;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CreaPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String CREAPOST_LOG = "CREAPOST_LOG";
    private static final String[] optionsStato = new String[]{"Seleziona", "In orario", "Ritardo di pochi minuti", "Ritardo più di 15 minuti", "Treni soppressi"};
    private Spinner tendinaRitardo;

    private String ritardo = "";
    private String stato = "";
    private TextView commento;
    private JSONObject campiPost = new JSONObject();

    /**
     * istanza Model
     */
    MyModel istanza = MyModel.getInstance();

    /**
     * dichiarazione CommunicationController
     */
    CommunicationController cc;

    /**
     * parametro durata visibilità toast
     */
    int duration = Toast.LENGTH_LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creapost);

        /** inizializzazione CommunicationController */
        cc = new CommunicationController(this);

        /** riferimenti ai vari oggetti del layout */
        tendinaRitardo = findViewById(R.id.spinnerRitardo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, optionsStato);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tendinaRitardo.setAdapter(adapter);
        tendinaRitardo.setOnItemSelectedListener(this);
        commento = findViewById(R.id.textCommento);

        Button btnPubblicaPost = findViewById(R.id.btnPubblicaPost);
        btnPubblicaPost.setOnClickListener((v) -> {
            postOnHomePage();
        });
    }

    /**
     * metodi onClick pulsante
     */
    private void postOnHomePage() {
        try {
            if (checkMinFields_createObj() && checkCommentLength()) {
                Log.d(CREAPOST_LOG, "oggetto  " + campiPost.toString());
                createNewPost();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodi per il controllo del post
     */
    public boolean checkMinFields_createObj() throws JSONException {
        if (this.stato != "" || this.ritardo != "" || !(this.commento.getText().toString().equals(""))) {
            if (this.stato != "") {
                this.campiPost.put("status", this.stato);
            }
            if (this.ritardo != "") {
                this.campiPost.put("delay", this.ritardo);
            }
            if (!(this.commento.getText().toString().equals(""))) {
                this.campiPost.put("comment", this.commento.getText().toString());
            }
            return true;
        }
        Log.d(CREAPOST_LOG, "almeno un campo richiesto");
        Toast toast = Toast.makeText(this, "Inserisci almeno un campo", duration);
        toast.show();
        return false;
    }

    private boolean checkCommentLength() {
        if (commento.getText().toString().trim().length() > 100) {
            Toast toast = Toast.makeText(this, "Commento troppo lungo", duration);
            toast.show();
            return false;
        }
        return true;
    }

    /**
     * crea nuovo post
     */
    public void createNewPost() throws JSONException {
        cc.addPost(
                istanza.getSid(),
                istanza.getDid(),
                this.campiPost,
                response -> {
                    Log.d(CREAPOST_LOG, "ok " + response.toString());
                },
                error -> Log.e(CREAPOST_LOG, "errore" + error.toString())
        );
    }

    /**
     * metodi richiamati direttamente dagli oggeti nel layout
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.radioProblemi:
                if (checked)
                    this.stato = "2";
                break;
            case R.id.radioAccettabile:
                if (checked)
                    this.stato = "1";
                break;
            case R.id.radioIdeale:
                if (checked)
                    this.stato = "0";
                break;
            default:
                this.stato = "";
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                this.ritardo = "";
                break;
            case 1:
                this.ritardo = "0";
                break;
            case 2:
                this.ritardo = "1";
                break;
            case 3:
                this.ritardo = "2";
                break;
            case 4:
                this.ritardo = "3";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        this.ritardo = "";
    }


}

