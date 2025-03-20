package com.moviles2025.freshlink43.di

import android.content.Context
import com.moviles2025.freshlink43.ui.maps.MapFacade
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object MapModule {

    @Provides
    fun provideMapFacade(@ApplicationContext context: Context): MapFacade {
        return MapFacade(context)
    }
}