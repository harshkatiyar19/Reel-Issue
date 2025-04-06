package com.example.login_project.interfaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Division {
    @GET("division.php")
    fun divData(@Query("pageid") pageId:String): Call<List<String>>
}
