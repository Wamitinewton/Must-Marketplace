package com.example.mustmarket

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mustmarket.core.retryConfig.RetryUtil
import com.example.mustmarket.features.home.data.local.db.ProductDao
import com.example.mustmarket.features.home.data.remote.ProductsApi
import com.example.mustmarket.features.home.data.repository.AllProductsRepositoryImpl
import com.example.mustmarket.features.home.secureStorage.SecureProductStorage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * See [Medium guide for android tests](https://medium.com/getir/test-your-android-app-unit-test-with-mockk-28c1c465bafc#:~:text=every%20%2F%20coEvery,used%20for%20mocking%20suspend%20functions.).
 */

@OptIn(ExperimentalCoroutinesApi::class)
class AllProductsImplTest {
    private lateinit var repository: AllProductsRepositoryImpl
    private lateinit var api: ProductsApi
    private lateinit var productsDao: ProductDao
    private lateinit var prefs: SecureProductStorage
    private lateinit var retryUtil: RetryUtil
    private val testDispatcher = StandardTestDispatcher()

    @Before
    // setting up objects before running the test cases using @Before
    fun setUp() {
        api = mockk()
        productsDao = mockk()
        prefs = mockk()
        retryUtil = mockk()

        repository = AllProductsRepositoryImpl(
            productsApi = api,
            dao = productsDao,
            preferences = prefs,
            ioDispatcher = testDispatcher,
            retryUtil = retryUtil
        )
    }

    @Test
    fun `shouldRefresh returns true when cache is empty`() = runTest {
        val currentTime = System.currentTimeMillis()
        val oldTimeStamp = currentTime - (25 * 60 * 60 * 1000)
        coEvery {  }
    }
}