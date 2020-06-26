package com.blogger.lite.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.blogger.lite.data.models.Post;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PostDao {
    @Insert(onConflict = REPLACE)
    void save(Post post);

    @Query("SELECT * FROM posts WHERE id = :postId")
    LiveData<Post> loadPost(String postId);

    //Get All Users
    @Query("SELECT * FROM posts ORDER BY updated DESC")
    LiveData<List<Post>> getAllPosts();

    @Query("SELECT COUNT(id) FROM posts WHERE id = :postId")
    int exists(String postId);

}
