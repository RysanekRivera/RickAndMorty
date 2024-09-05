package io.github.rysanekrivera.home_api_impl.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.rysanekrivera.home_api.repositories.RickAndMortyRepository
import io.github.rysanekrivera.home_api_impl.repositories.RickAndMortyRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RickAndMortyModule {

    @Singleton
    @Provides
    fun provideRickAndMortyRepository(impl: RickAndMortyRepositoryImpl): RickAndMortyRepository = impl

}