package com.example.tesloginnavigationdrawer.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroServer {

    private static final String baseURL = "http://balita.mywebcommunity.org/";  // awardspace.com
//    private static final String baseURL = "http://muhammadazmi.triweb.id/";
    private static final String baseURLbawaanLaptop = "http://192.168.137.1/balita/";
    private static final String baseURLhotspotHP = "http://192.168.43.154/balita/";

    private static final String mainBaseURL = baseURL;  // diganti sesuai api

    private static Retrofit retro;

    public static  Retrofit konekRetrofit(){
        if(retro == null){
            retro = new Retrofit.Builder()
                    .baseUrl(mainBaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retro;
    }

    public String getBaseURL(){
        String url = mainBaseURL;
        return url;
    }
}
