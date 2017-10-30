package com.libra.utils;

/**
 * Created by libra on 2017/8/1.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JSONUtils {
    public static Gson gson =
            new GsonBuilder().registerTypeAdapter(LinkedHashMap.class,
                                   new JsonDeserializer<LinkedHashMap<String, Object>>() {
                                       @Override
                                       public LinkedHashMap<String, Object> deserialize(
                                               JsonElement json, Type typeOfT,
                                               JsonDeserializationContext context)
                                               throws JsonParseException {
                                           LinkedHashMap<String, Object> map =
                                                   new LinkedHashMap<>();
                                           JsonObject jsonObject = json.getAsJsonObject();
                                           Set<Map.Entry<String, JsonElement>> entrySet =
                                                   jsonObject.entrySet();
                                           for (Map.Entry<String, JsonElement> entry : entrySet) {
                                               if (entry.getValue().isJsonPrimitive()) {
                                                   JsonPrimitive jsonPrimitive =
                                                           entry.getValue().getAsJsonPrimitive();
                                                   if (jsonPrimitive.isBoolean()) {
                                                       map.put(entry.getKey(),
                                                               jsonPrimitive.getAsBoolean());
                                                   } else if (jsonPrimitive.isString()) {
                                                       map.put(entry.getKey(),
                                                               jsonPrimitive.getAsString());
                                                   } else {
                                                       BigDecimal bigDecimal =
                                                               jsonPrimitive.getAsBigDecimal();
                                                       try {
                                                           map.put(entry.getKey(),
                                                                   bigDecimal.intValueExact());
                                                       } catch (ArithmeticException e) {
                                                           try {
                                                               map.put(entry.getKey(),
                                                                       bigDecimal.longValueExact());
                                                           } catch (Exception e1) {
                                                               map.put(entry.getKey(),
                                                                       bigDecimal.doubleValue());
                                                           }
                                                       }
                                                   }
                                               } else {
                                                   map.put(entry.getKey(),
                                                           context.deserialize(entry.getValue(),
                                                                               Object.class));
                                               }
                                           }
                                           return map;
                                       }
                                   })
                             .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
                             .create();


    public static String toJson(Object o) {
        return gson.toJson(o);
    }


    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return gson.fromJson(json, classOfT);
        } catch (Exception e) {
            return null;
        }
    }


    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            return null;
        }
    }

    public static JsonElement toJsonTree(Object o) {
        return gson.toJsonTree(o);
    }
}
