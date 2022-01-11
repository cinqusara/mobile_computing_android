package com.example.maledettatreest2.linee;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest2.MainActivity;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.fragment.TratteFragment;


@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewHolderLinee extends RecyclerView.ViewHolder {
    public static final String MTE_LOG = "MTE_LOG";
    private TextView textViewLinea;
    private TextView textViewDirezione1;
    private TextView textViewDirezione2;
    private Button btnDirezione1;
    private Button btnDirezione2;
    private TratteFragment fragment;
    private Linea linea;


    /** istanza Model */
    MyModel istanza = MyModel.getInstance();


    public ViewHolderLinee(View itemView, TratteFragment fragment) {
        super(itemView);
        this.fragment = fragment;
        textViewLinea = itemView.findViewById(R.id.SingleRowLinea);
        textViewDirezione1 = itemView.findViewById(R.id.SingleRowDirezione1);
        textViewDirezione2 = itemView.findViewById(R.id.SingleRowDirezione2);
        btnDirezione1 = itemView.findViewById(R.id.btnDirezione1);
        btnDirezione2 = itemView.findViewById(R.id.btnDirezione2);

        btnDirezione1.setOnClickListener((v) ->{
            Log.d(MTE_LOG, "pulsante direzione1 premuto");
            /**salvataggio did nel Model */
            istanza.setDid(linea.getTratta1().getDid());
            ///**salvataggio did Shared Preferences */
            //MainActivity.getInstance().salvaUltimoDid(linea.getTratta1().getDid());

            MainActivity.getActivity().passaAHome();

        });
        btnDirezione2.setOnClickListener((v) ->{
            Log.d(MTE_LOG, "pulsante direzione2 premuto");
            /**salvataggio did nel Model */
            istanza.setDid(linea.getTratta2().getDid());
            ///**salvataggio did Shared Preferences */
            //MainActivity.getInstance().salvaUltimoDid(linea.getTratta2().getDid());

            MainActivity.getActivity().passaAHome();
        });

        Log.d(MTE_LOG, "pulsante direzione2 premuto");
    }

    public void updateContent(Linea l) {
        this.linea= l;
        Log.d(MTE_LOG, "on updateContent");
        Log.d(MTE_LOG, l.toString());
        Log.d(MTE_LOG, l.getTratta1().getNome());

        textViewLinea.setText(l.getNomeLinea());
        textViewDirezione1.setText(l.getTratta1().getNome());
        textViewDirezione2.setText(l.getTratta2().getNome());
    }


}
