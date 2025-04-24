package com.moviles2025.freshlink43.data.serviceadapters

import com.moviles2025.freshlink43.data.dto.ProductDto
import com.moviles2025.freshlink43.data.dto.RestaurantDto
import com.moviles2025.freshlink43.data.dto.UserDto
import com.moviles2025.freshlink43.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject

class BackendServiceAdapter {

    private val client = OkHttpClient()

    suspend fun fetchRestaurants(): Result<List<RestaurantDto>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url("${Constants.BASE_URL}/restaurants").build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body?.string() ?: return@withContext Result.failure(Exception("Empty body"))
                Result.success(parseRestaurantsJson(body))
            } else {
                Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchRestaurantsByQuery(query: String): Result<List<RestaurantDto>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url("${Constants.BASE_URL}/restaurants/search/$query").build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body?.string() ?: return@withContext Result.failure(Exception("Empty body"))
                Result.success(parseRestaurantsJson(body))
            } else {
                Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchRestaurantsByType(type: String): Result<List<RestaurantDto>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url("${Constants.BASE_URL}/restaurants/type/$type").build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body?.string() ?: return@withContext Result.failure(Exception("Empty body"))
                Result.success(parseRestaurantsJson(body))
            } else {
                Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun fetchRestaurantDetails(productId: Int): Result<RestaurantDto> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url("${Constants.BASE_URL}/products/$productId").build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val body = response.body?.string() ?: return@withContext Result.failure(Exception("Empty body"))
                val restaurant = parseRestaurantJson(body) ?: return@withContext Result.failure(Exception("Invalid JSON"))
                Result.success(restaurant)
            } else {
                Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun verifyUser(idToken: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("${Constants.BASE_URL}/users/me")
                .get()
                .addHeader("Authorization", "Bearer $idToken")
                .build()

            val response = client.newCall(request).execute()

            when (response.code) {
                200 -> Result.success(true)
                404 -> Result.success(false)
                else -> Result.failure(Exception("HTTP ${response.code}: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerUserWithEmail(userDto: UserDto): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
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

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.body?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun registerUser(userDto: UserDto, idToken: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
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
                .addHeader("Authorization", "Bearer $idToken")
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                Result.success(true)
            } else {
                Result.failure(Exception(response.body?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    private fun parseRestaurantJson(responseBody: String): RestaurantDto? {
        val jsonObject = JSONObject(responseBody)
        val productsList = mutableListOf<ProductDto>()
        val productsJson = jsonObject.getJSONArray("products")
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
        return RestaurantDto(
            name = jsonObject.getString("name"),
            imageUrl = jsonObject.getString("imageUrl"),
            description = jsonObject.getString("description"),
            latitude = jsonObject.getDouble("latitude"),
            longitude = jsonObject.getDouble("longitude"),
            address = jsonObject.getString("address"),
            rating = jsonObject.getDouble("rating"),
            type = jsonObject.getInt("type"),
            products = productsList
        )
    }
}
