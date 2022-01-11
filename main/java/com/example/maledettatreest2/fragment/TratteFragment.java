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

import com.example.maledettatreest2.linee.AdapterLinee;
import com.example.maledettatreest2.MyModel;
import com.example.maledettatreest2.R;
import com.example.maledettatreest2.databinding.FragmentTratteBinding;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TratteFragment extends Fragment {

    private FragmentTratteBinding binding;
    MyModel istanza = MyModel.getInstance();
    public static final String MTE_LOG = "MTE_LOG";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTratteBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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
        Log.d(MTE_LOG, "onStart");
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AdapterLinee adapter = new AdapterLinee(this);
        recyclerView.setAdapter(adapter);


    }

    //public void onClickSelezione(View v, )
}



