package com.example.login_project

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.login_project.interfaces.Transporter
import com.example.login_project.interfaces.Truck
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class fragTransporter : Fragment() {
    private lateinit var transporter: MaterialAutoCompleteTextView
    private lateinit var truckno: MaterialAutoCompleteTextView


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.additional_fields, container, false)
        transporter = view.findViewById(R.id.sTransporter)
        truckno = view.findViewById(R.id.sTruckNo)
        val url=getString(R.string.ServerUrl)
        Transporter(url)

        return view
    }


    private fun Transporter(url: String){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(Transporter::class.java)
        val data = arguments?.getString("div")
        val req =request.transporterData("transporter", data.toString())
        req.enqueue(object: Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>){

                if (response.isSuccessful) {
                    val transporter_data = response.body() ?: emptyList()
                    val adapter2 = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,transporter_data)
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    transporter.setAdapter(adapter2)
                    transporter.setOnItemClickListener { _, _, position, _ ->
                        val TransporterS = transporter_data[position]
                        val sharedPreferences = activity?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()!!
                        editor.putString("transport", TransporterS)
                        editor.apply()
                        Truck(url)
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )
    }



    private fun Truck(url: String){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(Truck::class.java)
        val req =request.truckData("truck",transporter.text.toString())
        req.enqueue(object: Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>){

                if (response.isSuccessful) {
                    val truck_data = response.body() ?: emptyList()
                    val adapter2 = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,truck_data)
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    truckno.setAdapter(adapter2)
                    truckno.setOnItemClickListener { _, _, position, _ ->
                        val TruckNoS= truck_data[position]
                        val sharedPreferences = activity?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                        val editor = sharedPreferences?.edit()!!
                        editor.putString("truck", TruckNoS)
                        editor.apply()
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )
    }
}
