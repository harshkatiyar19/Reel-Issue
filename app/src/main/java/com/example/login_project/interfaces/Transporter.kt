package com.example.login_project.interfaces


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Transporter {
    @GET("transporter.php")
    fun transporterData(@Query("pageid") pageId:String, @Query("division") division:String): Call<List<String>>
}