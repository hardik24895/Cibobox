package com.eisuchi.eisuchi.data.network


import com.eisuchi.eisuchi.data.modal.*
import com.google.gson.JsonElement
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // Login
    @POST("user")
    suspend fun login(@Body body: RequestBody): Response<LoginModal>

    // Login
    @POST("user")
    suspend fun logout(@Body body: RequestBody): Response<LogoutModal>

    // OrderList
    @POST("orders")
    suspend fun order(@Body body: RequestBody): Response<JsonElement>

    // Order Detail
    @POST("orders")
    suspend  fun orderDetail(@Body body: RequestBody):Response <OrderDetailModal>

    // Order Detail
    @POST("orders")
    suspend fun setDelivery(@Body body: RequestBody):Response<JsonElement>

    // Booking Status
    @POST("orders")
    suspend fun getBookingStatus(@Body body: RequestBody):Response<JsonElement>

 // Booking Status
    @POST("orders")
    suspend fun completeOrder(@Body body: RequestBody):Response<JsonElement>

 // Booking Status
   @POST("orders")
   suspend fun getPrintSlip(@Body body: RequestBody):Response<JsonElement>

}
