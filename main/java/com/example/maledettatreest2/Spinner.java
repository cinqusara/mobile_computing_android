package com.example.maledettatreest2;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;

public class Spinner {

    /**
     * riferimento a spinner loading
     */
    private static ProgressBar spinner;




    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void  mostraSpinner(){
        //spinner = (ProgressBar)MainActivity.getActivity().findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void  nascondiSpinner(){
        //spinner = (ProgressBar)MainActivity.getActivity().findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
    }




}
