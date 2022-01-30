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

/**
 * è il controller per una singola cella
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewHolderLinee extends RecyclerView.ViewHolder {
    public static final String VIEWHOLDER_LINEE_LOG = "VIEWHOLDER_LINEE_LOG";
    private TextView textViewLinea;
    private TextView textViewDirezione1;
    private TextView textViewDirezione2;
    private Button btnDirezione1;
    private Button btnDirezione2;
    private Button btnHome;

    private TratteFragment fragment;
    private Linea linea;

    /**
     * istanza Model
     */
    MyModel istanza = MyModel.getInstance();

    public ViewHolderLinee(View itemView, TratteFragment fragment) {
        super(itemView);
        this.fragment = fragment;
        textViewLinea = itemView.findViewById(R.id.SingleRowLinea);
        textViewDirezione1 = itemView.findViewById(R.id.SingleRowDirezione1);
        textViewDirezione2 = itemView.findViewById(R.id.SingleRowDirezione2);
        btnDirezione1 = itemView.findViewById(R.id.btnDirezione1);
        btnDirezione2 = itemView.findViewById(R.id.btnDirezione2);
        btnHome = itemView.findViewById(R.id.navigation_home);

        btnDirezione1.setOnClickListener((v) -> {
            Log.d(VIEWHOLDER_LINEE_LOG, "pulsante direzione1 premuto");
            /**salvataggio did nel Model */
            istanza.setDid(linea.getTratta1().getDid());

            MainActivity.getActivity().saveAllInShared();
            MainActivity.getActivity().goToHomePage();

        });
        btnDirezione2.setOnClickListener((v) -> {
            Log.d(VIEWHOLDER_LINEE_LOG, "pulsante direzione2 premuto");
            /**salvataggio did nel Model */
            istanza.setDid(linea.getTratta2().getDid());

            MainActivity.getActivity().saveAllInShared();
            MainActivity.getActivity().goToHomePage();
        });

    }

    /** prende i riferimenti agli elementi nella singola cella*/
    public void updateContent(Linea l) {
        this.linea = l;
        Log.d(VIEWHOLDER_LINEE_LOG, "on updateContent");
        Log.d(VIEWHOLDER_LINEE_LOG, l.toString());
        Log.d(VIEWHOLDER_LINEE_LOG, l.getTratta1().getNome());

        textViewLinea.setText(l.getNomeLinea());
        textViewDirezione1.setText(l.getTratta1().getNome());
        textViewDirezione2.setText(l.getTratta2().getNome());
    }


}
