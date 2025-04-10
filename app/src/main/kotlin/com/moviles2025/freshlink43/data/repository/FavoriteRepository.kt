package com.moviles2025.freshlink43.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.moviles2025.freshlink43.model.Restaurant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )

    fun addFavorite(restaurant: Restaurant) {
        val favorites = getFavoriteNames().toMutableSet()
        favorites.add(restaurant.name)
        saveFavoriteNames(favorites)
    }

    fun removeFavorite(restaurant: Restaurant) {
        val favorites = getFavoriteNames().toMutableSet()
        favorites.remove(restaurant.name)
        saveFavoriteNames(favorites)
    }

    fun isFavorite(restaurant: Restaurant): Boolean {
        return getFavoriteNames().contains(restaurant.name)
    }

    fun getFavorites(allRestaurants: List<Restaurant>): List<Restaurant> {
        val favorites = getFavoriteNames()
        return allRestaurants.filter { favorites.contains(it.name) }
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