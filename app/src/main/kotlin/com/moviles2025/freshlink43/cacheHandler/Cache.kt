package com.moviles2025.freshlink43.cacheHandler

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.moviles2025.freshlink43.model.Restaurant
import androidx.core.content.edit
import com.moviles2025.freshlink43.model.Order

// Guardar los primeros 5 restaurantes en SharedPreferences
fun saveRestaurantsToCache(context: Context, restaurants: List<Restaurant>) {
    val sharedPreferences = context.getSharedPreferences("restaurants_cache", Context.MODE_PRIVATE)
    sharedPreferences.edit() {

        val gson = Gson()
        val json = gson.toJson(restaurants.take(4)) // Guardamos solo los primeros 5

        putString("restaurants_cache_key", json)
    } // Guarda de manera asincrónica
}

// Obtener los restaurantes desde el caché (SharedPreferences)
fun getRestaurantsFromCache(context: Context): List<Restaurant> {
    val sharedPreferences = context.getSharedPreferences("restaurants_cache", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("restaurants_cache_key", null)

    return if (json != null) {
        val gson = Gson()
        val type = object : TypeToken<List<Restaurant>>() {}.type
        gson.fromJson(json, type)
    } else {
        emptyList()
    }
}

// Guardar las primeras 5 ordenes en SharedPreferences
fun saveOrdersToCache(context: Context, orders: List<Order>) {
    val sharedPreferences = context.getSharedPreferences("orders_cache", Context.MODE_PRIVATE)
    sharedPreferences.edit() {

        val gson = Gson()
        val json = gson.toJson(orders.take(4)) // Guardamos solo los primeros 5

        putString("orders_cache_key", json)
    } // Guarda de manera asincrónica
}

// Obtener las ordenes desde el caché (SharedPreferences)
fun getOrdersFromCache(context: Context): List<Order> {
    val sharedPreferences = context.getSharedPreferences("orders_cache", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("orders_cache_key", null)

    return if (json != null) {
        val gson = Gson()
        val type = object : TypeToken<List<Order>>() {}.type
        gson.fromJson(json, type)
    } else {
        emptyList()
    }
}

fun clearCache(context: Context) {
    val sharedPreferences = context.getSharedPreferences("restaurants_cache", Context.MODE_PRIVATE)
    sharedPreferences.edit() {

        remove("restaurants_cache_key")
    }
}
