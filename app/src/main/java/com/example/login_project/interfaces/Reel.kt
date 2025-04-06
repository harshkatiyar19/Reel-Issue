package com.example.login_project.interfaces

import com.example.login_project.holdrno
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Reel {
    @GET("reel.php")
    fun reelData(@Query("pageid") pageId:String, @Query("barcode") from:String): Call<holdrno>
}