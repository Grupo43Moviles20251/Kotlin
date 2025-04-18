package com.moviles2025.freshlink43.ui.maps

import android.content.Context
import android.location.Geocoder
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.moviles2025.freshlink43.data.dto.RestaurantMaps
import java.util.*

class MapFacade(private val context: Context) {

    private var googleMap: GoogleMap? = null


    fun initializeMap(map: GoogleMap, userLocation: LatLng) {
        googleMap = map
        googleMap?.apply {
            moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            uiSettings.isZoomControlsEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isMapToolbarEnabled = true
            uiSettings.isCompassEnabled = true
        }
    }

    fun addRestaurantMarkers(restaurantMaps: List<RestaurantMaps>) {
        if (googleMap == null) {
            Log.e("MapFacade", "GoogleMap no está inicializado")
            return
        }

        googleMap?.apply {
            clear()
            restaurantMaps.forEach { restaurant ->
                val position = LatLng(restaurant.latitude, restaurant.longitude)
                val discountText = restaurant.products.firstOrNull()?.discountPrice?.let {
                    "$$it"
                } ?: "No products"

                val marker = addMarker(
                    MarkerOptions()
                        .position(position)
                        .title(restaurant.name)
                        .snippet("Descuento: $discountText")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                )
                marker?.tag = restaurant
            }
        }
    }

/*
    fun getAddressFromCoordinates(latLng: LatLng): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0) ?: "Dirección no encontrada"
        } catch (e: Exception) {
            Log.e("MapFacade", "Error obteniendo dirección: ${e.message}")
            null
        }
    }

 */

    fun moveCameraToLocation(location: LatLng, zoomLevel: Float = 15f) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
            ?: Log.e("MapFacade", "GoogleMap no está inicializado")
    }
}