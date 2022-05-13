package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NotificationModal2(

	@field:SerializedName("ops")
	val ops: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("order")
	val order: String? = null
):Serializable


