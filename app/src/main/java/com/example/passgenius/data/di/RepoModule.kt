package com.example.passgenius.data.di

import com.example.passgenius.domain.repository.MyRepoImpl
import com.example.passgenius.data.repository.MyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepoModule {


    @Binds
    @Singleton
    abstract fun provideMyRpo(repo: MyRepoImpl): MyRepository
}