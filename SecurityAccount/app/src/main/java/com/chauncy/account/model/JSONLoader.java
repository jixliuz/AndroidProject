package com.chauncy.account.model;

import android.content.res.AssetManager;

import com.chauncy.account.utils.Global;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSONLoader {
    private static JSONLoader instance = new JSONLoader();

    private String ACCOUNT_FILE = "account_data.json";


    public static JSONLoader get() {
        return instance;
    }


    public String loadJSON(String file) {
        AssetManager manager = Global.getContext().getAssets();
        try {
            InputStreamReader reader = new InputStreamReader(manager.open(file), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
                builder.append(line);
            bufferedReader.close();
            reader.close();

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public <T> Map<String, List<T>> parseJSON(Class<T> tClass) {
        Map<String, List<T>> map = new HashMap<>();
        JsonObject root = new JsonParser().parse(loadJSON(ACCOUNT_FILE)).getAsJsonObject();
        Set<String> set = root.keySet();
        for (String type : set) {
            List<T> element = parseJSON(root.getAsJsonArray(type), tClass);
            map.put(type, element);
        }
        return map;
    }

    private <T> List<T> parseJSON(JsonArray jsonArray, Class<T> tClass) {
        List<T> result = new ArrayList<>();
        Gson gson = new Gson();
        for (int i = 0, size = jsonArray.size(); i < size; i++) {
            T element=gson.fromJson(jsonArray.get(i), tClass);
            result.add(element);
        }
        return result;
    }


}
