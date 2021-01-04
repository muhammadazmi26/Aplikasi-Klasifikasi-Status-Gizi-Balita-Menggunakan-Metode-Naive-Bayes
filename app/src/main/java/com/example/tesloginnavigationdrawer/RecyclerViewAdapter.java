package com.example.tesloginnavigationdrawer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesloginnavigationdrawer.API.ApiRequestData;
import com.example.tesloginnavigationdrawer.API.RetroServer;
import com.example.tesloginnavigationdrawer.Model.DeleteModel;
import com.example.tesloginnavigationdrawer.Model.ResultTrainingModel;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private Context context;
    private List<ResultTrainingModel> results;
    int  hitung = 0;
    int posisi;

    private AdapterView.OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context context, List<ResultTrainingModel> results) {
        this.context = context;
        this.results = results;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_training, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    public void updateData(List<ResultTrainingModel> resultTrainingModels) {
        results.clear();
        results.addAll(resultTrainingModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, ResultTrainingModel resultTrainingModel) {
        results.add(position, resultTrainingModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        results.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ResultTrainingModel result = results.get(position);
        System.out.println("POSISI ONBINDVIEWHOLDER :"+position);
        setPosisi(position);

        holder.tvIdBalita.setText(result.getId_balita());
        holder.tvNama.setText(result.getNama());
        holder.tvJenisKelamin.setText(result.getJenis_kelamin());
        holder.tvBeratBadan.setText(result.getBerat_badan());
        System.out.println("BERAT BADAN : "+result.getBerat_badan());
        holder.tvTinggiBadan.setText(result.getTinggi_badan());
        holder.tvStatusGizi.setText(result.getLabel());

        System.out.print("PENGHITUNG : ");
        System.out.println(hitung++);
        System.out.print("ID balita yg tampil dilayar hp : ");
        System.out.println(result.getId_balita());
        System.out.print("SET POSISI : "+posisi);

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu_training);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_data:
                                //handle edit_data click
                                //Toast.makeText(context, "Data Balita yg di klik : "+result.getId_balita(), Toast.LENGTH_SHORT).show();
                                pindahKeUpdateActivity(result);
                                break;
                            case R.id.hapus_data:
                                //handle hapus_data click
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                                alertDialogBuilder.setTitle("Peringatan");
                                alertDialogBuilder
                                        .setMessage("Apakah Anda Yakin Ingin Mengapus Data "+result.getNama())
                                        .setCancelable(false)
                                        .setPositiveButton("Hapus",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {

                                                String id_balita = result.getId_balita();

                                                ApiRequestData ardDelete = RetroServer.konekRetrofit().create(ApiRequestData.class);
                                                Call<DeleteModel> hapusData = ardDelete.ardDeleteDataTraining(id_balita);

                                                hapusData.enqueue(new Callback<DeleteModel>() {
                                                    @Override
                                                    public void onResponse(Call<DeleteModel> call, Response<DeleteModel> response) {
                                                        String value = response.body().getValue();
                                                        String message = response.body().getMessage();
                                                        if (value.equals("1")) {

                                                            // untuk reload recyclerView setelah dihapus
                                                            notifyItemRemoved(position);    //  untuk menghapus item/data diREcyclerview 😪
//                                                            Toast.makeText(context, "Data Indek ke-"+ position +" Behasil Dihapus ...", Toast.LENGTH_LONG).show();
                                                            Toast.makeText(context, "Data Berhasil Dihapus", Toast.LENGTH_LONG).show();
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
                                        .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,int id) {
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
                //displaying the popup
                popup.show();

            }
        });
    }

    private int setPosisi(int pos) {
        return posisi = pos;
    }

    void pindahKeUpdateActivity(ResultTrainingModel result){   // parameter ini utk mengirimkan data balita yg diKlik ke UpdateActivity
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK );

        intent.putExtra("id_balita", result.getId_balita());
        intent.putExtra("nama", result.getNama());
        intent.putExtra("jenis_kelamin", result.getJenis_kelamin());
        intent.putExtra("berat_badan", result.getBerat_badan());
        intent.putExtra("tinggi_badan", result.getTinggi_badan());
        intent.putExtra("label", result.getLabel());

        System.out.println("-----------------------put intent---------------------");
        System.out.println("ID BALITA E: "+result.getId_balita());
        System.out.println("BERAT BADAN E: "+result.getBerat_badan());
        System.out.println("TINGGI BADAN E: "+result.getTinggi_badan());
        System.out.println("LABEL E : "+result.getLabel());
        System.out.println("--------------------------------------------");

        context.startActivity(intent);
        Toast.makeText(context, "Data Balita yg di klik ubah : "+result.getId_balita(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.textNPM) TextView textViewNPM;
//        @BindView(R.id.textNama) TextView textViewNama;
//        @BindView(R.id.textKelas) TextView textViewKelas;
//        @BindView(R.id.textSesi) TextView textViewSesi;

        TextView tvIdBalita, tvNama, tvJenisKelamin, tvBeratBadan, tvTinggiBadan, tvStatusGizi;
        TextView buttonViewOption;

        public ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);

            tvIdBalita = itemView.findViewById(R.id.tv_id_balita);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvJenisKelamin = itemView.findViewById(R.id.tv_jk);
            tvBeratBadan = itemView.findViewById(R.id.tv_bb);
            tvTinggiBadan = itemView.findViewById(R.id.tv_tb);
            tvStatusGizi = itemView.findViewById(R.id.tv_status_gizi);
            buttonViewOption = itemView.findViewById(R.id.tv_option_menu);
        }
    }

}
