package com.cibobox.app.data.modal

import com.google.gson.annotations.SerializedName

data class OrderCompleteModal(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("success")
	val success: Int? = null
)
