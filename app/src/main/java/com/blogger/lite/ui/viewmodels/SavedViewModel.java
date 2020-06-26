package com.blogger.lite.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import android.content.Context;

import com.blogger.lite.data.AppDatabase;
import com.blogger.lite.data.dao.FavoriteDao;
import com.blogger.lite.data.models.Post;

import java.util.List;


public class SavedViewModel extends ViewModel {

    private FavoriteDao dao;
    private Context ctx;

    public void init(Context ctx) {
        this.ctx = ctx;
        this.dao = AppDatabase.getDatabase(ctx).favoriteDao();
    }

    public LiveData<List<Post>> getFavorites() {
        return this.dao.getFavorites();
    }
}
