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

import com.example.tesloginnavigationdrawer.Model.UpdateModel;
import com.example.tesloginnavigationdrawer.ui.training.TrainingFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends AppCompatActivity {

    Button btnUbah;
    EditText edtNama, edtBeratBadan, edtTinggiBadan;
    private Spinner spJenisKelamin, spStatusGizi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // untuk kembali tampilan sebelumnya
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnUbah = findViewById(R.id.btn_ubahh);
        edtNama = findViewById(R.id.edt_nama);
        edtBeratBadan = findViewById(R.id.edt_berat_badan);
        edtTinggiBadan = findViewById(R.id.edt_tinggi_badan);


        spStatusGizi = findViewById(R.id.sp_status_gizi);
        spJenisKelamin = findViewById(R.id.sp_jenis_kelamin);

        // mengambil data dari Intent Activity
        Intent intent = getIntent();
        String id_balita = intent.getStringExtra("id_balita");
        String nama = intent.getStringExtra("nama");
        String jenis_kelamin = intent.getStringExtra("jenis_kelamin");
        String berat_badan = intent.getStringExtra("berat_badan");
        String tinggi_badan = intent.getStringExtra("tinggi_badan");
        String label = intent.getStringExtra("label");

        // MENG SET SPINNER SESUAI DATA ASAL (jenis_kelamin)
        String myStringjk = jenis_kelamin; //the value you want the position for
        ArrayAdapter myAdapJK = (ArrayAdapter) spJenisKelamin.getAdapter(); //cast to an ArrayAdapter
        int spinnerPositionJK = myAdapJK.getPosition(myStringjk);
        //set the default according to value
        spJenisKelamin.setSelection(spinnerPositionJK);

        // MENG SET SPINNER SESUAI DATA ASAL (Status_gizi)
        String myStringLabel = label; //the value you want the position for
        ArrayAdapter myAdapLabel = (ArrayAdapter) spStatusGizi.getAdapter(); //cast to an ArrayAdapter
        int spinnerPositionLabel = myAdapLabel.getPosition(myStringLabel);
        //set the default according to value
        spStatusGizi.setSelection(spinnerPositionLabel);

        System.out.println("DATA BALITA UPDATEACTIVITY : "+nama);

        // memberikan isi form editText sesuai data balita yg diKlik
        edtNama.setText(nama);
        edtBeratBadan.setText(berat_badan);
        edtTinggiBadan.setText(tinggi_badan);

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double minBerat = 2.4d;
                double minTinggi = 44d;
                double maxBerat = 25d;
                double maxTinggi = 116d;

                String pIdBalita = id_balita;
                String pNama = edtNama.getText().toString();
                String pJenisKelamin = spJenisKelamin.getSelectedItem().toString();
                String pBeratBadan = edtBeratBadan.getText().toString();
                String pTinggiBadan= edtTinggiBadan.getText().toString();
                String pLabel = spStatusGizi.getSelectedItem().toString();

                try {
                    if(Double.parseDouble(pBeratBadan) < minBerat | Double.parseDouble(pBeratBadan) > maxBerat){
                        edtBeratBadan.setError("Input berat badan  2.5 - 24 kg");
                    } else if(Double.parseDouble(pTinggiBadan) < minTinggi | Double.parseDouble(pTinggiBadan) > maxTinggi ){
                        edtTinggiBadan.setError("Input tinggi badan 45 - 115 cm");
                    } else if(pBeratBadan.trim().equals("")){
                        edtBeratBadan.setError("Berat Badan Harus Diisi");
                    } else if(pNama.trim().equals("")){
                        edtNama.setError("Nama Tidak Boleh Kosong");
                    }else if(pTinggiBadan.trim().equals("")){
                        edtTinggiBadan.setError("Tinggi Badan Harus Diisi");
                    } else {
                        UpdateData(pIdBalita, pNama, pJenisKelamin, pBeratBadan, pTinggiBadan, pLabel);

                        finish();
                        Intent in = new Intent(UpdateActivity.this, TrainingFragment.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    private void UpdateData(String id, String nama, String jk, String bb, String tb, String label){
        ApiRequestData ardUpdate = RetroServer.konekRetrofit().create(ApiRequestData.class);
        Call<UpdateModel> ubahData = ardUpdate.ardUpdateDataTraning(id, nama, jk, bb, tb, label);

        ubahData.enqueue(new Callback<UpdateModel>() {
            @Override
            public void onResponse(Call<UpdateModel> call, Response<UpdateModel> response) {
                String value = response.body().getValue();
                String message = response.body().getMessage();


//              Toast.makeText(getApplicationContext(), "Value : "+value+ "| Message : "+message, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Data Berhasil Diubah", Toast.LENGTH_LONG).show();
//                seukrolView.fullScroll(ScrollView.FOCUS_DOWN);
//                mp.start();
            }

            @Override
            public void onFailure(Call<UpdateModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal Merubah Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
