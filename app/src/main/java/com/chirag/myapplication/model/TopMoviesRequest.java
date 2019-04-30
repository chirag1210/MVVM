package com.chirag.myapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TopMoviesRequest {

    @GET("bins/18buhu")
    Call<TopMoviesModel> getMoviesData();

}
