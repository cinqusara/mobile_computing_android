package com.example.maledettatreest2.linee;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.fragment.TratteFragment;

/**
 * elemento del controller che permette di definire view dinamiche (recycleViee) rispetto ai dati da mostrare
 */

public class AdapterLinee extends RecyclerView.Adapter<ViewHolderLinee> {
    public static final String ADAPTER_LINEE_LOG = "ADAPTER_LINEE_LOG";
    private LayoutInflater mInflater; //oggetto che dal codice statico crea gli oggetti di interfaccia grafica
    private TratteFragment fragment;

    public AdapterLinee(TratteFragment fragment) {
        this.mInflater = LayoutInflater.from(fragment.getContext()); //riferimento a inflater
        this.fragment = fragment;
    }

    /**
     * richiamato quando si crea un nuovo oggetto di view che rappresenta una cella
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public ViewHolderLinee onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.single_row, parent, false);
        return new ViewHolderLinee(view, fragment);
    }

    /**
     * metodo che associa il model alla view
     * viene richiamato quando ad una cella vengono associati i suoi dati
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolderLinee holder, int position) {
        Linea l = MyModel.getInstance().getLinea(position);
        holder.updateContent(l);
        Log.d(ADAPTER_LINEE_LOG, l.toString());
    }

    /**
     * metodo per sapere il numero di oggetti da mostrare
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemCount() {
        return MyModel.getInstance().getSizeLinee();
    }


}