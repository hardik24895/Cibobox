package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName

data class BaseModal(

	@field:SerializedName("data")
	 val error: Error? = null,

	@field:SerializedName("status")
	val status: Int = 0
)

 data class Error(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)
