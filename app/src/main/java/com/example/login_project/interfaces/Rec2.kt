package com.example.login_project.interfaces

import com.example.login_project.ApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Rec2 {
        @GET("rec2.php")
        fun recordsData2(
            @Query("pageid") pageId:String,
            @Query("reelno") reelno:String,
            @Query("barcode") barcode:String,
            @Query("from") from:String,
            @Query("to") to:String,
            @Query("date") date:String,
            @Query("truck") truck:String,
            @Query("vendor") vendor:String): Call<ApiResponse>

}