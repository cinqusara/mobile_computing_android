package com.example.maledettatreest2;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.maledettatreest2.db_foto.FotoProfiloUtente;
import com.example.maledettatreest2.linee.Linea;
import com.example.maledettatreest2.posts.Post;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyModel{

    private static MyModel istanzaUnica = new MyModel();
    public static final String MTE_LOG = "MTE_LOG";

    @RequiresApi(api = Build.VERSION_CODES.O)
    private MyModel() {

    }

    public static synchronized MyModel getInstance(){
        return istanzaUnica;
    }
    ////////////////////////////////

    private String sid;
    private String nome;
    private String foto;
    private String did;

    private ArrayList<FotoProfiloUtente> fotoProfiloUtente = new ArrayList<FotoProfiloUtente>();
    private ArrayList<Linea> linee = new ArrayList<Linea>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<Stazione> stazioni = new ArrayList<Stazione>();



    //GET e SET
    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public ArrayList<FotoProfiloUtente> getFotoProfiloUtenti() {
        return fotoProfiloUtente;
    }

    public void setFotoProfiloUtenti(ArrayList<FotoProfiloUtente> fotoProfiloUtente) {
        this.fotoProfiloUtente = fotoProfiloUtente;
    }

    public ArrayList<Linea> getLinee() {
        return linee;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Stazione> getStazioni() {
        return stazioni;
    }

    public void setStazioni(ArrayList<Stazione> stazioni) {
        this.stazioni = stazioni;
    }

    public void addStazione(Stazione s){
        stazioni.add(s);
    }

    public void azzeraStazioni(){
        this.stazioni.clear();
    }

    public void setLinee(ArrayList<Linea> linee) {
        this.linee = linee;
    }
    /////////////////////////////

    public int getSizeLinee(){
        return linee.size();
    }


    public int getSizePosts() {
        return posts.size();
    }

    public Linea getLinea(int index) {
        return linee.get(index);
    }

    public Post getPost(int index) {
        return posts.get(index);
    }


    public void addLinea(Linea l){
        linee.add(l);
    }

    public void addPost(Post post){
        posts.add(post);
    }

    public String getUltimaLinea(){
        for (Linea l: linee) {

            if (l.getTratta1().getDid().equals(this.did) || l.getTratta2().getDid().equals(this.did)){
                return l.getTratta1().getNome().toString() + " - " + l.getTratta2().getNome().toString();
            }
        }
        Log.e(MTE_LOG, "nessuna linea trovata");
        return "errore";
    }

    public String getUltimaDirezione(){
        for (Linea l: linee) {
            if (l.getTratta1().getDid().equals(this.did))
                return l.getTratta1().getNome().toString();
            else if(l.getTratta2().getDid().equals(this.did))
                return l.getTratta2().getNome().toString();
        }
        Log.e(MTE_LOG, "nessuna direzione trovata");
        return "errore";
    }

    public void invertiDirezione(){
        Log.d(MTE_LOG, "in model --> inverti direzione ");
        for (Linea l: linee) {
            if (l.getTratta1().getDid().equals(this.did)){
                setDid(l.getTratta2().getDid());
            }else if (l.getTratta2().getDid().equals(this.did)) {
                setDid(l.getTratta1().getDid());
            }
        }

    }

    public void azzeraLinee() {
        this.linee.clear();
    }

    public void azzeraPosts() {
        this.posts.clear();
    }







    @Override
    public String toString() {
        return "MyModel{" +
                "linee=" + linee +
                '}';
    }




    ///////////



}
