package com.example.tesloginnavigationdrawer.API;

import com.example.tesloginnavigationdrawer.Model.CreateModel;
import com.example.tesloginnavigationdrawer.Model.DeleteModel;
import com.example.tesloginnavigationdrawer.Model.TestingModel;
import com.example.tesloginnavigationdrawer.Model.UpdateModel;
import com.example.tesloginnavigationdrawer.Model.ValueRiwayatModel;
import com.example.tesloginnavigationdrawer.Model.ValueTrainingModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiRequestData {

    @FormUrlEncoded
    @POST("nb_android.php")
    Call<TestingModel> ardCreateDataTesting(@Field("nama") String nama,
                                            @Field("jk") String jk,
                                            @Field("berat") String berat,
                                            @Field("tinggi") String tinggi
    );

    @GET("view_training.php")
    Call<ValueTrainingModel> ardViewDataTraining();

    @FormUrlEncoded
    @POST("update_training.php")
    Call<UpdateModel> ardUpdateDataTraning(@Field("id_balita") String id_balita,
                                           @Field("nama") String nama,
                                           @Field("jenis_kelamin") String jenis_kelamin,
                                           @Field("berat_badan") String bb,
                                           @Field("tinggi_badan") String tb,
                                           @Field("label") String label
    );

    @FormUrlEncoded
    @POST("create_training.php")
    Call<CreateModel> ardCreateDataTraning(@Field("nama") String nama,
                                           @Field("jenis_kelamin") String jenis_kelamin,
                                           @Field("berat_badan") String bb,
                                           @Field("tinggi_badan") String tb,
                                           @Field("label") String label
    );

    @FormUrlEncoded
    @POST("delete_training.php")
    Call<DeleteModel> ardDeleteDataTraining(@Field("id_balita") String id_balita);

    @GET("view_riwayat_testing.php")
    Call<ValueRiwayatModel> ardViewRiwayat();

    @FormUrlEncoded
    @POST("delete_riwayat.php")
    Call<DeleteModel> ardDeleteRiwayat(@Field("id_testing") String id_testing);

}
