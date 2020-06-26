package com.blogger.lite.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.blogger.lite.data.models.Favorite;
import com.blogger.lite.data.models.Post;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = REPLACE)
    void save(Favorite favorite);

    @Delete
    void delete(Favorite favorite);

    @Query("SELECT posts.* FROM posts,favorites WHERE posts.id = favorites.id ORDER BY posts.updated DESC")
    LiveData<List<Post>> getFavorites();

    @Query("SELECT COUNT(id) FROM favorites WHERE id = :postId")
    int existInFavorite(String postId);
}
