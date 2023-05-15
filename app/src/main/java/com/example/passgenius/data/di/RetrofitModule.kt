package com.example.passgenius.data.di

import com.example.passgenius.common.Constants
import com.example.passgenius.data.apis.ItemsApi
import com.example.passgenius.data.apis.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {





    @Provides
    @Singleton
    fun provideItemsApi(): ItemsApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_heroku)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItemsApi::class.java)
    }





}