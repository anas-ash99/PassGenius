package com.example.passgenius.data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.passgenius.domain.use_cases.TestUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Provides
    @Singleton
    fun provideTestUseCase(): TestUseCase {
        return TestUseCase()
    }


    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context) =
        context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)!!







}