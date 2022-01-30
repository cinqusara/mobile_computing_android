package com.example.maledettatreest2.posts;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.posts.Post;
import com.example.maledettatreest2.posts.ViewHolderPost;

/**
 * elemento del controller che permette di definire view dinamiche (recycleViee) rispetto ai dati da mostrare
 */

public class AdapterPost extends RecyclerView.Adapter<ViewHolderPost> {

    private LayoutInflater inflater;

    public AdapterPost(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    /**
     * richiamato quando si crea un nuovo oggetto di view che rappresenta una cella
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_post, parent, false);
        return new ViewHolderPost(view);
    }

    /**
     * metodo che associa il model alla view
     * viene richiamato quando ad una cella vengono associati i suoi dati
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost holder, int position) {
        Post p = MyModel.getInstance().getPost(position);
        try {
            holder.updatePost(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo per sapere il numero di oggetti da mostrare
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemCount() {
        return MyModel.getInstance().getSizePosts();
    }
}


