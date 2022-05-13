package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName

data class ComplateOrderModal(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("message")
	val message: String? = null
)
