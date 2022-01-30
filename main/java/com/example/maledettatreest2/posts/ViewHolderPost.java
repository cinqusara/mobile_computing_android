package com.example.maledettatreest2.posts;

import static com.example.maledettatreest2.MainActivity.getActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.ImageUtil;
import com.example.maledettatreest2.MainActivity;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.db_foto.FotoProfiloUtente;

import org.json.JSONException;

/**
 * è il controller per una singola cella
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewHolderPost extends RecyclerView.ViewHolder {

    public static final String VIEWHOLDER_POST_LOG = "POST_LOG";

    public static final int FOTO_PLACEHOLDER = R.drawable.default_user_profile_picture_hvoncb;
    public static final Bitmap FOTO_PLACEHOLDER_BITMAP = ImageUtil.drawableToBitmap(ContextCompat.getDrawable(getActivity(), FOTO_PLACEHOLDER));
    public static final String FOTO_PLACEHOLDER_STRING = ImageUtil.convertToBase64(FOTO_PLACEHOLDER_BITMAP);

    /**
     * istanza Model
     */
    MyModel istanzaModel = MyModel.getInstance();

    /**
     * CommunicationController
     */
    CommunicationController cc = new CommunicationController(getActivity());

    private TextView textViewAutore;
    private TextView textViewStato;
    private TextView textViewRitardo;
    private TextView textViewCommento;
    private TextView textViewData;
    private TextView textViewOra;
    private Button btnFollow;
    private ImageView fotoProfiloAutorePost;

    private Post p;

    public ViewHolderPost(@NonNull View itemView) {
        super(itemView);
        textViewAutore = itemView.findViewById(R.id.post_autore);
        textViewStato = itemView.findViewById(R.id.post_stato);
        textViewData = itemView.findViewById(R.id.post_data);
        textViewRitardo = itemView.findViewById(R.id.post_ritardo);
        textViewCommento = itemView.findViewById(R.id.post_commento);
        textViewOra = itemView.findViewById(R.id.post_ora);
        btnFollow = itemView.findViewById(R.id.btnFollow);
        fotoProfiloAutorePost = itemView.findViewById(R.id.fotoProfiloPost);

        //settiamo a tutte le foto l'immagine di profilo di base nel caso in cui ci dovessero essere dei problemi con l'immagine
        fotoProfiloAutorePost.setImageBitmap(FOTO_PLACEHOLDER_BITMAP);

        btnFollow.setOnClickListener((v) -> {
            Log.d(VIEWHOLDER_POST_LOG, "tap su btnFollow");
            followUnfollowAuthorPost(this.p);
        });
    }

    /**
     * prende i riferimenti agli elementi nella singola cella
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updatePost(Post p) throws Exception {
        this.p = p;
        String nomeTrimmato = p.getNomeAutore().replace("\n", " ");
        textViewAutore.setText(nomeTrimmato);
        String commentoTrimmato = p.getCommento().replace("\n", " ");
        textViewCommento.setText(commentoTrimmato);
        textViewData.setText(p.getDataString());
        textViewOra.setText(p.getOraString());

        setStato();
        setRitardo();
        setBtnFollow();

        if (p.getVersioneFoto() != 0) {// fotoProfiloUtente presente nel post
            int versioneFotoPost = p.getVersioneFoto();
            int versioneFotoModel = istanzaModel.getVersionFromPost(p);
            String uidFotoDaScaricare = p.getUidAutore();
            if (versioneFotoPost == versioneFotoModel) { //versione nel model aggiornata -> visualizza
                Log.d(VIEWHOLDER_POST_LOG, "versione model aggiornata");
                String foto = istanzaModel.getPictureFromPost(p).getFoto();
                showUserPicture(ImageUtil.convertToBitmap(foto));
            } else {//versione NON aggiornata o NON presente
                Log.d(VIEWHOLDER_POST_LOG, "versione model NON aggiornata o NON presente");
                getUserPictureFromServer(uidFotoDaScaricare);
            }
        } else {// fotoProfiloUtente NON presente nel post
            fotoProfiloAutorePost.setImageBitmap(FOTO_PLACEHOLDER_BITMAP);
        }
    }

    /**
     * metodi per la visualizzazione dei dati del post
     */
    private void setStato() {
        switch (p.getStato()) {
            case "0":
                textViewStato.setText("Ideale");
                break;
            case "1":
                textViewStato.setText("Accettable");
                break;
            case "2":
                textViewStato.setText("Gravi problemi per i passeggeri");
                break;
            default:
                textViewStato.setText("Nessuna\ninformazione");
        }
    }

    private void setRitardo() {
        switch (p.getRitardo()) {
            case "0":
                textViewRitardo.setText("In orario");
                break;
            case "1":
                textViewRitardo.setText("Ritardo di pochi minuti");
                break;
            case "2":
                textViewRitardo.setText("Ritardo oltre i 15 minuti");
                break;
            case "3":
                textViewRitardo.setText("Treni soppressi");
                break;
            default:
                textViewRitardo.setText("Nessuna\ninformazione");
        }
    }

    public void setBtnFollow() {
        Drawable unfollow_icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_unfollow);
        Drawable follow_icon = ContextCompat.getDrawable(getActivity(), R.drawable.ic_check2);
        if (this.p.getFollowAutore()) {
            btnFollow.setCompoundDrawablesWithIntrinsicBounds(follow_icon, follow_icon, follow_icon, follow_icon);
            btnFollow.setBackgroundColor(Color.WHITE);
        } else {
            btnFollow.setCompoundDrawablesWithIntrinsicBounds(unfollow_icon, unfollow_icon, unfollow_icon, unfollow_icon);
            btnFollow.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.primary_green));
        }
    }

    public void showUserPicture(Bitmap foto) {
        if (ImageUtil.isQuadrata(foto)) {
            Log.d("foto89", "SI quadrata");
            fotoProfiloAutorePost.setImageBitmap(foto);
        } else {
            Log.d("foto89", "NO quadrata");
            fotoProfiloAutorePost.setImageBitmap(FOTO_PLACEHOLDER_BITMAP);
        }
    }

    /**
     * chiamata di rete per prelevare le foto del post
     */
    private void getUserPictureFromServer(String uidFotoDaScaricare) {
        cc.getUserPicture(  //chiamata rete per scaricare foto
                istanzaModel.getSid(),
                uidFotoDaScaricare,
                response -> {
                    String uid = null;
                    int versione = -1;
                    String fotoResponse = null;

                    try {
                        uid = response.getString("uid");
                        versione = response.getInt("pversion");
                        fotoResponse = response.getString("picture");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    FotoProfiloUtente fpu = new FotoProfiloUtente(uid, versione, fotoResponse); //creo oggetto FPU

                    try {
                        showUserPicture(ImageUtil.convertToBitmap(fpu.getFoto()));  // visualizzo foto nuova
                    } catch (Exception e) {
                        Log.d(VIEWHOLDER_POST_LOG, "formato foto non corretto --> sostituzione con foto placeholder");
                        fotoProfiloAutorePost.setImageBitmap(FOTO_PLACEHOLDER_BITMAP);
                        fpu.setFoto(FOTO_PLACEHOLDER_STRING);
                        e.printStackTrace();
                    }
                    istanzaModel.insertPictureInModel(fpu);
                    MainActivity.getActivity().insertPictureInDB(fpu);
                },
                error -> Log.e(VIEWHOLDER_POST_LOG, "errore download foto profilo  " + error.getLocalizedMessage())
        );
    }


    /**
     * metodi per follow e unfollow autore post
     */

    public void followUnfollowAuthorPost(Post p) {
        Log.d(VIEWHOLDER_POST_LOG, istanzaModel.getPosts().toString());
        if (p.getFollowAutore()) {
            unfollowAuthor();
        } else {
            followAuthor();
        }
    }

    //da non seguito a seguito
    private void followAuthor() {
        cc.follow(
                istanzaModel.getSid(),
                p.getUidAutore(),
                response -> {
                    istanzaModel.setFollowPost(p);
                    MainActivity.getActivity().notifyAdapterPostFromMainActivity();
                },
                error -> Log.e(VIEWHOLDER_POST_LOG, "errore" + error.toString())
        );
    }

    //da seguito a non seguito
    private void unfollowAuthor() {
        cc.unfollow(
                istanzaModel.getSid(),
                p.getUidAutore(),
                response -> {
                    istanzaModel.setUnfollowPost(p);
                    MainActivity.getActivity().notifyAdapterPostFromMainActivity();
                    Log.d(VIEWHOLDER_POST_LOG, "ok unfollow" + response.toString());
                },
                error -> Log.e(VIEWHOLDER_POST_LOG, "errore" + error.toString())
        );
    }

}


