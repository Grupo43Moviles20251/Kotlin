package com.moviles2025.freshlink43.di

import com.google.firebase.firestore.FirebaseFirestore
import com.moviles2025.freshlink43.data.services.RestaurantService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRestaurantRepository(
        firestore: FirebaseFirestore
    ): RestaurantService {
        return RestaurantService(firestore)
    }
}