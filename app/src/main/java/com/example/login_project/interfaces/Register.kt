package com.example.login_project.interfaces


import com.example.login_project.data
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Register{
    @GET("register.php")
    fun enterUser(@Query("pageid") pageId:String,@Query("unid") userId:String,
                  @Query("emailid")emaiId:String, @Query("nameid") nameId:String): Call<data>
}