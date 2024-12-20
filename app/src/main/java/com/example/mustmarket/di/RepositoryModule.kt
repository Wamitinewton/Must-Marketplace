package com.example.mustmarket.di

import com.example.mustmarket.usecase.UseCases
import com.example.mustmarket.core.retryConfig.RetryUtil
import com.example.mustmarket.features.auth.data.remote.AuthApi
import com.example.mustmarket.features.auth.data.repository.AuthRepositoryImpl
import com.example.mustmarket.features.auth.data.datastore.SessionManager
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import com.example.mustmarket.features.auth.domain.usecases.AuthUseCase
import com.example.mustmarket.database.dao.BookmarkDao
import com.example.mustmarket.database.dao.CategoryDao
import com.example.mustmarket.database.dao.ProductDao
import com.example.mustmarket.database.dao.UserDao
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.data.repository.AllProductsRepositoryImpl
import com.example.mustmarket.features.home.data.repository.BookmarkRepositoryImpl
import com.example.mustmarket.features.home.data.repository.CategoryRepositoryImpl
import com.example.mustmarket.features.home.data.repository.SearchProductsRepositoryImpl
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.domain.repository.BookmarkRepository
import com.example.mustmarket.features.home.domain.repository.CategoryRepository
import com.example.mustmarket.features.home.domain.repository.SearchProductsRepository
import com.example.mustmarket.features.home.domain.usecases.HomeUseCases
import com.example.mustmarket.features.home.secureStorage.SecureProductStorage
import com.example.mustmarket.features.merchant.products.data.remote.UploadProductsApi
import com.example.mustmarket.features.merchant.products.data.repository.ProductRepositoryImpl
import com.example.mustmarket.features.merchant.products.domain.repository.ProductRepository
import com.example.mustmarket.features.merchant.products.domain.usecases.AddProductUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Binds
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        sessionManager: SessionManager,
        userDao: UserDao
    ): AuthRepository {
        return AuthRepositoryImpl(
            authApi = authApi,
            sessionManger = sessionManager,
            userDao = userDao
        )
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
        dao: ProductDao,
        preferences: SecureProductStorage,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
        retryUtil: RetryUtil,
        sessionManager: SessionManager
    ): AllProductsRepository {
        return AllProductsRepositoryImpl(
            productsApi = allProductsApi,
            dao = dao,
            preferences = preferences,
            ioDispatcher = ioDispatcher,
            retryUtil = retryUtil,
            sessionManager = sessionManager
        )
    }

    @Provides
    @Singleton
    fun provideBookmarkRepository(dao: BookmarkDao): BookmarkRepository {
        return BookmarkRepositoryImpl(bookmarkDao = dao)
    }

    @Provides
    @Singleton
    fun provideSearchProductRepository(
        dao: ProductDao,
        productsApi: ProductsApi,
        bookmarkDao: BookmarkDao
    ): SearchProductsRepository {
        return SearchProductsRepositoryImpl(
            dao = dao,
            productsApi = productsApi,
            bookmarkDao = bookmarkDao
        )
    }

    @Provides
    @Singleton
    fun providesProductRepository(
        api: UploadProductsApi,
        @IODispatcher dispatcher: CoroutineDispatcher
    ): ProductRepository {
        return ProductRepositoryImpl(api, dispatcher = dispatcher)
    }


    @Provides
    @Singleton
    fun provideUseCases(
        authRepository: AuthRepository,
        categoryRepository: CategoryRepository,
        allProductsRepository: AllProductsRepository,
        bookmarkRepository: BookmarkRepository,
        searchProductsRepository: SearchProductsRepository,
        addProductRepository: ProductRepository
    ): UseCases =
        UseCases(
            authUseCase = AuthUseCase(repository = authRepository),
            homeUseCases = HomeUseCases(
                categoryRepository = categoryRepository,
                productRepository = allProductsRepository,
                bookmarksRepository = bookmarkRepository,
                searchProductsRepository = searchProductsRepository,
            ),
            addProduct = AddProductUseCase(
                repository = addProductRepository
            )
        )
}