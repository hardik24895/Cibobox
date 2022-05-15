package com.eisuchi.eisuchi.data.network


import com.cibobox.app.data.modal.*
import com.eisuchi.eisuchi.data.modal.*
import com.google.gson.JsonElement
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Login
    @POST("user/login_api")
    suspend fun login(@Body body: LoginRequest): Response<LoginModal>

    // Login
    @POST("user")
    suspend fun logout(@Body body: RequestBody): Response<LogoutModal>

    // OrderList
    @FormUrlEncoded
    @POST("my_order_api")
    suspend fun order(@Field("userid")  userName:Int): Response<OrderListModal>

    // Order Detail
    @GET("orderdetail_api/{order_id}")
    suspend  fun orderDetail(@Path("order_id") id :String):Response <OrderDetailModal>

    // Order Complete
    @FormUrlEncoded
    @POST("serve_api")
    suspend fun orderComplete(@Field("order_id")  userName:Int): Response<OrderCompleteModal>


}
