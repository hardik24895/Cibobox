package com.eisuchi.eisuchi.data.network

import okhttp3.RequestBody
import retrofit2.http.Body
import javax.inject.Inject

class ApiHelper @Inject constructor (private val apiService: ApiService) {
    suspend fun  login(@Body body: RequestBody) = apiService.login(body)
    suspend fun  logout(@Body body: RequestBody) = apiService.logout(body)
    suspend fun  order(@Body body: RequestBody) = apiService.order(body)
    suspend fun  orderDetail(@Body body: RequestBody) = apiService.orderDetail(body)
    suspend fun  setDelivery(@Body body: RequestBody) = apiService.setDelivery(body)
    suspend fun  getBookingStatus(@Body body: RequestBody) = apiService.getBookingStatus(body)
    suspend fun  getPrintSlip(@Body body: RequestBody) = apiService.getPrintSlip(body)
    suspend fun  complateOrder(@Body body: RequestBody) = apiService.getPrintSlip(body)
}