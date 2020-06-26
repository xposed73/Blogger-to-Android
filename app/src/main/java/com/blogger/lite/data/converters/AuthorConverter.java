package com.blogger.lite.data.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.blogger.lite.data.models.Author;

public class AuthorConverter {
    @TypeConverter
    public static Author fromString(String value) {
        return new Gson().fromJson(value, Author.class);
    }

    @TypeConverter
    public static String fromAuthor(Author author) {
        Gson gson = new Gson();
        String json = gson.toJson(author);
        return json;
    }
}
