package com.example.mustmarket.di

import com.example.mustmarket.UseCases
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.data.repository.AuthRepositoryImpl
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import com.example.mustmarket.features.auth.domain.usecases.LoginUseCase
import com.example.mustmarket.features.auth.domain.usecases.SignUpUseCase
import com.example.mustmarket.features.auth.domain.usecases.TokenSession
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.data.repository.AllProductsRepositoryImpl
import com.example.mustmarket.features.home.data.repository.CategoryRepositoryImpl
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.domain.repository.CategoryRepository
import com.example.mustmarket.features.home.domain.usecases.AllProducts
import com.example.mustmarket.features.home.domain.usecases.Categories
import com.example.mustmarket.features.home.domain.usecases.ProductCategories
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
    fun provideAuthRepository(authApi: AuthApi): AuthRepository {
        return AuthRepositoryImpl(authApi = authApi)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryProductsApi: ProductsApi): CategoryRepository {
        return CategoryRepositoryImpl(categoryApi = categoryProductsApi)
    }

    @Provides
    @Singleton
    fun provideAllProductsRepository(allProductsApi: ProductsApi): AllProductsRepository {
        return AllProductsRepositoryImpl(productsApi = allProductsApi)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        authRepository: AuthRepository,
        categoryRepository: CategoryRepository,
        allProductsRepository: AllProductsRepository
    ): UseCases =
        UseCases(
            signUpUseCase = SignUpUseCase(repository = authRepository),
            loginUseCase = LoginUseCase(repository = authRepository),
            tokenLogin = TokenSession(repository = authRepository),
            productCategories = ProductCategories(repository = categoryRepository),
            categories = Categories(repository = categoryRepository),
            allProducts = AllProducts(repository = allProductsRepository)
        )
}