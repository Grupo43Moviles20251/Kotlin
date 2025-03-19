package com.moviles2025.freshlink43.data.repository

import android.content.Context
import com.moviles2025.freshlink43.ui.home.Product
import com.moviles2025.freshlink43.ui.home.Restaurant
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class SearchRepository {
    private val client = OkHttpClient()
    // Llamada a la API para obtener los restaurantes
    fun getFilteredRestaurants(query:String, callback: (List<Restaurant>?, String?) -> Unit) {
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/restaurants/search/$query")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                callback(null, "Error al obtener los restaurantes: ${e.localizedMessage}")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        // Convertir la respuesta JSON a objetos Restaurant
                        val restaurants = parseRestaurantsJson(it)
                        callback(restaurants, null)
                    }
                } else {
                    callback(null, "Error en la respuesta de la API: ${response.message}")
                }
            }
        })
    }

    fun getFilteredRestaurantsByType(type:String, callback: (List<Restaurant>?, String?) -> Unit) {
        val request = Request.Builder()
            .url("http://10.0.2.2:8000/restaurants/type/$type")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                callback(null, "Error al obtener los restaurantes: ${e.localizedMessage}")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        // Convertir la respuesta JSON a objetos Restaurant
                        val restaurants = parseRestaurantsJson(it)
                        callback(restaurants, null)
                    }
                } else {
                    callback(null, "Error en la respuesta de la API: ${response.message}")
                }
            }
        })
    }

    // Función para convertir el JSON a una lista de objetos Restaurant
    private fun parseRestaurantsJson(responseBody: String): List<Restaurant> {
        val restaurantsList = mutableListOf<Restaurant>()
        val jsonArray = JSONArray(responseBody)

        for (i in 0 until jsonArray.length()) {
            val restaurantJson = jsonArray.getJSONObject(i)

            val productsList = mutableListOf<Product>()
            val productsJson = restaurantJson.getJSONArray("products")
            for (j in 0 until productsJson.length()) {
                val productJson = productsJson.getJSONObject(j)
                val product = Product(
                    productJson.getInt("productId"),
                    productJson.getString("productName"),
                    productJson.getInt("amount"),
                    productJson.getBoolean("available"),
                    productJson.getDouble("discountPrice"),
                    productJson.getDouble("originalPrice")
                )
                productsList.add(product)
            }

            val restaurant = Restaurant(
                restaurantJson.getString("name"),
                restaurantJson.getString("imageUrl"),
                restaurantJson.getString("description"),
                restaurantJson.getDouble("latitude"),
                restaurantJson.getDouble("longitude"),
                restaurantJson.getString("address"),
                productsList,
                restaurantJson.getDouble("rating"),
                restaurantJson.getInt("type")
            )

            restaurantsList.add(restaurant)
        }

        return restaurantsList
    }
}