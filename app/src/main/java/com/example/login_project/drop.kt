//package com.example.login_project
//
//import android.annotation.SuppressLint
//import android.util.Log
//import android.widget.ArrayAdapter
//import com.google.android.material.textfield.MaterialAutoCompleteTextView
//import okhttp3.OkHttpClient
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import java.util.concurrent.TimeUnit
//
//open class drop {
//
//
//     fun Division(url: String,pageid:String,dropdown: MaterialAutoCompleteTextView,hue:String):String {
//        val okHttpClient = OkHttpClient.Builder()
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .build()
//        val request = Retrofit.Builder()
//            .baseUrl(url)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build()
//            .create(com.example.login_project.interfaces.Division::class.java)
//        val req = request.divData(pageid)
//        req.enqueue(object : Callback<List<String>> {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
//                if (response.isSuccessful) {
//                    val division_data = response.body() ?: emptyList()
//                    val adapter1 = ArrayAdapter(this@drop, android.R.layout.simple_spinner_item, division_data)
//                    adapter1.setDropDownViewResource(R.layout.customdropdown)
//                    dropdown.setAdapter(adapter1)
//                    dropdown.setOnItemClickListener { _, _, position, _ ->
//                        return@setOnItemClickListener division_data[position]
//
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<String>>, t: Throwable) {
//                Log.d("HEllO", "$t")
//            }
//        }
//        )
//    }
//}