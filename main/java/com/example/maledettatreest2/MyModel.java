package com.example.maledettatreest2;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.maledettatreest2.db_foto.FotoProfiloUtente;
import com.example.maledettatreest2.linee.Linea;
import com.example.maledettatreest2.posts.OfficialPost;
import com.example.maledettatreest2.posts.Post;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MyModel {

    private static MyModel istanzaModel = new MyModel();
    public static final String MODEL_LOG = "MODEL_LOG";

    @RequiresApi(api = Build.VERSION_CODES.O)
    private MyModel() {
    }

    /**
     * singleton
     */
    public static synchronized MyModel getInstance() {
        return istanzaModel;
    }

    private String sid; //numero sessione
    private String nome;
    private String foto;
    private String did;

    private ArrayList<FotoProfiloUtente> fotoProfiloUtenti = new ArrayList<FotoProfiloUtente>();
    private ArrayList<Linea> linee = new ArrayList<Linea>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<OfficialPost> officialPosts = new ArrayList<>();
    private ArrayList<Stazione> stazioni = new ArrayList<Stazione>();
    private OfficialPost officialPostSelected = new OfficialPost(null, null, null);


    /**
     * metodi getter e setter
     */

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
        return fotoProfiloUtenti;
    }

    public void setFotoProfiloUtenti(ArrayList<FotoProfiloUtente> fotoProfiloUtenti) {
        this.fotoProfiloUtenti = fotoProfiloUtenti;
    }

    public ArrayList<Linea> getLinee() {
        return linee;
    }

    public void setLinee(ArrayList<Linea> linee) {
        this.linee = linee;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public OfficialPost getOfficialPostSelected() {
        return officialPostSelected;
    }

    public void setOfficialPostSelected(OfficialPost officialPostSelected) {
        this.officialPostSelected = officialPostSelected;
    }

    /**
     * metodi per le stazioni
     */

    public ArrayList<Stazione> getStazioni() {
        return stazioni;
    }

    public void setStazioni(ArrayList<Stazione> stazioni) {
        this.stazioni = stazioni;
    }

    public void addStazione(Stazione s) {
        stazioni.add(s);
    }

    public void azzeraStazioni() {
        this.stazioni.clear();
    }

    /**
     * metodi per le linee
     */

    public int getSizeLinee() {
        return linee.size();
    }

    public Linea getLinea(int index) {
        return linee.get(index);
    }

    public void addLine(Linea l) {
        linee.add(l);
    }

    public String getLastLine() {
        for (Linea l : linee) {
            if (l.getTratta1().getDid().equals(this.did) || l.getTratta2().getDid().equals(this.did)) {
                return l.getTratta1().getNome() + " - " + l.getTratta2().getNome();
            }
        }
        Log.e(MODEL_LOG, "nessuna linea trovata");
        return "errore";
    }

    public String getLastDirection() {
        for (Linea l : linee) {
            if (l.getTratta1().getDid().equals(this.did))
                return l.getTratta1().getNome();
            else if (l.getTratta2().getDid().equals(this.did))
                return l.getTratta2().getNome();
        }
        Log.e(MODEL_LOG, "nessuna direzione trovata");
        return "errore";
    }

    public void changeDirection() {
        Log.d(MODEL_LOG, "inverti direzione");
        for (Linea l : linee) {
            if (l.getTratta1().getDid().equals(this.did)) {
                setDid(l.getTratta2().getDid());
            } else if (l.getTratta2().getDid().equals(this.did)) {
                setDid(l.getTratta1().getDid());
            }
        }
    }

    public void clearLines() {
        Log.d(MODEL_LOG, "azzera linee ");
        this.linee.clear();
    }

    /**
     * metodi per i post
     */

    public Post getPost(int index) {
        return posts.get(index);
    }

    public OfficialPost getOfficialPost(int index) {
        return officialPosts.get(index);
    }

    public ArrayList<OfficialPost> getAllOfficialPosts() {
        return officialPosts;
    }

    public int getSizePosts() {
        return posts.size();
    }

    public int getSizeOffPosts() {
        return officialPosts.size();
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    public void addOfficialPost(OfficialPost officialPost){
        officialPosts.add(officialPost);
    }

    public void clearPosts() {
        Log.d(MODEL_LOG, "azzera posts");
        this.posts.clear();
    }

    public void clearOffPosts() {
        Log.d(MODEL_LOG, "azzera posts");
        this.officialPosts.clear();
    }

    /**
     * metodi per le foto profilo
     */
    public void addFotoProfiloUtente(FotoProfiloUtente fpu) {
        fotoProfiloUtenti.add(fpu);
    }

    public int getVersionFromPost(Post p) {
        Log.d(MODEL_LOG, "get versione dal post");
        for (FotoProfiloUtente fpu : fotoProfiloUtenti) {
            if (fpu.getUid().equals(p.getUidAutore()))
                return fpu.getVersione();
        }
        return 0;
    }

    public FotoProfiloUtente getPictureFromPost(Post p) throws Exception {
        Log.d(MODEL_LOG, "get foto dal post");
        for (FotoProfiloUtente fpu : fotoProfiloUtenti) {
            if (fpu.getUid().equals(p.getUidAutore()))
                return fpu;
        }
        throw new Exception("Exception message");
    }

    public int getVersioneFotoProfiloUtenteFromUid(String uid) throws Exception {
        Log.d(MODEL_LOG, "get versione foto dall'uid");
        for (FotoProfiloUtente fp : fotoProfiloUtenti) {
            if (fp.getUid().equals(uid))
                return fp.getVersione();
        }
        throw new IllegalArgumentException("Versione foto non trovata -> getVersioneFotoProfiloUtenteFromUid");
    }

    public void insertPictureInModel(FotoProfiloUtente fpu) {
        Log.d(MODEL_LOG, "inserimento foto nel model");
        for (FotoProfiloUtente fotoObj : fotoProfiloUtenti) {
            if (fpu.getUid().equals(fotoObj)) {
                fotoProfiloUtenti.remove(fotoObj); //rimuove obj fpu vecchio
            }
        }
        fotoProfiloUtenti.add(fpu); //aggiunge obj fpu nuovo
    }

    /**
     * metodi per follow/unfollow
     */
    public void setFollowPost(Post p) {
        for (Post post : posts) {
            if (post.getUidAutore().equals(p.getUidAutore())) {
                post.setFollowAutore(true);
            }
        }
    }

    public void setUnfollowPost(Post p) {
        for (Post post : posts) {
            if (post.getUidAutore().equals(p.getUidAutore())) {
                post.setFollowAutore(false);
            }
        }
    }

    /**
     * to string
     */
    @Override
    public String toString() {
        return "MyModel{" +
                "linee=" + linee +
                '}';
    }

}
