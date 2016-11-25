package com.example.sonata.attendancetakingapplication.Retrofit;

import com.example.sonata.attendancetakingapplication.BuildConfig;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Sonata on 11/14/2016.
 */

public abstract class ServerCallBack<T> implements Callback<T> {

    private Call<T> call;

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        this.call = call;

        if (BuildConfig.DEBUG) {
            t.printStackTrace();
        }
    }

    public Call<T> getCall() {
        return call;
    }
}
