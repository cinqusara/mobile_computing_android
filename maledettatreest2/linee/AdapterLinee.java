package com.example.maledettatreest2.linee;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.fragment.TratteFragment;

public class AdapterLinee extends RecyclerView.Adapter<ViewHolderLinee> {
    public static final String MTE_LOG = "MTE_LOG";
    private LayoutInflater mInflater;
    private TratteFragment fragment;


    public AdapterLinee(TratteFragment fragment) {
        this.mInflater = LayoutInflater.from(fragment.getContext());
        this.fragment = fragment;
    }

    @Override
    public ViewHolderLinee onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_row, parent, false);
        return new ViewHolderLinee(view, fragment);
    }
    @Override
    public int getItemCount() {
        return MyModel.getInstance().getSizeLinee();
    }

    @Override
    public void onBindViewHolder(ViewHolderLinee holder, int position) {
        Linea l = MyModel.getInstance().getLinea(position);
        holder.updateContent(l);
        Log.d(MTE_LOG, l.toString());
    }



}