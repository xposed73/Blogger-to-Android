package com.blogger.lite.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import android.content.Context;
import android.content.SharedPreferences;

import com.blogger.lite.data.AppDatabase;
import com.blogger.lite.data.dao.PostDao;
import com.blogger.lite.data.models.OcoPosts;
import com.blogger.lite.data.models.Post;
import com.blogger.lite.data.repository.PostRepository;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeViewModel extends ViewModel {

    private PostRepository postRepo;
    private String token;
    private SharedPreferences sharedPreferences;
    private PostDao dao;
    private Context ctx;
    public void init(Context ctx, PostRepository postRepo) {
        this.postRepo = postRepo;
        this.ctx = ctx;
        sharedPreferences = ctx.getSharedPreferences("com.blogger.lite", MODE_PRIVATE);
        this.token = sharedPreferences.getString("token",null);
        this.dao = AppDatabase.getDatabase(ctx).postDao();
    }

    public LiveData<List<Post>> getPosts(Boolean latest){

        String reqToken =  null;
        if (latest == false) {
               reqToken = this.token;
        }

        LiveData<OcoPosts> response = this.postRepo.getPosts(reqToken);

        LiveData<List<Post>> data = Transformations.switchMap(response, res -> {
            for (int i =0; i<res.getPosts().size(); i++){
                Post post = res.getPosts().get(i);
                this.token = res.getNextPageToken();
                sharedPreferences.edit().putString("token",this.token).apply();
                dao.save(post);
            }
            return dao.getAllPosts();
        });

        return data;
    }

}
