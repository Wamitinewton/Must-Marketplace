package com.newton.mustmarket.di

import com.newton.mustmarket.database.dao.BookmarkDao
import com.newton.mustmarket.database.dao.CategoryDao
import com.newton.mustmarket.database.dao.ProductDao
import com.newton.mustmarket.database.dao.UserDao
import com.newton.mustmarket.features.auth.data.datastore.SessionManager
import com.newton.mustmarket.features.auth.data.remote.service.AuthenticationService
import com.newton.mustmarket.features.auth.data.repository.AuthRepositoryImpl
import com.newton.mustmarket.features.auth.domain.repository.AuthRepository
import com.newton.mustmarket.features.auth.domain.usecases.AuthUseCase
import com.newton.mustmarket.features.products.data.remote.api_service.ProductsApi
import com.newton.mustmarket.features.products.data.repository.AllProductsRepositoryImpl
import com.newton.mustmarket.features.products.data.repository.BookmarkRepositoryImpl
import com.newton.mustmarket.features.products.data.repository.CategoryRepositoryImpl
import com.newton.mustmarket.features.products.data.repository.SearchProductsRepositoryImpl
import com.newton.mustmarket.features.products.domain.repository.AllProductsRepository
import com.newton.mustmarket.features.products.domain.repository.BookmarkRepository
import com.newton.mustmarket.features.products.domain.repository.CategoryRepository
import com.newton.mustmarket.features.products.domain.repository.SearchProductsRepository
import com.newton.mustmarket.features.products.domain.usecases.HomeUseCases
import com.newton.mustmarket.features.inbox.chat.model.ChatDao
import com.newton.mustmarket.features.inbox.repository.ChatRepository
import com.newton.mustmarket.features.merchant.products.data.remote.UploadProductsApi
import com.newton.mustmarket.features.merchant.products.data.repository.ProductRepositoryImpl
import com.newton.mustmarket.features.merchant.products.domain.repository.ProductRepository
import com.newton.mustmarket.features.merchant.products.domain.usecases.AddProductUseCase
import com.newton.mustmarket.features.merchant.create_store.data.remote.api_service.MerchantServices
import com.newton.mustmarket.features.merchant.create_store.data.repository.MerchantRepositoryImpl
import com.newton.mustmarket.features.merchant.create_store.domain.repository.MerchantRepository
import com.newton.mustmarket.features.merchant.create_store.domain.usecases.MerchantUseCase
import com.newton.mustmarket.usecase.UseCases
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
        return ProductRepositoryImpl(api = api, dispatcher = dispatcher)
    }

    @Provides
    @Singleton
    fun provideMerchantRepository(
        api: MerchantServices
    ): MerchantRepository {
        return MerchantRepositoryImpl(merchantServices = api)
    }


    @Provides
    @Singleton
    fun provideUseCases(
        authRepository: AuthRepository,
        categoryRepository: CategoryRepository,
        allProductsRepository: AllProductsRepository,
        bookmarkRepository: BookmarkRepository,
        searchProductsRepository: SearchProductsRepository,
        addProductRepository: ProductRepository,
        merchantRepository: MerchantRepository
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
            ),
            merchantUseCase = MerchantUseCase(
                merchantRepository = merchantRepository
            )
        )
}