package com.example.login_project

import com.google.gson.annotations.SerializedName

data class data (
    @SerializedName("message")  val message: String,
    @SerializedName("status") val status: Boolean
)

data class holdInfo(val reelno:String,val barcode:String)

data class ApiResponse(
    @SerializedName("messageR1") val messageR1: String?,
    @SerializedName("statusR1")val statusR1: Boolean,
    @SerializedName("messageR2") val messageR2: String?,
    @SerializedName("statusR2")val statusR2: Boolean
)


data class holdrno(@SerializedName("reelno") val reelno:String)