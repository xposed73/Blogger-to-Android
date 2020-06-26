package com.blogger.lite.data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;

import com.blogger.lite.data.converters.AuthorConverter;
import com.blogger.lite.data.converters.BlogConverter;
import com.blogger.lite.data.converters.ReplyConverter;
import com.blogger.lite.data.converters.TimestampConverter;
import com.blogger.lite.data.dao.FavoriteDao;
import com.blogger.lite.data.dao.PostDao;
import com.blogger.lite.data.models.Favorite;
import com.blogger.lite.data.models.Post;

@Database(entities = {Post.class, Favorite.class}, version = 1, exportSchema = false)
@TypeConverters({ReplyConverter.class, BlogConverter.class, AuthorConverter.class, TimestampConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "blogger_db")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public abstract PostDao postDao();
    public abstract FavoriteDao favoriteDao();
}
