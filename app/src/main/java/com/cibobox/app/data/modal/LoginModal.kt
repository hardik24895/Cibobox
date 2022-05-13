package com.eisuchi.eisuchi.data.modal

import com.google.gson.annotations.SerializedName

data class LoginModal(

    @field:SerializedName("data")
    val data: LoginData? = null,

    @field:SerializedName("status")
    val status: Int = 0,


    )

data class LoginData(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("firstname")
    val firstname: String? = null,

    @field:SerializedName("access_type")
    val accessType: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("birthdate")
    val birthdate: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("sessionKey")
    val sessionKey: String? = null,

    @field:SerializedName("mobile")
    val mobile: String? = null,

    @field:SerializedName("postcode")
    val postcode: String? = null,

    @field:SerializedName("middlename")
    val middlename: String? = null,

    @field:SerializedName("roll_id")
    val rollId: String? = null,

    @field:SerializedName("lastname")
    val lastname: String? = null,

    @field:SerializedName("access_level")
    val accessLevel: String? = null,

    @field:SerializedName("photourl")
    val photourl: Any? = null,

    @field:SerializedName("suburb")
    val suburb: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("state")
    val state: String? = null,

    @field:SerializedName("roll_name")
    val rollName: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("msg")
    val msg: String? = null,

    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("title")
    val title: String? = null
)
