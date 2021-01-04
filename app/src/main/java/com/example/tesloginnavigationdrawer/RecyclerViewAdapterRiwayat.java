package com.example.tesloginnavigationdrawer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesloginnavigationdrawer.API.ApiRequestData;
import com.example.tesloginnavigationdrawer.API.RetroServer;
import com.example.tesloginnavigationdrawer.Model.DeleteModel;
import com.example.tesloginnavigationdrawer.Model.ResultRiwayatModel;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewAdapterRiwayat extends RecyclerView.Adapter<RecyclerViewAdapterRiwayat.ViewHolder>{


    private Context context;
    private List<ResultRiwayatModel> results;
    int  hitung = 0;
    int posisi;

    public RecyclerViewAdapterRiwayat(Context context, List<ResultRiwayatModel> results) {
        this.context = context;
        this.results = results;
    }


    @Override
    public RecyclerViewAdapterRiwayat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_riwayat, parent, false);
        RecyclerViewAdapterRiwayat.ViewHolder holder = new RecyclerViewAdapterRiwayat.ViewHolder(v);

        return holder;
    }


    public void updateData(List<ResultRiwayatModel> resultRiwayatModels) {
        results.clear();
        results.addAll(resultRiwayatModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, ResultRiwayatModel resultRiwayatModel) {
        results.add(position, resultRiwayatModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        results.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterRiwayat.ViewHolder holder, int position) {
        ResultRiwayatModel result = results.get(position);
        System.out.println("POSISI ONBINDVIEWHOLDER :"+position);
        setPosisi(position);

        holder.tvIdTesting.setText(result.getId_testing());
        holder.tvNama.setText(result.getNama());
        holder.tvJenisKelamin.setText(result.getJenis_kelamin());
        holder.tvBeratBadan.setText(result.getBerat_badan());
        holder.tvTinggiBadan.setText(result.getTinggi_badan());
        holder.tvStatusGizi.setText(result.getLabel());

        System.out.print("PENGHITUNG : ");
        System.out.println(hitung++);
        System.out.print("ID balita yg tampil dilayar hp : ");
        System.out.println(result.getId_testing());
        System.out.print("SET POSISI : "+posisi);

        holder.buttonHapusRiwayat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.buttonHapusRiwayat);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu_riwayat);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cetak_pdf:
                                //handle edit_data click
//                                Toast.makeText(context, "Sedang Membuat PDF "+result.getNama(), Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Sedang Membuat PDF ...: ", Toast.LENGTH_LONG).show();
                                cetak(Integer.parseInt(result.getId_testing()));
                                break;
                            case R.id.hapus_data:
                                ///////////////////////////////////////////////////
                                //handle hapus_data click
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("Peringatan");
                                alertDialogBuilder
                                        .setMessage("Apakah Anda Yakin Ingin Mengapus Data " + result.getNama())
                                        .setCancelable(false)
                                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                String id_testing = result.getId_testing();

                                                ApiRequestData ardDelete = RetroServer.konekRetrofit().create(ApiRequestData.class);
                                                Call<DeleteModel> hapusData = ardDelete.ardDeleteRiwayat(id_testing);

                                                hapusData.enqueue(new Callback<DeleteModel>() {
                                                    @Override
                                                    public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                                                        String value = response.body().getValue();
                                                        String message = response.body().getMessage();
                                                        if (value.equals("1")) {

                                                            // untuk reload recyclerView setelah dihapus
//                                                            notifyItemRemoved(position);    //  untuk menghapus item/data diREcyclerview 😪
                                                            removeItem(position);
//                                                            Toast.makeText(context, "Data Indek ke-" + position + " Behasil Dihapus ...", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(context, "Berhasil Menghaspus Data", Toast.LENGTH_LONG).show();
                                                        } else {
                                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<DeleteModel> call, Throwable t) {
                                                        t.printStackTrace();
                                                        Toast.makeText(context, "Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    void cetak(int id_data){
        Intent webOffline = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.137.1/balita/download_pdf.php?id_data="+id_data));
        Intent webOnline = new Intent(Intent.ACTION_VIEW, Uri.parse("http://balita.mywebcommunity.org/download_pdf.php?id_data="+id_data));
        Intent webOfflineRedmi5A = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.43.154/balita/download_pdf.php?id_data="+id_data));
        context.startActivity(webOnline);
    }

    private int setPosisi(int pos) {
        return posisi = pos;
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIdTesting, tvNama, tvJenisKelamin, tvBeratBadan, tvTinggiBadan, tvStatusGizi;
        TextView buttonHapusRiwayat;

        public ViewHolder(View itemView) {
            super(itemView);
            tvIdTesting = itemView.findViewById(R.id.tv_id_balita);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvJenisKelamin = itemView.findViewById(R.id.tv_jk);
            tvBeratBadan = itemView.findViewById(R.id.tv_bb);
            tvTinggiBadan = itemView.findViewById(R.id.tv_tb);
            tvStatusGizi = itemView.findViewById(R.id.tv_status_gizi);
            buttonHapusRiwayat = itemView.findViewById(R.id.tv_hapus_testing);
        }
    }
}
