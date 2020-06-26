package com.blogger.lite.data.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.blogger.lite.data.models.Blog;

public class BlogConverter {
    @TypeConverter
    public static Blog fromString(String value) {
        return new Gson().fromJson(value, Blog.class);
    }

    @TypeConverter
    public static String fromBlog(Blog blog) {
        Gson gson = new Gson();
        String json = gson.toJson(blog);
        return json;
    }
}
