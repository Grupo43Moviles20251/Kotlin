package com.moviles2025.freshlink43.data.serviceadapters

import com.moviles2025.freshlink43.data.dto.ProductDto
import com.moviles2025.freshlink43.data.dto.RestaurantDto
import com.moviles2025.freshlink43.data.dto.UserDto
import com.moviles2025.freshlink43.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class BackendServiceAdapter {

    private val client = OkHttpClient()

    fun fetchRestaurantsByQuery(query: String, callback: (List<RestaurantDto>?, String?) -> Unit) {
        val request = Request.Builder()
            .url("${Constants.BASE_URL}/restaurants/search/$query")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback(null, e.localizedMessage)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val restaurants = body?.let { parseRestaurantsJson(it) }
                    callback(restaurants, null)
                } else {
                    callback(null, response.message)
                }
            }
        })
    }

    fun fetchRestaurantsByType(type: String, callback: (List<RestaurantDto>?, String?) -> Unit) {
        val request = Request.Builder()
            .url("${Constants.BASE_URL}/restaurants/type/$type")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                callback(null, e.localizedMessage)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val restaurants = body?.let { parseRestaurantsJson(it) }
                    callback(restaurants, null)
                } else {
                    callback(null, response.message)
                }
            }
        })
    }
    /** Obtener restaurantes **/
    fun registerUserWithEmail(
        userDto: UserDto,
        callback: (Boolean, String?) -> Unit
    ) {
        val json = JSONObject().apply {
            put("name", userDto.name)
            put("email", userDto.email)
            put("password", userDto.password)
            put("address", userDto.address)
            put("birthday", userDto.birthday)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("${Constants.BASE_URL}/signup")
            .post(requestBody)
            .header("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                callback(false, e.localizedMessage)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, response.body?.string() ?: "Unknown error")
                }
            }
        })
    }

    fun fetchRestaurants(callback: (List<RestaurantDto>?, String?) -> Unit) {
        val request = Request.Builder()
            .url("${Constants.BASE_URL}/restaurants")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                callback(null, "Error en fetchRestaurants: ${e.localizedMessage}")
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    responseBody?.let {
                        val restaurants = parseRestaurantsJson(it)
                        callback(restaurants, null)
                    }
                } else {
                    callback(null, "Error de respuesta: ${response.message}")
                }
            }
        })
    }

    /** Verificar si usuario existe en el backend **/
    fun verifyUser(idToken: String, callback: (Boolean, String?) -> Unit) {
        val request = Request.Builder()
            .url("${Constants.BASE_URL}/users/me")
            .get()
            .addHeader("Authorization", "Bearer $idToken")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                callback(false, e.localizedMessage)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true, null) // Usuario existe
                } else if (response.code == 404) {
                    callback(false, null) // Usuario no existe
                } else {
                    callback(false, response.message)
                }
            }
        })
    }

    /** Registrar un nuevo usuario en el backend **/
    fun registerUser(userDto: UserDto, idToken: String, callback: (Boolean, String?) -> Unit) {
        val json = JSONObject().apply {
            put("name", userDto.name)
            put("email", userDto.email)
            put("password", userDto.password)
            put("address", userDto.address)
            put("birthday", userDto.birthday)
            //put("photoUrl", userDto.photoUrl)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("${Constants.BASE_URL}/signup")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $idToken")
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                callback(false, e.localizedMessage)
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, response.message)
                }
            }
        })
    }

    /** Conversor de restaurantes **/
    private fun parseRestaurantsJson(responseBody: String): List<RestaurantDto> {
        val restaurantsList = mutableListOf<RestaurantDto>()
        val jsonArray = JSONArray(responseBody)

        for (i in 0 until jsonArray.length()) {
            val restaurantJson = jsonArray.getJSONObject(i)

            val productsList = mutableListOf<ProductDto>()
            val productsJson = restaurantJson.getJSONArray("products")
            for (j in 0 until productsJson.length()) {
                val productJson = productsJson.getJSONObject(j)
                val product = ProductDto(
                    productId = productJson.getInt("productId"),
                    productName = productJson.getString("productName"),
                    amount = productJson.getInt("amount"),
                    available = productJson.getBoolean("available"),
                    discountPrice = productJson.getDouble("discountPrice"),
                    originalPrice = productJson.getDouble("originalPrice")
                )
                productsList.add(product)
            }

            val restaurant = RestaurantDto(
                name = restaurantJson.getString("name"),
                imageUrl = restaurantJson.getString("imageUrl"),
                description = restaurantJson.getString("description"),
                latitude = restaurantJson.getDouble("latitude"),
                longitude = restaurantJson.getDouble("longitude"),
                address = restaurantJson.getString("address"),
                rating = restaurantJson.getDouble("rating"),
                type = restaurantJson.getInt("type"),
                products = productsList
            )
            restaurantsList.add(restaurant)
        }

        return restaurantsList
    }
}