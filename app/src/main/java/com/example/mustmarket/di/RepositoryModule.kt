package com.example.mustmarket.di

import com.example.mustmarket.UseCases
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.data.repository.AuthRepositoryImpl
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import com.example.mustmarket.features.auth.domain.usecases.LoginUseCase
import com.example.mustmarket.features.auth.domain.usecases.SignUpUseCase
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
    fun provideAuthRepository(authApi: AuthApi): AuthRepository{
        return AuthRepositoryImpl(authApi = authApi)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        authRepository: AuthRepository,
    ): UseCases =
        UseCases(
            signUpUseCase = SignUpUseCase(repository = authRepository),
            loginUseCase = LoginUseCase(repository = authRepository)
        )
}