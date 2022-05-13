package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName

data class OrderDetailModal(

    @field:SerializedName("data")
    val data: OrderData? = null,

    @field:SerializedName("status")
    val status: Int = 0
)

data class OrderData(

    @field:SerializedName("qr_type")
    val qrType: String? = null,

    @field:SerializedName("additem_url")
    val additemUrl: String? = null,

    @field:SerializedName("restaurant_id")
    val restaurantId: String? = null,

    @field:SerializedName("total_items")
    val totalItems: String? = null,

    @field:SerializedName("restaurant_name")
    val restaurantName: String? = null,

    @field:SerializedName("table_room_no")
    val tableRoomNo: String? = null,

    @field:SerializedName("user_id")
    val userId: String? = null,

    @field:SerializedName("pending_items")
    val pendingItems: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("updated_date")
    val updatedDate: String? = null,

    @field:SerializedName("created_date")
    val createdDate: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("items")
    val items: List<OrderItems> = mutableListOf(),

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("code")
    val code: Int? = null,

  /*  @field:SerializedName("additem_url")
    val additemUrl: Int? = null,*/

    @field:SerializedName("title")
    val title: String? = null
)

data class OrderItems(

    @field:SerializedName("delivered_by")
    val deliveredBy: String? = null,

    @field:SerializedName("item_id")
    val itemId: String? = null,

    @field:SerializedName("added_date")
    val addedDate: String? = null,

    @field:SerializedName("image_url")
    val imageUrl: String? = null,

    @field:SerializedName("item_name")
    val itemName: String? = null,

    @field:SerializedName("delivered_qty")
    val deliveredQty: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("delivered_time")
    val deliveredTime: String? = null,

    @field:SerializedName("qty")
    val qty: String? = null,

    @field:SerializedName("item_image")
    val itemImage: Any? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("rest_order_id")
    val restOrderId: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    var isSelected: Boolean = false
)
