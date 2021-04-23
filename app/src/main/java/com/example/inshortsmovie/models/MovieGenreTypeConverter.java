package com.example.inshortsmovie.models;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MovieGenreTypeConverter {

    @TypeConverter
    public static List<MovieGenre> fromString(String value) {
        Type listType = new TypeToken<List<MovieGenre>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<MovieGenre> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
