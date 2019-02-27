package hn.creatoo.com.web.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.log4j.Logger;

/**
 * JSON 和 字符串 和 JAVA对象相互转换
 * Created by wangxl on 2017/6/6.
 */
public class JsonUtils {
    /**
     * google gson 对象
     */
    private static final Gson gson = new Gson();

    /**
     * 将JAVA对象转换成JSON字符串
     * @param obj JAVA对象
     * @return JSON字符串
     */
    public static String toJsonStr(Object obj){
        String jsonStr = "";
        try{
            jsonStr = gson.toJson(obj);
        }catch (Exception e){
            Logger.getLogger(JsonUtils.class).error(e.getMessage(), e);
        }
        return jsonStr;
    }

    /**
     * 将JSON字符串转换成JsonObject对象
     * @param json JSON字符串
     * @return JsonObject
     */
    public static JsonObject toJsonObj(String json){
        JsonObject jsonObj = null;
        try{
            jsonObj = gson.fromJson(json, JsonObject.class);
        }catch (Exception e){
            Logger.getLogger(JsonUtils.class).error(e.getMessage(), e);
        }
        return jsonObj;
    }

    /**
     * 将JSON字符串转换成JsonArray对象
     * @param json JSON字符串
     * @return JsonArray对象
     */
    public static JsonArray toJsonArr(String json){
        JsonArray jsonObj = null;
        try{
            jsonObj = gson.fromJson(json, JsonArray.class);
        }catch (Exception e){
            Logger.getLogger(JsonUtils.class).error(e.getMessage(), e);
        }
        return jsonObj;
    }

    /**
     * 将JSON字符串转换成JAVA对象
     * @param json JSON字符串
     * @param t JAVA对象class
     * @param <T> JAVA对象类型
     * @return JAVA对象实例
     */
    public static <T> T toJavaObj(String json, Class<T> t){
        T jsonObj = null;
        try{
            jsonObj = gson.fromJson(json, t);
        }catch (Exception e){
            Logger.getLogger(JsonUtils.class).error(e.getMessage(), e);
        }
        return jsonObj;
    }

    /**
     * 将JSON字符串转换成JAVA对象
     * @param json JsonElement对象
     * @param t JAVA对象class
     * @param <T> JAVA对象类型
     * @return JAVA对象实例
     */
    public static <T> T toJavaObj(JsonElement json, Class<T> t){
        T jsonObj = null;
        try{
            jsonObj = gson.fromJson(json, t);
        }catch (Exception e){
            Logger.getLogger(JsonUtils.class).error(e.getMessage(), e);
        }
        return jsonObj;
    }
}
