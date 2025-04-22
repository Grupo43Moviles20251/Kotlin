package com.moviles2025.freshlink43.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.moviles2025.freshlink43.model.Restaurant
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class FavoriteRepository @Inject constructor(
    context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    suspend fun addFavorite(restaurant: Restaurant) = withContext(Dispatchers.IO) {
        val favorites = getFavoriteNames().toMutableSet()
        favorites.add(restaurant.name)
        saveFavoriteNames(favorites)
    }

    suspend fun removeFavorite(restaurant: Restaurant) = withContext(Dispatchers.IO) {
        val favorites = getFavoriteNames().toMutableSet()
        favorites.remove(restaurant.name)
        saveFavoriteNames(favorites)
    }

    suspend fun isFavorite(restaurant: Restaurant): Boolean = withContext(Dispatchers.IO) {
        getFavoriteNames().contains(restaurant.name)
    }

    suspend fun getFavorites(allRestaurants: List<Restaurant>): List<Restaurant> = withContext(Dispatchers.IO) {
        val favorites = getFavoriteNames()
        allRestaurants.filter { it.name in favorites }
    }

    private fun getFavoriteNames(): Set<String> {
        return sharedPreferences.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }

    private fun saveFavoriteNames(favorites: Set<String>) {
        sharedPreferences.edit()
            .putStringSet(FAVORITES_KEY, favorites)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "favorites_prefs"
        private const val FAVORITES_KEY = "favorites_list"
    }
}