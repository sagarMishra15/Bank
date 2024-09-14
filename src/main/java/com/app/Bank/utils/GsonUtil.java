package com.app.Bank.utils;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Map;

public class GsonUtil {
    private GsonUtil(){}

    public static final Gson gson = new Gson();
    private static final Type type = new TypeToken<Map<String, Object>>(){}.getType();

    public static Map<String, Object> toMap(String body){
        return gson.fromJson(body, type);
    }
}
