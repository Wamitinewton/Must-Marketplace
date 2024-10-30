package com.example.mustmarket.di

import com.example.mustmarket.UseCases
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.data.repository.AuthRepositoryImpl
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import com.example.mustmarket.features.auth.domain.usecases.AuthUseCase
import com.example.mustmarket.features.home.data.local.db.BookmarkDao
import com.example.mustmarket.features.home.data.local.db.CategoryDao
import com.example.mustmarket.features.home.data.local.db.ProductDao
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.data.repository.AllProductsRepositoryImpl
import com.example.mustmarket.features.home.data.repository.BookmarkRepositoryImpl
import com.example.mustmarket.features.home.data.repository.CategoryRepositoryImpl
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.domain.repository.BookmarkRepository
import com.example.mustmarket.features.home.domain.repository.CategoryRepository
import com.example.mustmarket.features.home.domain.usecases.HomeUseCases
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
    fun provideCategoryRepository(
        categoryProductsApi: ProductsApi,
        dao: CategoryDao
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryApi = categoryProductsApi, dao = dao)
    }

    @Provides
    @Singleton
    fun provideAllProductsRepository(
        allProductsApi: ProductsApi,
        dao: ProductDao
    ): AllProductsRepository {
        return AllProductsRepositoryImpl(productsApi = allProductsApi, dao = dao)
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(dao: BookmarkDao): BookmarkRepository {
        return BookmarkRepositoryImpl(bookmarkDao = dao)
    }


    @Provides
    @Singleton
    fun provideUseCases(
        authRepository: AuthRepository,
        categoryRepository: CategoryRepository,
        allProductsRepository: AllProductsRepository,
        bookmarkRepository: BookmarkRepository
    ): UseCases =
        UseCases(
            authUseCase = AuthUseCase(repository = authRepository),
            homeUseCases = HomeUseCases(
                categoryRepository = categoryRepository,
                productRepository = allProductsRepository,
                bookmarksRepository = bookmarkRepository
            )
        )
}