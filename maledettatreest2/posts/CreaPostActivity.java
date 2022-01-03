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

import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.MainActivity;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;

import org.json.JSONException;
import org.json.JSONObject;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CreaPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String[] items = new String[]{"Seleziona", "In orario", "Ritardo di pochi minuti", "Ritardo più di 15 minuti", "Treni soppressi"};
    private Spinner tendinaRitardo;

    private String ritardo = "";
    private String stato = "";
    private TextView commento;
    private JSONObject campiPost = new JSONObject();

    private static final String MTE_LOG = "MTE_LOG" ;
    /** istanza Model */
    MyModel istanza = MyModel.getInstance();

    /** dichiarazione CommunicationController */
    CommunicationController cc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creapost);

        /** inizializzazione CommunicationController */
        cc = new CommunicationController(this);

        Button btnPubblicaPost = findViewById(R.id.btnPubblicaPost);
        btnPubblicaPost.setOnClickListener((v) ->{
            try {
                if(verificaMinimoUnCampoECreaOggetto()){
                    Log.d(MTE_LOG,"oggetto  " + campiPost.toString());
                    creaPost();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        tendinaRitardo = findViewById(R.id.spinnerRitardo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tendinaRitardo.setAdapter(adapter);
        tendinaRitardo.setOnItemSelectedListener(this);

        commento = findViewById(R.id.textCommento);



    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioProblemi:
                if (checked)
                    this.stato="2";
                    break;
            case R.id.radioAccettabile:
                if (checked)
                    this.stato="1";
                    break;
            case R.id.radioIdeale:
                if (checked)
                    this.stato="0";
                    break;
            default:
                this.stato="";
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                this.ritardo= "";
                break;
            case 1:
                this.ritardo= "0";
                break;
            case 2:
                this.ritardo= "1";
                break;
            case 3:
                this.ritardo= "2";
                break;
            case 4:
                this.ritardo= "3";
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        this.ritardo= "";
    }


    public boolean verificaMinimoUnCampoECreaOggetto() throws JSONException {

        if (this.stato!="" || this.ritardo!="" || !(this.commento.getText().toString().equals(""))){
            if (this.stato!=""){
                this.campiPost.put("status", this.stato);
            }
            if (this.ritardo!=""){
                this.campiPost.put("delay", this.ritardo);
            }
            if (!(this.commento.getText().toString().equals(""))){
                this.campiPost.put("comment", this.commento.getText().toString());
            }
            return true;
        }
        Log.d(MTE_LOG,"if NON entrato");
        return false;

    }


    public void creaPost() throws JSONException {
        cc.addPost(
                istanza.getSid(),
                istanza.getDid(),
                this.campiPost,
                response-> {
                    Log.d(MTE_LOG,"ok " + response.toString());
                },
                error -> Log.e(MTE_LOG, "errore" + error.toString())
        );
    }

}

