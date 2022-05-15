package com.cibobox.app.data.modal

import com.google.gson.annotations.SerializedName

data class LoginModal(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("result")
	val result: MutableList<ResultItem> = mutableListOf(),

	@field:SerializedName("success")
	val success: String? = null
)

data class ResultItem(

	@field:SerializedName("userid")
	val userid: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
