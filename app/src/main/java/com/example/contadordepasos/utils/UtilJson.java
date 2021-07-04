package com.example.contadordepasos.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class UtilJson <T> {

    private final Gson gson = new Gson();

    public JSONObject MapperObj2Json(Object entity){
        String jsonInString = new Gson().toJson(entity);
        JSONObject postData = null;
        try {
            postData = new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postData;
    }

    public Object MapperJson2Obj(JSONObject jsonObject, Object o){
        return gson.fromJson(jsonObject.toString(),o.getClass());
    }

    public List<T> ListJson2ListObject(JSONArray jsonArray){
        Type typeList = new TypeToken<List<T>>(){}.getType();
        return gson.fromJson(String.valueOf(jsonArray), typeList);
    }
}
