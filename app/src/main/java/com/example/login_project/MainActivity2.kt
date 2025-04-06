package com.example.login_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.login_project.interfaces.Division
import com.example.login_project.interfaces.From
import com.example.login_project.interfaces.To
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class MainActivity2 : AppCompatActivity() {
    private lateinit var DivS:String
    private lateinit var FromS:String
    private lateinit var ToS:String
    private lateinit var division: MaterialAutoCompleteTextView
    private lateinit var from:MaterialAutoCompleteTextView
    private lateinit var to:MaterialAutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        division = findViewById(R.id.sDivision)
        from= findViewById(R.id.sFromWarehouse)
        to = findViewById(R.id.sToWarehouse)
        val next: Button = findViewById(R.id.btnNext)
        val url = getString(R.string.ServerUrl)
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            val f = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(f)
        }

        Division(url)
        next.setOnClickListener {
            if (to.text!=null){
                val f2 = Intent(this@MainActivity2, MainActivity3::class.java)
                f2.putExtra("Div",DivS)
                f2.putExtra("From",FromS)
                f2.putExtra("To",ToS)
                startActivity(f2)
                frameClear()
                division.text=null
                from.text=null
                to.text=null
            }
        }
    }

    private fun frameClear() {
        val fragMan= supportFragmentManager
        val fragTran = fragMan.findFragmentById(R.id.frag)
        if (fragTran != null) {
            fragMan.beginTransaction().remove(fragTran).commit()
        }
    }

    private fun frameChange(frag: Fragment, bundle: Bundle) {
        frag.arguments = bundle
        val fragMan= supportFragmentManager
        val fragTran=fragMan.beginTransaction()
        fragTran.replace(R.id.frag,frag)
        fragTran.commit()
    }

    private fun Division(url: String){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(Division::class.java)
        val req =request.divData("division")
        req.enqueue(object: Callback<List<String>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>){
                if (response.isSuccessful) {
                    val division_data = response.body() ?: emptyList()
                    val adapter1 = ArrayAdapter(this@MainActivity2,android.R.layout.simple_spinner_item, division_data)
                    adapter1.setDropDownViewResource(R.layout.customdropdown)
                    division.setAdapter(adapter1)
                    division.setOnItemClickListener { _, _, position, _ ->
                        DivS = division_data[position]
                        From(url)
                    }
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )
    }

    private fun From(url: String){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(From::class.java)
        val req =request.fromData("from",division.text.toString())
        req.enqueue(object: Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>){
                if (response.isSuccessful) {
                    val fromWarehouse_data = response.body() ?: emptyList()
                    val adapter2 = ArrayAdapter(this@MainActivity2,android.R.layout.simple_spinner_item,fromWarehouse_data)
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    from.setAdapter(adapter2)
                    from.setOnItemClickListener { _, _, position, _ ->
                        FromS= fromWarehouse_data[position]
                        To(url)
                    }
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )
    }


    private fun To(url: String){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(To::class.java)

        val req =request.toData("to",from.text.toString())
        req.enqueue(object: Callback<List<String>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>){
                if (response.isSuccessful) {
                    val toWarehouse_data = response.body() ?: emptyList()
                    val mutableToWarehouseData = toWarehouse_data.toMutableList()
                    val adapter = ArrayAdapter(this@MainActivity2,android.R.layout.simple_spinner_item, mutableToWarehouseData)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    to.setAdapter(adapter)
                    val bundle = Bundle().apply {
                        putString("div", division.text.toString())}
                    to.setOnItemClickListener { _, _, position, _ ->
                        ToS = mutableToWarehouseData[position]
                        when (ToS) {
                            "MACHINE" ->  frameChange(fragTransporter(),bundle)// Add the data you want to pass
                            "JOB" -> frameChange(fargVendor(),bundle)
                            else->frameClear()
                        }
                    }
                }
                else {
                    Log.e("ERROR", "Failed to fetch divisions: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )
    }

}