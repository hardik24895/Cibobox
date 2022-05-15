package com.eisuchi.eisuchi.data.network

import com.cibobox.app.data.modal.LoginRequest
import com.cibobox.app.data.modal.OrderListRequest
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class ApiHelper @Inject constructor (private val apiService: ApiService) {
    suspend fun  login(@Body body: LoginRequest) = apiService.login(body)
    suspend fun  logout(@Body body: RequestBody) = apiService.logout(body)
    suspend fun  order(@Field("userid")  id:Int) = apiService.order(id)
    suspend fun  orderComplete(@Field("order_id")  id:Int) = apiService.orderComplete(id)
    suspend fun  orderDetail(@Path("order_id") id :String) = apiService.orderDetail(id)

}