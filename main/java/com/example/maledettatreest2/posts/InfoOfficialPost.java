package com.example.maledettatreest2.posts;

import static com.example.maledettatreest2.MainActivity.getActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maledettatreest2.ImageUtil;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;

@RequiresApi(api = Build.VERSION_CODES.O)
public class InfoOfficialPost extends AppCompatActivity {
    public static final String INFO_POST_LOG = "OFFICIALPOST_LOG";
    MyModel istanzaModel = MyModel.getInstance();
    private OfficialPost officialPost;

    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewData;
    private TextView textViewOra;
    private ImageView fotoOfficialPost;

    public static final int FOTO_PLACEHOLDER = R.drawable.sfondo_profilo2;
    public static final Bitmap FOTO_PLACEHOLDER_BITMAP = ImageUtil.drawableToBitmap(ContextCompat.getDrawable(getActivity(), FOTO_PLACEHOLDER));
    public static final String FOTO_PLACEHOLDER_STRING = ImageUtil.convertToBase64(FOTO_PLACEHOLDER_BITMAP);


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_official_post);

        officialPost = istanzaModel.getOfficialPostSelected();

        setLayoutPostSelected();
    }

    private void setLayoutPostSelected() {
        textViewData = findViewById(R.id.dataPostSelected);
        textViewDescription = findViewById(R.id.descriptionPostSelected);
        textViewTitle = findViewById(R.id.titlePostSelected);
        textViewOra =findViewById(R.id.oraPostSelected);
        fotoOfficialPost = findViewById(R.id.foto_official_post_selected);

        Log.d(INFO_POST_LOG, "create layout");

        fotoOfficialPost.setImageBitmap(FOTO_PLACEHOLDER_BITMAP);

        textViewOra.setText(officialPost.getOraString());
        textViewData.setText(officialPost.getDataString());
        textViewTitle.setText(officialPost.getTitle());
        textViewDescription.setText(officialPost.getDirection());

    }


}