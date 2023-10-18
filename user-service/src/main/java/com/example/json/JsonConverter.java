package com.example.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonConverter {

    private static final Gson gson = new Gson();

    public static <T> String toJson(T object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return gson.fromJson(json, type);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken);
    }
}
