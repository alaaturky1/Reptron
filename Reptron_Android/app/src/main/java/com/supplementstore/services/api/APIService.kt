package com.supplementstore.services.api

import android.content.Context
import com.supplementstore.datastore.TokenManager
import com.supplementstore.models.AddToCartRequest
import com.supplementstore.models.AuthResponse
import com.supplementstore.models.Coach
import com.supplementstore.models.CreateBookingRequest
import com.supplementstore.models.CreateOrderRequest
import com.supplementstore.models.CreateReviewRequest
import com.supplementstore.models.Equipment
import com.supplementstore.models.AnalyzeFrameRequest
import com.supplementstore.models.ExerciseResponse
import com.supplementstore.models.LoginRequest
import com.supplementstore.models.Product
import com.supplementstore.models.Purchase
import com.supplementstore.models.RegisterRequest
import com.supplementstore.models.Review
import com.supplementstore.models.StartSessionRequest
import com.supplementstore.models.StartSessionResponse
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Header

interface PowerFuelApi {

    @POST("start-session")
    suspend fun startFitnessSession(
        @Body request: StartSessionRequest,
        @Header("X-API-Key") apiKey: String? = APIClient.AI_API_KEY
    ): Response<StartSessionResponse>

    @POST("analyze-frame")
    suspend fun analyzeExercise(
        @Body request: AnalyzeFrameRequest,
        @Header("X-API-Key") apiKey: String? = APIClient.AI_API_KEY
    ): Response<ExerciseResponse>

    // ================== Auth ==================
    @POST("api/Auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/Auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>


    // ================== Cart ==================
    @GET("api/Cart")
    suspend fun getCart(): Response<com.supplementstore.models.CartResponse> // 👈 التعديل هنا


    @POST("api/Cart/items")
    suspend fun addToCart(@Body request: AddToCartRequest): Response<Any>

    @PUT("api/Cart/items/{cartItemId}")
    suspend fun updateCartItemQuantity(
        @Path("cartItemId") cartItemId: Int,
        @Query("quantity") quantity: Int
    ): Response<Any>

    @DELETE("api/Cart/items/{cartItemId}")
    suspend fun removeCartItem(@Path("cartItemId") cartItemId: Int): Response<Any>


    // ================== Categories ==================
    @GET("api/Categories/products")
    suspend fun getProductCategories(): Response<List<String>>

    @GET("api/Categories/equipment")
    suspend fun getEquipmentCategories(): Response<List<String>>


    // ================== Coaches ==================
    @GET("api/Coaches/{id}")
    suspend fun getCoachById(@Path("id") id: Int): Response<Coach>

    @GET("api/Coaches")
    suspend fun getCoaches(): Response<List<Coach>>

    @GET("api/Coaches/{id}/availability")
    suspend fun getCoachAvailability(@Path("id") id: Int): Response<List<String>>

    @POST("api/Coaches/{id}/bookings")
    suspend fun createBooking(
        @Path("id") id: Int,
        @Body request: CreateBookingRequest
    ): Response<Any>


    // ================== Equipments ==================
    @GET("api/Equipments/{id}")
    suspend fun getEquipmentById(@Path("id") id: Int): Response<Equipment>

    @GET("api/Equipments")
    suspend fun getEquipments(@Query("category") category: String? = null): Response<List<Equipment>>


    // ================== Orders ==================
    @POST("api/Orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): Response<Any>

    @GET("api/Orders")
    suspend fun getOrders(): Response<List<Purchase>>

    @GET("api/Orders/{orderId}")
    suspend fun getOrderById(@Path("orderId") orderId: Int): Response<Purchase>


    // ================== Payments ==================
    @POST("api/Payments/orders/{orderId}/payment-intent")
    suspend fun createPaymentIntent(@Path("orderId") orderId: Int): Response<Any>


    // ================== Products ==================
    @GET("api/Products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @GET("api/Products/best-sellers")
    suspend fun getBestSellers(@Query("count") count: Int = 8): Response<List<Product>>

    @GET("api/Products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null
    ): Response<List<Product>>


    // ================== Reviews ==================
    @GET("api/Reviews/products/{productId}")
    suspend fun getProductReviews(@Path("productId") productId: Int): Response<List<Review>>

    @POST("api/Reviews/products/{productId}")
    suspend fun addProductReview(
        @Path("productId") productId: Int,
        @Body request: CreateReviewRequest
    ): Response<Any>

    @GET("api/Reviews/equipment/{equipmentId}")
    suspend fun getEquipmentReviews(@Path("equipmentId") equipmentId: Int): Response<List<Review>>

    @POST("api/Reviews/equipment/{equipmentId}")
    suspend fun addEquipmentReview(
        @Path("equipmentId") equipmentId: Int,
        @Body request: CreateReviewRequest
    ): Response<Any>
}

object APIClient {
    private const val BASE_URL = "http://gym-management-0.runasp.net/"
    private const val AI_BASE_URL = "http://gym-management-0.runasp.net/"
    const val AI_API_KEY: String? = null

    private var tokenManager: TokenManager? = null

    fun initialize(context: Context) {
        tokenManager = TokenManager(context.applicationContext)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor لإضافة التوكن أوتوماتيكياً
    private val authInterceptor = Interceptor { chain ->
        val requestBuilder = chain.request().newBuilder()

        val token = tokenManager?.let {
            runBlocking { it.tokenFlow.firstOrNull() }
        }

        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        chain.proceed(requestBuilder.build())
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    val api: PowerFuelApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PowerFuelApi::class.java)
    }

    val fitnessApi: PowerFuelApi by lazy {
        Retrofit.Builder()
            .baseUrl(AI_BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PowerFuelApi::class.java)
    }
}