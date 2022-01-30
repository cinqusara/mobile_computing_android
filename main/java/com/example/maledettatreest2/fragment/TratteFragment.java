package com.example.maledettatreest2.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maledettatreest2.CommunicationController;
import com.example.maledettatreest2.linee.AdapterLinee;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.databinding.FragmentTratteBinding;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TratteFragment extends Fragment {

    private FragmentTratteBinding binding;
    public static final String TRATTE_LOG = "TRATTE_LOG";

    /** istanza Model */
    MyModel istanzaModel = MyModel.getInstance();

    /** dichiarazione CommunicationController */
    CommunicationController cc;

    /** RecyclerView */
    RecyclerView recyclerViewLinee;
    AdapterLinee adapterLinee ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTratteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        /** istanza CommunicationController */
        cc = new CommunicationController(getContext());

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TRATTE_LOG, "onStart");

        //recyclerView Linee
        recyclerViewLinee = getActivity().findViewById(R.id.recyclerViewLinee);
        recyclerViewLinee.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterLinee = new AdapterLinee(this);
        recyclerViewLinee.setAdapter(adapterLinee);
    }

    public void notifyAdapterLinee(){
        adapterLinee.notifyDataSetChanged();
    }

}



