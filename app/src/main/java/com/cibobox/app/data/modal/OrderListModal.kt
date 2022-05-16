package com.cibobox.app.data.modal


import com.google.gson.annotations.SerializedName

data class OrderListModal(

	@field:SerializedName("msg")
	val msg: String? = null,
@field:SerializedName("total")
	val total: Int? = null,

	@field:SerializedName("result")
	val result: MutableList<OrderListData> = mutableListOf(),

	@field:SerializedName("success")
	val success: String? = null
)

data class User(

	@field:SerializedName("is_active")
	val isActive: String? = null,

	@field:SerializedName("mobile_verified_at")
	val mobileVerifiedAt: Any? = null,

	@field:SerializedName("is_verify")
	val isVerify: String? = null,

	@field:SerializedName("mobile_no")
	val mobileNo: String? = null,

	@field:SerializedName("last_name")
	val lastName: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: Any? = null,

	@field:SerializedName("device_type")
	val deviceType: String? = null,

	@field:SerializedName("forgot_token")
	val forgotToken: Any? = null,

	@field:SerializedName("user_type")
	val userType: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("device_token")
	val deviceToken: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("first_name")
	val firstName: Any? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class OrderListData(

	@field:SerializedName("contact_name")
	val contactName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("total_amount")
	val totalAmount: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("business_id")
	val businessId: Int? = null,

	@field:SerializedName("order_id")
	val orderId: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("contact_email")
	val contactEmail: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
