package com.eisuchi.eisuchi.data.repository

import com.cibobox.app.data.modal.LoginRequest
import com.cibobox.app.data.modal.OrderListRequest
import com.eisuchi.eisuchi.data.network.ApiHelper
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor (private val apiHelper: ApiHelper) {
    suspend fun  login(@Body body: LoginRequest) = apiHelper.login(body)
    suspend fun  logout(@Body body: RequestBody) = apiHelper.logout(body)
    suspend fun  oder(@Field("userid")  id:Int, @Field("pageno")  pageno:Int) = apiHelper.order(id, pageno)
    suspend fun  oderDetail(@Path("order_id") id :String) = apiHelper.orderDetail(id)
    suspend fun  orderComplete(@Field("order_id")  id:Int) = apiHelper.orderComplete(id)
}