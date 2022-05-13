package com.eisuchi.eisuchi.data.repository

import com.eisuchi.eisuchi.data.network.ApiHelper
import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor (private val apiHelper: ApiHelper) {
    suspend fun  login(@Body body: RequestBody) = apiHelper.login(body)
    suspend fun  logout(@Body body: RequestBody) = apiHelper.logout(body)
    suspend fun  oder(@Body body: RequestBody) = apiHelper.order(body)
    suspend fun  oderDetail(@Body body: RequestBody) = apiHelper.orderDetail(body)
    suspend fun  setDeliver(@Body body: RequestBody) = apiHelper.setDelivery(body)
    suspend fun  getBookingStatus(@Body body: RequestBody) = apiHelper.getBookingStatus(body)
    suspend fun  getPrintSlip(@Body body: RequestBody) = apiHelper.getPrintSlip(body)
    suspend fun  complateOrder(@Body body: RequestBody) = apiHelper.getPrintSlip(body)
}