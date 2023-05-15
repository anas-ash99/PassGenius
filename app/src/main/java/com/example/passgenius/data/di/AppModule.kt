package com.example.passgenius.data.di

import com.example.passgenius.domain.use_cases.TestUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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






}