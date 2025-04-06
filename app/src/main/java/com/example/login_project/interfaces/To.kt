package com.example.login_project.interfaces



import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface To {
    @GET("to.php")
    fun toData(@Query("pageid") pageId:String,@Query("from") from:String): Call<List<String>>
}