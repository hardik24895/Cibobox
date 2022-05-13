package com.eisuchi.eisuchi.uitils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

object RetrofitRequestBody {

    fun wrapParams(params: String): RequestBody {
        return params
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
    }

    /* fun wrapParams(params: String): RequestBody {
         return params
             .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
     }*/

    fun setParentJsonData(
        methodName: String,
        jsonBody: JSONObject
    ): String {
        val jsonObject = JSONObject()
        try {
            jsonObject.put(Constant.ACTION, methodName)
            jsonObject.put(Constant.PARAMS, jsonBody)
            Logger.d("Request::::> $jsonObject")
            return jsonObject.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject.toString()
    }
}