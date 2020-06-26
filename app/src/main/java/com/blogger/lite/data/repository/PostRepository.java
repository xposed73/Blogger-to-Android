package com.blogger.lite.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.util.Log;

import com.blogger.lite.data.models.OcoPosts;
import com.blogger.lite.data.network.Webservice;
import com.blogger.lite.ui.helpers.Constants;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepository {
    private final Webservice webservice;

    public PostRepository(Webservice webservice) {
        this.webservice = webservice;
    }

    public LiveData<OcoPosts> getPosts(String token) {
        final MutableLiveData<OcoPosts> data = new MutableLiveData<>();
        webservice.getPosts(Constants.BLOGGER_KEY,token).enqueue(new Callback<OcoPosts>() {
            @Override
            public void onResponse(Call<OcoPosts> call, Response<OcoPosts> response) {
                if (response.isSuccessful()){
                    data.setValue(response.body());
                }else{
                    try {
                        Log.d("error networking", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<OcoPosts> call, Throwable t) {
                Log.d("error networking", t.getMessage());
            }
        });

        return data;
    }

}
