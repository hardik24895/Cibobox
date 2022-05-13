package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName

data class DeliveryModal(

    @field:SerializedName("data")
     val data: List<DeliveryDataItem> = mutableListOf(),

    @field:SerializedName("status")
    val status: Int = 0
)

data class DeliveryDataItem(

    @field:SerializedName("delivered_by")
    val deliveredBy: String? = null,

    @field:SerializedName("delivered_qty")
    val deliveredQty: String? = null,

    @field:SerializedName("item_id")
    val itemId: String? = null,

    @field:SerializedName("added_date")
    val addedDate: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("delivered_time")
    val deliveredTime: String? = null,

    @field:SerializedName("qty")
    val qty: String? = null,

    @field:SerializedName("item_name")
    val itemName: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("rest_order_id")
    val restOrderId: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

)
