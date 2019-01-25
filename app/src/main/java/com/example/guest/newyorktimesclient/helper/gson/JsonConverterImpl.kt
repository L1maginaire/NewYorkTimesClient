package com.example.guest.newyorktimesclient.helper.gson

import com.google.gson.Gson

class JsonConverterImpl(private val gson: Gson) : JsonConverter {
    override fun <T> fromJson(json: String, clazz: Class<*>): T? = gson.fromJson(json, clazz) as T
    override fun toJson(src: Any): String = gson.toJson(src)
}