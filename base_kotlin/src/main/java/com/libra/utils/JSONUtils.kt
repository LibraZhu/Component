package com.libra.utils

/**
 * Created by libra on 2017/8/1.
 */

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Modifier
import java.lang.reflect.Type
import java.util.*

object JSONUtils {
    var gson = GsonBuilder().registerTypeAdapter(LinkedHashMap::class.java,
                                                 JsonDeserializer { json, typeOfT, context ->
                                                     val map = LinkedHashMap<String, Any>()
                                                     val jsonObject = json.asJsonObject
                                                     val entrySet = jsonObject.entrySet()
                                                     for ((key, value) in entrySet) {
                                                         if (value.isJsonPrimitive) {
                                                             val jsonPrimitive = value.asJsonPrimitive
                                                             if (jsonPrimitive.isBoolean) {
                                                                 map.put(key,
                                                                         jsonPrimitive.asBoolean)
                                                             } else if (jsonPrimitive.isString) {
                                                                 map.put(key,
                                                                         jsonPrimitive.asString)
                                                             } else {
                                                                 val bigDecimal = jsonPrimitive.asBigDecimal
                                                                 try {
                                                                     map.put(key,
                                                                             bigDecimal.intValueExact())
                                                                 } catch (e: ArithmeticException) {
                                                                     try {
                                                                         map.put(key,
                                                                                 bigDecimal.longValueExact())
                                                                     } catch (e1: Exception) {
                                                                         map.put(key,
                                                                                 bigDecimal.toDouble())
                                                                     }

                                                                 }

                                                             }
                                                         } else {
                                                             map.put(key, context.deserialize(value,
                                                                                              Any::class.java))
                                                         }
                                                     }
                                                     return@JsonDeserializer map
                                                 }).disableHtmlEscaping().excludeFieldsWithModifiers(
            Modifier.STATIC, Modifier.TRANSIENT).create()


    fun toJson(o: Any): String {
        return gson.toJson(o)
    }


    fun <T> fromJson(json: String, classOfT: Class<T>): T? {
        return try {
            gson.fromJson(json, classOfT)
        } catch (e: Exception) {
            null
        }

    }


    fun <T> fromJson(json: String, typeOfT: Type): T? {
        return try {
            gson.fromJson<T>(json, typeOfT)
        } catch (e: Exception) {
            null
        }

    }

    fun toJsonTree(o: Any): JsonElement {
        return gson.toJsonTree(o)
    }
}
