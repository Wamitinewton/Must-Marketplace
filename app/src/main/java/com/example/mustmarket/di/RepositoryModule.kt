package com.example.mustmarket.di

import com.example.mustmarket.database.dao.BookmarkDao
import com.example.mustmarket.database.dao.CategoryDao
import com.example.mustmarket.database.dao.ProductDao
import com.example.mustmarket.database.dao.UserDao
import com.example.mustmarket.features.auth.data.datastore.SessionManager
import com.example.mustmarket.features.auth.data.remote.service.AuthenticationService
import com.example.mustmarket.features.auth.data.repository.AuthRepositoryImpl
import com.example.mustmarket.features.auth.domain.repository.AuthRepository
import com.example.mustmarket.features.auth.domain.usecases.AuthUseCase
import com.example.mustmarket.features.home.data.remote.api_service.ProductsApi
import com.example.mustmarket.features.home.data.repository.AllProductsRepositoryImpl
import com.example.mustmarket.features.home.data.repository.BookmarkRepositoryImpl
import com.example.mustmarket.features.home.data.repository.CategoryRepositoryImpl
import com.example.mustmarket.features.home.data.repository.SearchProductsRepositoryImpl
import com.example.mustmarket.features.home.domain.repository.AllProductsRepository
import com.example.mustmarket.features.home.domain.repository.BookmarkRepository
import com.example.mustmarket.features.home.domain.repository.CategoryRepository
import com.example.mustmarket.features.home.domain.repository.SearchProductsRepository
import com.example.mustmarket.features.home.domain.usecases.HomeUseCases
import com.example.mustmarket.features.inbox.chat.model.ChatDao
import com.example.mustmarket.features.inbox.repository.ChatRepository
import com.example.mustmarket.features.merchant.products.data.remote.UploadProductsApi
import com.example.mustmarket.features.merchant.products.data.repository.ProductRepositoryImpl
import com.example.mustmarket.features.merchant.products.domain.repository.ProductRepository
import com.example.mustmarket.features.merchant.products.domain.usecases.AddProductUseCase
import com.example.mustmarket.usecase.UseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthenticationService,
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
    fun provideChatRepository(chatDao: ChatDao): ChatRepository {
        return ChatRepository(chatDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(
        categoryProductsApi: ProductsApi,
        dao: CategoryDao,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): CategoryRepository {
        return CategoryRepositoryImpl(
            categoryApi = categoryProductsApi,
            dao = dao,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideAllProductsRepository(
        allProductsApi: ProductsApi,
        dao: ProductDao,
        @IODispatcher ioDispatcher: CoroutineDispatcher,
    ): AllProductsRepository {
        return AllProductsRepositoryImpl(
            productsApi = allProductsApi,
            dao = dao,
            ioDispatcher = ioDispatcher,
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