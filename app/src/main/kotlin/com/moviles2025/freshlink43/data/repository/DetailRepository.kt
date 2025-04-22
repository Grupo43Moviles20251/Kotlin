package com.moviles2025.freshlink43.data.repository

import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import com.moviles2025.freshlink43.model.Restaurant

class DetailRepository(
    private val backendServiceAdapter: BackendServiceAdapter
) {
    //fun getRestaurantDetail (productId: Int, callback: (Restaurant?, String?) -> Unit) {
        //backendServiceAdapter.fetchRestaurantDetails(restaurantId) { dto, error ->
            //if (dto != null) {
                //val restaurant = dto.toDomain()
                //callback(restaurant, null)
            //} else {
                //callback(null, error)
            //}
        //}
    //}
}