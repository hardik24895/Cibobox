package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName

data class BookingStatusModal(

	@field:SerializedName("data")
	val data: List<BookingDataItem> = mutableListOf(),

	@field:SerializedName("status")
	val status: Int? = null
)

data class BookingDataItem(

	@field:SerializedName("qr_type")
	val qrType: String? = null,

	@field:SerializedName("qr_number")
	val qrNumber: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)
