package com.example.maledettatreest2.db_foto;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
@Entity
public class FotoProfiloUtente {
    public static final String JSON_UID = "uid";
    public static final String JSON_VERSIONE = "pversion";
    public static final String JSON_FOTO = "picture";

    @PrimaryKey @NotNull
    private String uid;

    @ColumnInfo(name = "versione")
    private int versione;

    @ColumnInfo(name = "foto")
    private String foto;

    public FotoProfiloUtente(String uid, int versione, String foto) {
        this.uid = uid;
        this.versione = versione;
        this.foto = foto;
    }

    public FotoProfiloUtente(JSONObject oggetto){
        try {
            this.uid=oggetto.getString(JSON_UID);
            this.versione=Integer.parseInt(oggetto.getString(JSON_VERSIONE));
            this.foto=oggetto.getString(JSON_FOTO);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUid() {
        return uid;
    }

    public int getVersione() {
        return versione;
    }

    public void setVersione(int versione) {
        this.versione = versione;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "FotoProfiloUtente{" +
                "uid='" + uid + '\'' +
                ", versione=" + versione +
                ", foto='" + foto + '\'' +
                '}';
    }
}
