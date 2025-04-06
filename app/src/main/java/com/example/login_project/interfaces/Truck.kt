package com.example.login_project.interfaces


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface Truck {
    @GET("truck.php")
    fun truckData(@Query("pageid") pageId:String, @Query("transporter") transporter:String): Call<List<String>>
}