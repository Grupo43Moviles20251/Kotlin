package com.moviles2025.freshlink43.di

import com.google.firebase.firestore.FirebaseFirestore
import com.moviles2025.freshlink43.data.repository.FavoriteRepository
import com.moviles2025.freshlink43.data.repository.HomeRepository
import com.moviles2025.freshlink43.data.repository.RestaurantRepository
import com.moviles2025.freshlink43.data.serviceadapters.BackendServiceAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import com.moviles2025.freshlink43.data.repository.ForgotPasswordRepository
import com.moviles2025.freshlink43.data.repository.LoginRepository
import com.moviles2025.freshlink43.data.repository.SearchRepository
import com.moviles2025.freshlink43.data.repository.SignUpRepository
import com.moviles2025.freshlink43.data.serviceadapters.FirebaseServiceAdapter

//import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRestaurantRepository(
        firebaseServiceAdapter: FirebaseServiceAdapter
    ): RestaurantRepository {
        return RestaurantRepository(firebaseServiceAdapter)
    }

    @Provides
    @Singleton
    fun provideBackendServiceAdapter(): BackendServiceAdapter {
        return BackendServiceAdapter()
    }

    @Provides
    @Singleton
    fun provideHomeRepository(
        backendServiceAdapter: BackendServiceAdapter
    ): HomeRepository {
        return HomeRepository(backendServiceAdapter)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(
        @ApplicationContext context: Context
    ): FavoriteRepository {
        return FavoriteRepository(context)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        backendServiceAdapter: BackendServiceAdapter, firebaseServiceAdapter: FirebaseServiceAdapter
    ): LoginRepository {
        return LoginRepository(firebaseServiceAdapter,backendServiceAdapter)
    }

    @Provides
    @Singleton
    fun provideFirebaseServiceAdapter(): FirebaseServiceAdapter {
        return FirebaseServiceAdapter()
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(
        backendServiceAdapter: BackendServiceAdapter
    ): SignUpRepository {
        return SignUpRepository(backendServiceAdapter)
    }

    @Provides
    @Singleton
    fun provideForgotPasswordRepository(
        firebaseServiceAdapter: FirebaseServiceAdapter
    ): ForgotPasswordRepository {
        return ForgotPasswordRepository(firebaseServiceAdapter)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        backendServiceAdapter: BackendServiceAdapter
    ): SearchRepository {
        return SearchRepository(backendServiceAdapter)
    }
}