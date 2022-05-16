package com.cibobox.app.data.modal

import com.google.gson.annotations.SerializedName

data class OrderDetailModal(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("success")
	val success: String? = null
)

data class Result(

	@field:SerializedName("totalprice")
	val totalprice: String? = null,

	@field:SerializedName("mobile_no")
	val mobileNo: String? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("items")
	val items: MutableList<OrderIteams> = mutableListOf(),

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("first_name")
	val name: String? = null
)

data class OrderIteams(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("special_detail")
	val instra: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("name")
	val name: String? = null
)
