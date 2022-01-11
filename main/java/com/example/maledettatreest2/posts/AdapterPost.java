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

public class AdapterPost extends RecyclerView.Adapter<ViewHolderPost> {

    private LayoutInflater inflater;

    public AdapterPost(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_post2, parent, false);
        return new ViewHolderPost(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderPost holder, int position) {
        Post p = MyModel.getInstance().getPost(position);
        holder.updatePost(p);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemCount() {
        return MyModel.getInstance().getSizePosts();
    }
}


