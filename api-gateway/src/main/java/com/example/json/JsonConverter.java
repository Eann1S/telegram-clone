package com.example.json;

import com.google.gson.Gson;

public class JsonConverter {

    private static final Gson GSON = new Gson();

    private JsonConverter() {
    }

    public static <T> T fromJson(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }
}
