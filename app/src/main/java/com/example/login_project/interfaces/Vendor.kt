package com.example.login_project.interfaces


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Vendor {
    @GET("vendor.php")
    fun vendorData(@Query("pageid") pageId:String): Call<List<String>>
}