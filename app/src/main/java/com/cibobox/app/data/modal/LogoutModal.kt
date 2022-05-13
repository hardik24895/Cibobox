package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName

data class LogoutModal(

	@field:SerializedName("data")
	val data: List<Any?>? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
