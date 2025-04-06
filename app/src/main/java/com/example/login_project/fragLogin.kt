package com.example.login_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.login_project.interfaces.Login
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class fragLogin : Fragment(){
    private lateinit var username:EditText
    private lateinit var password:EditText
    private lateinit var url:String
    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val btnEnter:Button= view.findViewById(R.id.btnEnter1)
        username=view.findViewById(R.id.etUsername)
        password =view.findViewById(R.id.etPassword1)
        url = getString(R.string.ServerUrl)

        btnEnter.setOnClickListener {
            if(username.text.isEmpty()&&password.text.isEmpty()){
                Toast.makeText(context,"Please enter a username and password", Toast.LENGTH_SHORT).show()
            }
            else if(username.text.isEmpty()){
                Toast.makeText(context,"Please enter a username", Toast.LENGTH_SHORT).show()
            } else if(password.text.isEmpty()){
                Toast.makeText(context,"Please enter a password", Toast.LENGTH_SHORT).show()
            }
            else{
                authenticate(url,username.text.toString(),password.text.toString())
            }
        }
        return view
    }

    private fun authenticate(url: String,user:String,pass:String){
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(Login::class.java)
        val encryptor=encrypt()
        val encryptPass=encryptor.sha256(pass)
        val req =request.authenticate("login",user,encryptPass)
        req.enqueue(object: Callback<data?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<data?>, response: Response<data?>){
                val rb=response.body()!!
                if(rb.status){
                    Toast.makeText(context,rb.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity2::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(context,rb.message, Toast.LENGTH_SHORT).show()
                }
                Log.d("ANSWER", "$rb")
            }
            override fun onFailure(call: Call<data?>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )
    }
}
