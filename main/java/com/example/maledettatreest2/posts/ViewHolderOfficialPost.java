package com.example.maledettatreest2.posts;

import static com.example.maledettatreest2.MainActivity.getActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.ImageUtil;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.fragment.HomeFragment;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewHolderOfficialPost extends RecyclerView.ViewHolder implements View.OnClickListener{


    public static final String VIEWHOLDER_OFFICILA_POST_LOG = "OFFICIALPOST_LOG";

    public static final int FOTO_PLACEHOLDER = R.drawable.sfondo_profilo2;
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

    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewData;
    private TextView textViewOra;
    private ImageView fotoOfficialPost;
    private HomeFragment homeFragment;

    private OfficialPost officialPost;

    public ViewHolderOfficialPost(@NonNull View itemView, HomeFragment homeFragment) {
        super(itemView);
        this.homeFragment = homeFragment;
        textViewData = itemView.findViewById(R.id.post_data);
        textViewDescription = itemView.findViewById(R.id.post_description);
        textViewTitle = itemView.findViewById(R.id.title_post);
        textViewOra = itemView.findViewById(R.id.post_orario);
        fotoOfficialPost = itemView.findViewById(R.id.foto_official_post);

        Log.d(VIEWHOLDER_OFFICILA_POST_LOG, "in view holder official post");

        fotoOfficialPost.setImageBitmap(FOTO_PLACEHOLDER_BITMAP);

        //imposto il listener sull'elemento della lista
        itemView.setOnClickListener(this);
    }

    public void updateOfficialPost(OfficialPost officialPost) throws Exception {
        Log.d(VIEWHOLDER_OFFICILA_POST_LOG, "in update official posts");
        this.officialPost = officialPost;
        textViewOra.setText(officialPost.getOraString());
        textViewData.setText(officialPost.getDataString());
        textViewTitle.setText(officialPost.getTitle());
        textViewDescription.setText(officialPost.getDirection());

    }

    @Override
    public void onClick(View view) {
        Log.d(VIEWHOLDER_OFFICILA_POST_LOG, "click sul official post");
        homeFragment.onClickViewHolder(view, getAdapterPosition());
        istanzaModel.setOfficialPostSelected(officialPost);
        Log.d(VIEWHOLDER_OFFICILA_POST_LOG, "official post " + officialPost.toString());
    }
}
