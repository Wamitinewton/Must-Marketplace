package com.newton.mustmarket.repositoryTest
//
//import android.arch.core.executor.testing.InstantTaskExecutorRule
//import com.example.mustmarket.core.retryConfig.RetryUtil
//import com.example.mustmarket.features.home.data.local.db.ProductDao
//import com.example.mustmarket.features.home.data.mapper.toProductListingEntity
//import com.example.mustmarket.features.home.data.remote.api_service.ProductsApi
//import com.example.mustmarket.features.home.data.repository.AllProductsRepositoryImpl
//import com.example.mustmarket.features.home.domain.model.products.NetworkProduct
//import com.example.mustmarket.features.home.secureStorage.SecureProductStorage
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.TestCoroutineScheduler
//import kotlinx.coroutines.test.TestScope
//import kotlinx.coroutines.test.UnconfinedTestDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Test
//
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Rule
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// *
// * See [Medium guide for android tests](https://medium.com/getir/test-your-android-app-unit-test-with-mockk-28c1c465bafc#:~:text=every%20%2F%20coEvery,used%20for%20mocking%20suspend%20functions.).
// */
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class AllProductsImplTest {
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    private val scheduler = TestCoroutineScheduler()
//    private val testDispatcher = UnconfinedTestDispatcher(scheduler)
//    private val testScope = TestScope(testDispatcher)
//
//    private lateinit var repository: AllProductsRepositoryImpl
//    private lateinit var api: ProductsApi
//    private lateinit var productsDao: ProductDao
//    private lateinit var prefs: SecureProductStorage
//    private lateinit var retryUtil: RetryUtil
//
//    @Before
//    // setting up objects before running the test cases using @Before
//    fun setUp() {
//
//        Dispatchers.setMain(testDispatcher)
//
//        api = mockk(relaxed = true)
//
//        productsDao = mockk(relaxed = true)
//
//        prefs = mockk(relaxed = true)
//
//        retryUtil = mockk(relaxed = true)
//
//        repository = AllProductsRepositoryImpl(
//            productsApi = api,
//
//            dao = productsDao,
//
//            preferences = prefs,
//
//            ioDispatcher = testDispatcher,
//
//            retryUtil = retryUtil
//        )
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `shouldRefresh returns true when cache is empty`() = testScope.runTest {
//
//        // Given
//        val currentTime = System.currentTimeMillis()
//
//        val oldTimeStamp = currentTime - (25 * 60 * 60 * 1000)
//
//        coEvery { prefs.getLastUpdateTimestamp() } returns oldTimeStamp
//
//        coEvery { productsDao.getAllProducts() } returns flowOf(emptyList())
//
//
//        // when
//        val result = repository.shouldRefresh()
//
//
//        // then
//        assertEquals(true, result)
//    }
//
//    @Test
//    fun `shouldRefresh returns true when cache is expired`() = testScope.runTest {
//
//        // Given
//        val currentTime = System.currentTimeMillis()
//
//        val oldTimeStamp = currentTime - (25 * 60 * 60 * 1000)
//
//        coEvery { prefs.getLastUpdateTimestamp() } returns oldTimeStamp
//
//        coEvery { productsDao.getAllProducts() } returns flowOf(listOf())
//
//        // when
//
//        val result = repository.shouldRefresh()
//
//        // Then
//
//        assertEquals(true, result)
//    }
//
//    @Test
//    fun `shouldRefresh returns false when cache is valid and not empty`() = testScope.runTest {
//        // Given
//        val currentTime = System.currentTimeMillis()
//
//        coEvery { prefs.getLastUpdateTimestamp() } returns currentTime
//
//        val mockProduct = mockk<NetworkProduct>(relaxed = true)
//
//        coEvery { productsDao.getAllProducts() } returns flowOf(listOf(mockProduct.toProductListingEntity()))
//
//        // when
//        val result = repository.shouldRefresh()
//
//        // Then
//        assertEquals(false, result)
//    }
//}