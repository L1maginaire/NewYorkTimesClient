package com.example.guest.newyorktimesclient.helper.gson

interface JsonConverter {
    fun <T> fromJson(json: String, clazz: Class<*>): T?
    fun toJson(src: Any): String
}