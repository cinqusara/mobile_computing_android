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
import com.example.maledettatreest2.fragment.HomeFragment;

public class AdapterOfficialPost extends RecyclerView.Adapter<ViewHolderOfficialPost> {

    private LayoutInflater inflater;
    private HomeFragment homeFragment;


    public AdapterOfficialPost(Context context, HomeFragment fragment) {
        this.inflater = LayoutInflater.from(context);
        this.homeFragment = fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public ViewHolderOfficialPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_official_post, parent, false);
        return new ViewHolderOfficialPost(view, homeFragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolderOfficialPost holder, int position) {
        OfficialPost of = MyModel.getInstance().getOfficialPost(position);
        try {
            holder.updateOfficialPost(of);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemCount() {
        return MyModel.getInstance().getSizeOffPosts();
    }
}
