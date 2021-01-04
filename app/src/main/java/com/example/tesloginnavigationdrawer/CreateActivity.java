package com.example.tesloginnavigationdrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tesloginnavigationdrawer.API.ApiRequestData;
import com.example.tesloginnavigationdrawer.API.RetroServer;
import com.example.tesloginnavigationdrawer.Model.CreateModel;
import com.example.tesloginnavigationdrawer.ui.training.TrainingFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends AppCompatActivity {

    Button btnSimpan;
    EditText edtNama, edtJenisKelamin, edtBeratBadan, edtTinggiBadan, edtLabel;
    private Spinner spJenisKelamin, spStatusGizi;
    String nama, berat,tinggi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        // untuk kembali tampilan sebelumnya
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        btnSimpan = findViewById(R.id.btn_simpan);
        edtNama = findViewById(R.id.edt_namaC);
//        edtJenisKelamin = findViewById(R.id.edt_jkC);
        edtBeratBadan = findViewById(R.id.edt_berat_badanC);
        edtTinggiBadan = findViewById(R.id.edt_tinggi_badanC);

        spStatusGizi = findViewById(R.id.sp_status_gizi);
        spJenisKelamin = findViewById(R.id.sp_jenis_kelamin);
//       btSpinner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(), "SPINNER YG DIPILIH : "+ spJenisKelamin.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double minBerat = 2.4d;
                double minTinggi = 44d;
                double maxBerat = 25d;
                double maxTinggi = 116d;

                nama = edtNama.getText().toString();
                berat = edtBeratBadan.getText().toString();
                tinggi = edtTinggiBadan.getText().toString();

                try {
                    if(Double.parseDouble(berat) <= minBerat | Double.parseDouble(berat) >= maxBerat){
                        edtBeratBadan.setError("Input berat badan  2.5 - 24 kg");
                    } else if(Double.parseDouble(tinggi) <= minTinggi | Double.parseDouble(tinggi) >= maxTinggi ){
                        edtTinggiBadan.setError("Input tinggi badan 45 - 115 cm");
                    } else if(berat.trim().equals("")){
                        edtBeratBadan.setError("Berat Badan Harus Diisi");
                    } else if(nama.trim().equals("")){
                        edtNama.setError("Nama Tidak Boleh Kosong");
                    }else if(tinggi.trim().equals("")){
                        edtTinggiBadan.setError("Tinggi Badan Harus Diisi");
                    } else {
                        createData(edtNama.getText().toString(), spJenisKelamin.getSelectedItem().toString(), edtBeratBadan.getText().toString(), edtTinggiBadan.getText().toString(), spStatusGizi.getSelectedItem().toString());

                        finish();
                        Intent in = new Intent(CreateActivity.this, TrainingFragment.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

//              startActivity(in);

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    // untuk kembali tampilan sebelumnya
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void createData(String nama, String jk, String bb, String tb, String label){
        ApiRequestData ardCreate = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<CreateModel> tambahData = ardCreate.ardCreateDataTraning(nama, jk, bb, tb, label);

        tambahData.enqueue(new Callback<CreateModel>() {
            @Override
            public void onResponse(Call<CreateModel> call, Response<CreateModel> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();
                
//                Toast.makeText(getApplicationContext(), "Value : "+value+ "| Message : "+message, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Data Berhasil Ditambah", Toast.LENGTH_LONG).show();
//                seukrolView.fullScroll(ScrollView.FOCUS_DOWN);
//                mp.start();
            }

            @Override
            public void onFailure(Call<CreateModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), " Gagal Menambahkan Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
