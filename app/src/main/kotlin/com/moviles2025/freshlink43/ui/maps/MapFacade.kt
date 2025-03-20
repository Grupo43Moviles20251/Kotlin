package com.moviles2025.freshlink43.ui.maps

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.moviles2025.freshlink43.ui.home.Restaurant
import java.util.Locale

class MapFacade(private val context: Context) {

    fun initializeMap(googleMap: GoogleMap, userLocation: LatLng) {
        googleMap.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
        }
    }

    fun addRestaurantMarkers(googleMap: GoogleMap, restaurants: List<Restaurant>) {
        googleMap.clear() // Limpiar marcadores previos
        restaurants.forEach { restaurant ->
            val position = LatLng(restaurant.latitude, restaurant.longitude)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(restaurant.name)
                    .snippet("Descuento: ${restaurant.products[0].discountPrice}$")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )
            marker?.tag = restaurant
        }
    }

    fun getAddressFromCoordinates(latLng: LatLng): String? {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0)
        } catch (e: Exception) {
            Log.e("MapFacade", "Error obteniendo dirección: ${e.message}")
            null
        }
    }
}