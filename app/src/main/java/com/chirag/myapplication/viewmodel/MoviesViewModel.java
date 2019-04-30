package com.chirag.myapplication.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.chirag.myapplication.model.MovieModel;
import com.chirag.myapplication.model.TopMoviesModel;
import com.chirag.myapplication.model.TopMoviesRequest;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesViewModel extends AndroidViewModel {

    private static final String TAG = "MoviesViewModel";

    /**
     * Context instance
     */
    private Context mContext;

    /*
     * Instance of LiveData
     * */
    private MutableLiveData<List<MovieModel>> dataObserver = new MutableLiveData<>();

    /**
     * Constructor
     * @param application
     */
    public MoviesViewModel(@NonNull Application application) {
        super(application);
        mContext=application.getBaseContext();
    }

    public LiveData<List<MovieModel>> getDataObserver() {
        return dataObserver;
    }

    public void requestMoviesAPI(){
        /*
         * OkHttp Logging Interceptor
         * */
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://api.myjson.com/");

        Retrofit retrofit = retrofitBuilder.build();

        TopMoviesRequest topMoviesRequest = retrofit.create(TopMoviesRequest.class);
        Call<TopMoviesModel> request = topMoviesRequest.getMoviesData();
        request.enqueue(new Callback<TopMoviesModel>()
        {
            @Override
            public void onResponse(@NonNull Call<TopMoviesModel> call, @NonNull Response<TopMoviesModel> response) {
                TopMoviesModel responseBody = response.body();

                List<MovieModel> moviesList = responseBody.getMovieModelList();
                //Toast.makeText(mContext,"Movies size "+moviesList.size(),Toast.LENGTH_LONG).show();
                dataObserver.postValue(moviesList);
            }

            @Override
            public void onFailure(@NonNull Call<TopMoviesModel> call, @NonNull Throwable throwable)
            {
                Log.e(TAG, "Failed to fetch Movies List", throwable);
            }
        });
    }

}
