package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class OrderListModal(

    @field:SerializedName("data")
    val data: List<OrderDataItem> = mutableListOf(),
    @field:SerializedName("status")
    val status: Int = 0

) : Serializable


data class OrderDataItem(

    @field:SerializedName("table_room_no")
    val tableRoomNo: String? = null,

    @field:SerializedName("qr_type")
    val qrType: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("pending_items")
    val pendingItems: String? = null,

    @field:SerializedName("restaurant_id")
    val restaurantId: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("updated_date")
    val updatedDate: String? = null,

    @field:SerializedName("created_date")
    val createdDate: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("total_items")
    val totalItems: String? = null,

    @field:SerializedName("restaurant_name")
    val restaurantName: String? = null,

    @field:SerializedName("qr_code_id")
    val qrCodeId: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("enable_payment")
    val enablePayment: String? = null,

    @field:SerializedName("pay_later")
    val payLater: String? = null,
 @field:SerializedName("payment_method")
    val paymentMethod: String? = null,

    @field:SerializedName("payment_status")
    val paymentstatus: String? = null


) : Serializable
