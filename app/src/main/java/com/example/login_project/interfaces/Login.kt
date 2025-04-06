package com.example.login_project.interfaces

import com.example.login_project.data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Login {
    @GET("login.php")
    fun authenticate(@Query("pageid") pageId:String,@Query("unid") userId:String, @Query("passid") passwordId:String):Call<data>
}