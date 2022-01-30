package com.example.maledettatreest2.posts;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class Post {

    private String nomeAutore;
    private String uidAutore;
    private LocalDateTime dataOra;
    private int versioneFoto = 0;
    private Boolean followAutore;

    //OPZIONALI
    private String ritardo;
    private String stato;
    private String commento;

    public Post(String nomeAutore, String uidAutore, LocalDateTime dataOra, int versioneFoto, Boolean followAutore, String ritardo, String stato, String commento) {
        this.nomeAutore = nomeAutore;
        this.uidAutore = uidAutore;
        this.dataOra = dataOra;
        this.versioneFoto = versioneFoto;
        this.followAutore = followAutore;
        this.ritardo = ritardo;
        this.stato = stato;
        this.commento = commento;
    }

    public String getNomeAutore() {
        return nomeAutore;
    }

    public void setNomeAutore(String nomeAutore) {
        this.nomeAutore = nomeAutore;
    }

    public String getUidAutore() {
        return uidAutore;
    }

    public void setUidAutore(String uidAutore) {
        this.uidAutore = uidAutore;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public int getVersioneFoto() {
        return versioneFoto;
    }

    public void setVersioneFoto(int versioneFoto) {
        this.versioneFoto = versioneFoto;
    }

    public Boolean getFollowAutore() {
        return followAutore;
    }

    public void setFollowAutore(Boolean followAutore) {
        this.followAutore = followAutore;
    }

    public String getRitardo() {
        return ritardo;
    }

    public void setRitardo(String ritardo) {
        this.ritardo = ritardo;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getCommento() {
        return commento;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDataString(){
        return String.valueOf(this.dataOra.getDayOfMonth() + "/" +  this.dataOra.getMonth().getValue() + "/" +  this.dataOra.getYear());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getOraString(){
        String ore = String.format("%02d",this.dataOra.getHour());
        String minuti = String.format("%02d",this.dataOra.getMinute());
        return (ore + ":" + minuti);
    }

    @Override
    public String toString() {
        return "Post{" +
                "nomeAutore='" + nomeAutore + '\'' +
                ", uidAutore='" + uidAutore + '\'' +
                ", dataOra=" + dataOra +
                ", versioneFoto='" + versioneFoto + '\'' +
                ", followAutore=" + followAutore +
                ", ritardo='" + ritardo + '\'' +
                ", stato='" + stato + '\'' +
                ", commento='" + commento + '\'' +
                '}';
    }
}


