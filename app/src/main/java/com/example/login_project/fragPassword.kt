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
import com.example.login_project.interfaces.passworQ
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class fragPassword:Fragment(){
@SuppressLint("SetTextI18n")
    private lateinit var passwordCheck: EditText
    private lateinit var password: EditText
    private lateinit var url:String
    private lateinit var receivedText:String
@SuppressLint("SetTextI18n")
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {
    val view = inflater.inflate(R.layout.fragment_password, container, false)
    val btnEnter: Button = view.findViewById(R.id.btnEnter)
    url = getString(R.string.ServerUrl)
    receivedText = arguments?.getString("key")?:"no input"
    passwordCheck= view.findViewById(R.id.etPassword2)
    password = view.findViewById(R.id.etPassword1)

    btnEnter.setOnClickListener {
        var indicator=0
        if(password.text.isEmpty()||passwordCheck.text.isEmpty()){indicator+=1}
        if (password.text.toString() != passwordCheck.text.toString()) {indicator+=2}
        check(indicator)
        password.setText("")
        passwordCheck.setText("")
    }
    return view
}

    private fun checkpassword(){
        val pwd=password.text.toString()
        val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
        if (passwordPattern.matches(pwd)){
            makepw(url, receivedText, pwd)
        }
        else{
            Toast.makeText(context,"Entered password doesn't meet criteria",Toast.LENGTH_SHORT).show()
        }
    }
    private fun check(value: Int) {
        when (value) {
            0 -> {
                checkpassword()
            }

            1 -> {
                Toast.makeText(context,"Please enter some password",Toast.LENGTH_SHORT).show()
            }

            2 -> {
                Toast.makeText(context,"Passwords don't match",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun makepw(url: String,user:String,pass:String) {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(passworQ::class.java)
        val encryptor=encrypt()
        val encryptPass=encryptor.sha256(pass)
        val req =request.input("password",user,encryptPass)
        req.enqueue(object: Callback<data?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<data?>, response: Response<data?>){
                val rb=response.body()!!

                Log.d("ANSWER", "$rb")
                if (rb.status) {
                    Toast.makeText(context,rb.message,Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity2::class.java)
                    startActivity(intent)
                }

            }
            override fun onFailure(call: Call<data?>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )

    }
}

