package com.example.login_project

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login_project.interfaces.Records
import com.example.login_project.interfaces.Reel
import com.google.android.material.appbar.MaterialToolbar

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


class MainActivity3 : AppCompatActivity() {
    private lateinit var recycle: RecyclerView
    private lateinit var adep:customAdapter
    private val dl= mutableListOf<holdInfo>()
    private lateinit var tab2:TableLayout
    private lateinit var data:MutableList<holdInfo>
    private lateinit var scannedData:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        val url = getString(R.string.ServerUrl)
        tab2=findViewById(R.id.tl2)
        val fromData:TextView=findViewById(R.id.tvFromData)
        val toData:TextView=findViewById(R.id.tvToData)
        val date:TextView=findViewById(R.id.tvDateData)
        val transporterData:TextView=findViewById(R.id.tvTransporterData)
        scannedData=findViewById(R.id.tvScannedData)
        val jobData:TextView=findViewById(R.id.tvJobData)
        val scan:Button=findViewById(R.id.btnScan)
        val submit:Button=findViewById(R.id.btnSubmit)
        recycle=findViewById(R.id.recycleview)
        var status=false
        var Transporter=""
        var TruckNo=""
        var Vendor=""
        val intent = intent
        if (intent == null) {
            Log.e("MainActivity3", "Intent is null")
            return
        }
        val From=intent.getStringExtra("From") ?: ""
        fromData.text = From
        val To=intent.getStringExtra("To") ?: ""
        toData.text = To
        val toolbar: MaterialToolbar = findViewById(R.id.topAppBar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            val f = Intent(this@MainActivity3, MainActivity2::class.java)
            startActivity(f)
        }
        if(To=="MACHINE"||To=="JOB"){
            val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            status=true
            Transporter = sharedPreferences.getString("transport","").toString()
            transporterData.text=Transporter
            TruckNo = sharedPreferences.getString("truck","").toString()
            Vendor = sharedPreferences.getString("vendor","").toString()
        }

        adep = customAdapter(this@MainActivity3, dl)
        recycle.adapter = adep
        recycle.layoutManager = LinearLayoutManager(this@MainActivity3)
        data = adep.getData()

        val curdate=getCurrentDate()
        date.text=curdate
        jobData.text=0.toString()
        scan.setOnClickListener {
            MainActivity4.startScanner(this){barcodes->
                barcodes.forEach{barcode ->
                    val bar=barcode.rawValue.toString()

                    getReelno(url,bar)
                }
            }
        }
        submit.setOnClickListener {
            addrecords(url,status,fromData.text.toString(),toData.text.toString(),curdate,TruckNo,Vendor)
            val handler = Handler(Looper.getMainLooper())
            val runnable = Runnable {
                val f = Intent(this@MainActivity3, MainActivity2::class.java)
                Toast.makeText(this@MainActivity3,"Data entered", Toast.LENGTH_SHORT).show()
                startActivity(f)
            }
            handler.postDelayed(runnable, 2000)
        }
    }

    private fun getReelno(url: String, bar: String){
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Reel::class.java)

        val req =request.reelData("reel",bar)
        req.enqueue(object: Callback<holdrno> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<holdrno>, response: Response<holdrno>) {
                if (response.isSuccessful) {
                    val rb= response.body()
                    val Rno= rb?.reelno!!
                    addrow(Rno, bar)
                }
                else {
                    Log.e("ERROR", "Failed to fetch divisions: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<holdrno>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )
    }

    private fun addrecords(url: String, status: Boolean, From: String, To: String, curdate: String, TruckNo: String, Vendor: String) {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val request = retrofit.create(Records::class.java)

        for (i in 0  until data.size) {
            val holdInfo = data[i]
            val call = if (!status) {
                request.recordsData("records", holdInfo.reelno, holdInfo.barcode, From, To, curdate)
            } else {
                request.recordsData("rec2",holdInfo.reelno, holdInfo.barcode, From, To, curdate, TruckNo, Vendor)
            }
            call.enqueue(object : Callback<ApiResponse> {
                override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                    val rb = response.body()!!
                    if(rb.statusR2){
                        Toast.makeText(this@MainActivity3,rb.messageR2, Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(this@MainActivity3,rb.messageR1, Toast.LENGTH_SHORT).show()
                    Log.d("ANSWER", "$rb")
                }
                override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                    Log.d("ERROR", "API call failed: ${t.message}")
                }
            })
        }
    }

    private fun getCurrentDate(): String {
        val currentDateTime = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDateTime.format(formatter)
    }
    private fun addrow(Rno:String,Bar:String){
        val newItem = holdInfo(Rno, Bar)
        dl.add(newItem)
        adep.notifyItemInserted(dl.size - 1)
        scannedData.text= adep.itemCount.toString()
    }
}