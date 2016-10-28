package com.example.sonata.attendancetakingapplication.Retrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class ErrorInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        switch (response.code()){
            case 401:
                Log.e("TEST","Unauthorized error for: " +request.url());
        }

        return response;
    }
}