package uz.gita.onlineshop.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.onlineshop.domain.AppRepository
import uz.gita.onlineshop.domain.impl.AppRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @[Singleton Binds]
    fun getRepository(impl: AppRepositoryImpl) : AppRepository



}