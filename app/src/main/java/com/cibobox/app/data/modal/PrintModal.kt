package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName

data class PrintModal(

	@field:SerializedName("data")
	val data: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
