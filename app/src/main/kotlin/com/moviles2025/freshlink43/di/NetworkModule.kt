package com.moviles2025.freshlink43.di

import android.content.Context
import com.moviles2025.freshlink43.network.ConnectivityHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideConnectivityHandler(@ApplicationContext context: Context): ConnectivityHandler {
        return ConnectivityHandler(context)
    }
}
