package com.example.maledettatreest2.posts;

import android.os.Build;
import android.util.Log;
import android.view.OnReceiveContentListener;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.MainActivity;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.posts.Post;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewHolderPost extends RecyclerView.ViewHolder{

    /** istanza Model */
    MyModel istanza = MyModel.getInstance();

    /** CommunicationController */
    CommunicationController cc = new CommunicationController(MainActivity.getActivity());

    public static final String MTE_LOG = "MTE_LOG";

    private TextView textViewAutore;
    private TextView textViewStato;
    private TextView textViewRitardo;
    private TextView textViewCommento;
    private TextView textViewData;
    private TextView textViewOra;
    private Button btnFollow;

    private Post p;

    private OnReceiveContentListener onReceiveContentListener;

    public ViewHolderPost(@NonNull View itemView) {
        super(itemView);
        textViewAutore = itemView.findViewById(R.id.post_autore);
        textViewStato = itemView.findViewById(R.id.post_stato);
        textViewData = itemView.findViewById(R.id.post_data);
        textViewRitardo = itemView.findViewById(R.id.post_ritardo);
        textViewCommento = itemView.findViewById(R.id.post_commento);
        textViewOra = itemView.findViewById(R.id.post_ora);
        btnFollow = itemView.findViewById(R.id.btnFollow);

        btnFollow.setOnClickListener((v) -> {
            Log.d(MTE_LOG, "tap su btnFollow");
            followUnfollow_AutorePost(this.p);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updatePost(Post p){
        this.p=p;
        textViewAutore.setText(p.getNomeAutore());
        textViewStato.setText(p.getStato());
        textViewRitardo.setText(p.getRitardo());
        textViewCommento.setText(p.getCommento());
        textViewData.setText(p.getDataString());
        textViewOra.setText(p.getOraString());
        setBtnFollow();
    }

    public void setBtnFollow(){
        if (this.p.getFollowAutore())
            btnFollow.setText("unfollow");
        else
            btnFollow.setText("follow");
    }

    public void followUnfollow_AutorePost(Post p){

        if(p.getFollowAutore()){
            //da seguito a non seguito
            cc.unfollow(
                    istanza.getSid(),
                    p.getUidAutore(),
                    response-> {
                        istanza.getPost(getAdapterPosition()).setFollowAutore(false);
                        btnFollow.setText("follow");//TODO con notify
                        Log.d(MTE_LOG,"ok unfollow" + response.toString());
                    },
                    error -> Log.e(MTE_LOG, "errore" + error.toString())
            );
        }else{
            //da non seguito a seguito
            cc.follow(
                    istanza.getSid(),
                    p.getUidAutore(),
                    response-> {
                        istanza.getPost(getAdapterPosition()).setFollowAutore(true);
                        btnFollow.setText("unfollow"); //TODO con notify
                        Log.d(MTE_LOG,"ok follow" + response.toString());
                    },
                    error -> Log.e(MTE_LOG, "errore" + error.toString())
            );
        }

    }



}


