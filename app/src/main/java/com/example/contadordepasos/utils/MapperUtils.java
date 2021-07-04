package com.example.contadordepasos.utils;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
public class MapperUtils {
    public MapperUtils(){}
    public static JSONObject MapperObj2Json(Object entity){
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
        Gson gson=new Gson();
        return gson.fromJson(jsonObject.toString(),o.getClass());
    }
}
