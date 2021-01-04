package com.example.tesloginnavigationdrawer.ui.riwayat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesloginnavigationdrawer.API.ApiRequestData;
import com.example.tesloginnavigationdrawer.API.RetroServer;
import com.example.tesloginnavigationdrawer.Model.ResultRiwayatModel;
import com.example.tesloginnavigationdrawer.Model.ValueRiwayatModel;
import com.example.tesloginnavigationdrawer.R;
import com.example.tesloginnavigationdrawer.RecyclerViewAdapterRiwayat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatFragment extends Fragment {

    private RiwayatViewModel riwayatViewModel;

    List<ResultRiwayatModel> results = new ArrayList<>();

    RecyclerView recyclerView;
    ProgressBar progressBar;
    FloatingActionButton btnTambah;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        riwayatViewModel =
                ViewModelProviders.of(this).get(RiwayatViewModel.class);
        View root = inflater.inflate(R.layout.fragment_riwayat, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);

//////////////////////////////////////////////////////////////////////////
        List<ResultRiwayatModel> results = new ArrayList<>();
        RecyclerViewAdapterRiwayat viewAdapter;

        recyclerView = root.findViewById(R.id.recyclerView_riwayat);
        progressBar = root.findViewById(R.id.progress_bar);

        viewAdapter = new RecyclerViewAdapterRiwayat(getContext(), results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);


        loadDataBalita();




        riwayatViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataBalita();
//        Toast.makeText(getContext(), "BERHASIL RESUME FRAGMENT", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        loadDataBalita();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        loadDataBalita();
    }



    public void loadDataBalita(){
        ApiRequestData api = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<ValueRiwayatModel> call = api.ardViewRiwayat();
        call.enqueue(new Callback<ValueRiwayatModel>() {
            @Override
            public void onResponse(Call<ValueRiwayatModel> call, Response<ValueRiwayatModel> response) {
                String value = response.body().getValue();

                progressBar.setVisibility(View.GONE);
                if (value.equals("1")) {

                    results = response.body().getResult(); // menyimpan kedalam array results
                    RecyclerViewAdapterRiwayat viewAdapter;
                    viewAdapter = new RecyclerViewAdapterRiwayat(getContext(), results);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call<ValueRiwayatModel> call, Throwable t) {
                try {
                    Toast.makeText(getContext(), "Gagal Memuat Riwayat Testing" , Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}