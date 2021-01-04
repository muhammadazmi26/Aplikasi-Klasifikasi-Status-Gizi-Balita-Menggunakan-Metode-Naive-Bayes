package com.example.tesloginnavigationdrawer.ui.training;

import android.content.Context;
import android.content.Intent;
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
import com.example.tesloginnavigationdrawer.CreateActivity;
import com.example.tesloginnavigationdrawer.Model.ResultTrainingModel;
import com.example.tesloginnavigationdrawer.Model.ValueTrainingModel;
import com.example.tesloginnavigationdrawer.R;
import com.example.tesloginnavigationdrawer.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingFragment extends Fragment {

    private TrainingViewModel trainingViewModel;
    List<ResultTrainingModel> results = new ArrayList<>();

    RecyclerView recyclerView;
    ProgressBar progressBar;
    FloatingActionButton btnTambah;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trainingViewModel =
                ViewModelProviders.of(this).get(TrainingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_training, container, false);

        final TextView textView = root.findViewById(R.id.text_gallery);
        btnTambah = root.findViewById(R.id.fab_tambah_data);
//////////////////////////////////////////////////////////////////////////
        List<ResultTrainingModel> results = new ArrayList<>();
        RecyclerViewAdapter viewAdapter;

        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progress_bar);

        viewAdapter = new RecyclerViewAdapter(getContext(), results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(viewAdapter);


        loadDataBalita();


        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), CreateActivity.class);
                startActivity(i);
            }
        });


////////////////////////////////////////////////////////////////////////////

        trainingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;


    }

    public void picu(){
        loadDataBalita();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataBalita();
//        Toast.makeText(getContext(), "memuat data berhasol", Toast.LENGTH_SHORT).show();
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
        Call<ValueTrainingModel> call = api.ardViewDataTraining();
        call.enqueue(new Callback<ValueTrainingModel>() {
            @Override
            public void onResponse(Call<ValueTrainingModel> call, Response<ValueTrainingModel> response) {
                String value = response.body().getValue();

                progressBar.setVisibility(View.GONE);
                if (value.equals("1")) {

                    results = response.body().getResult(); // menyimpan kedalam array results
                    RecyclerViewAdapter viewAdapter;
                    viewAdapter = new RecyclerViewAdapter(getContext(), results);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call<ValueTrainingModel> call, Throwable t) {
                try {
//                    Toast.makeText(getContext(), "Gagal Load Data Balita : "+t , Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(), "Gagal Memuat Data", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

}