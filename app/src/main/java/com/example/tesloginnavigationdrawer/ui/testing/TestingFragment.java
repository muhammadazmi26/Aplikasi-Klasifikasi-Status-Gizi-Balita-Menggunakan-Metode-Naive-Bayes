package com.example.tesloginnavigationdrawer.ui.testing;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tesloginnavigationdrawer.API.ApiRequestData;
import com.example.tesloginnavigationdrawer.API.RetroServer;
import com.example.tesloginnavigationdrawer.MainActivity;
import com.example.tesloginnavigationdrawer.Model.TestingModel;
import com.example.tesloginnavigationdrawer.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestingFragment extends Fragment {

    private TestingViewModel testingViewModel;

    private EditText edtBerat, edtTinggi, edtNama;
    TextView tvHasil, tvKeterangan;
    View card_hasil;
    private Button btnSimpan;
    private String nama, jk, berat, tinggi, a="L";

//    private RadioGroup radioJK;
    Spinner spJenisKelamin;

    ScrollView seukrolView;
    ProgressBar progressBar;

    MediaPlayer mp ;
    String keterangan;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        testingViewModel =
                ViewModelProviders.of(this).get(TestingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_testing, container, false);
        final TextView textView = root.findViewById(R.id.tv_data_testing);

        //////////////////////// Yg ditambah sendiri//////////////////////
//        Button btnSimpan = root.findViewById(R.id.btn_simpan);
//        final TextView tvNama = root.findViewById(R.id.tv_nama);
//        final EditText edtNama = root.findViewById(R.id.edt_nama);
//
//        btnSimpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getContext(), "Tombol Simpan Telah Ditekan !", Toast.LENGTH_SHORT).show();
//                String nama = edtNama.getText().toString();
//                tvNama.setText(nama);
//            }
//        });

        ///////////////////////////////////////////////////////////////////\
        progressBar = root.findViewById(R.id.progress_bar);

        spJenisKelamin = root.findViewById(R.id.sp_jenis_kelamin);

        edtNama = root.findViewById(R.id.editText_nama);
        edtBerat = root.findViewById(R.id.editText_berat);
        edtTinggi = root.findViewById(R.id.editText_tinggi);
        btnSimpan = root.findViewById(R.id.btn_prediksi);

        tvHasil = root.findViewById(R.id.tv_hasil);
        card_hasil = root.findViewById(R.id.tampil_hasil);
        tvKeterangan = root.findViewById(R.id.tv_keterangan);

        seukrolView = root.findViewById(R.id.seukrol_view);
//        mp = MediaPlayer.create(this, R.raw.tick);

//        radioJK = root.findViewById(R.id.rg_jk);
//
//        radioJK.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int id) {
//                switch (id){
//                    case R.id.rb_laki:
//                        a = "L";
//                        System.out.println("JENIS : "+a);
//                        break;
//                    case R.id.rb_perempuan:
//                        a = "P";
//                        break;
//                }
//            }
//        });


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

//                jk = edtJk.getText().toString();
                jk = spJenisKelamin.getSelectedItem().toString();
                berat = edtBerat.getText().toString();
                tinggi = edtTinggi.getText().toString();
                nama = edtNama.getText().toString().trim();

                // Range angka input
                double minBerat = 2.4d;
                double minTinggi = 44d;
                double maxBerat = 25d;
                double maxTinggi = 116d;

                try {
                    if(nama.trim().equals("")){
                        edtNama.setError("Nama tidak boleh kosong");
                    } else if(Double.parseDouble(berat) <= minBerat | Double.parseDouble(berat) >= maxBerat){
                        edtBerat.setError("Input berat badan  2.5 - 24 kg");
                    } else if(Double.parseDouble(tinggi) <= minTinggi | Double.parseDouble(tinggi) >= maxTinggi ){
                        edtTinggi.setError("Input tinggi badan 45 - 115 cm");
                    } else if(berat.trim().equals("")){
                        edtBerat.setError("Beart Badan Harus Diisi");
                    } else if(tinggi.trim().equals("")){
                        edtTinggi.setError("Tinggi Badan Harus Diisi");
                    } else {
                        createData();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        });



        ////////////////////////////////////////////////////////////////

        testingViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;



    }

    private void createData(){
        ApiRequestData ardData = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<TestingModel> simpanData = ardData.ardCreateDataTesting(nama, jk, berat, tinggi);

        simpanData.enqueue(new Callback<TestingModel>() {
            @Override
            public void onResponse(Call<TestingModel> call, Response<TestingModel> response) {
                String data = response.body().getData();
                int kode = response.body().getKode();

                progressBar.setVisibility(View.GONE);
                card_hasil.setVisibility(View.VISIBLE);
                System.out.println("ISI HASIL KALSIFIKASI : "+ data);

                if(data.equals("Sangat Kurus")){
                    keterangan = "Disarankan : \n\nMemberi makanan terapeutik dan susu formula khusus F-75.\n\n ";
                } else if (data.equals("Kurus")) {
                    keterangan = "Disarankan : \n\nMenambah porsi makan anak jika anak mampu untuk menghabiskan makanannya lebih dari satu porsi.\n\nBerikan anak jus buah- dan susu.";
                } else if (data.equals("Normal")) {
                    keterangan = "disarankan : \n\nJaga pola makan, tetap berikan anak makanan yang sehat dan bergizi agar kebutuhan gizi anak terpenuhi";
                } else if (data.equals("Gemuk")) {
                    keterangan = "Disarankan :\n\nHindari memberikan anak makanan utama dengan porsi yang terlalu besar.\n\nMakanan olahan, junk food, serta gorengan merupakan beberapa contoh makanan yang sebaiknya tidak sering dimakan.\n\nPerbanyak aktivitas fisik harian anak.";
                } else{
                    keterangan = "-";
                }

                tvHasil.setText(data);
                tvKeterangan.setText(keterangan);

//              Toast.makeText(MainActivity.this, "Kode : "+kode+ "| Data : "+data, Toast.LENGTH_SHORT).show();

                seukrolView.fullScroll(ScrollView.FOCUS_DOWN);
//                mp.start();
            }

            @Override
            public void onFailure(Call<TestingModel> call, Throwable t) {
//                Toast.makeText(getContext(), " Haduhh !! 😳 Gagal Menghubungi Server : "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Gagal Memproses Data", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}