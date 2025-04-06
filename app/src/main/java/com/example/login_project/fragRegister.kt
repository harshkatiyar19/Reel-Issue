package com.example.login_project

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.login_project.interfaces.Register
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class fragRegister: Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        val username: EditText =view.findViewById(R.id.etUsername)
        val email: EditText =view.findViewById(R.id.etEmail1)
        val Name: EditText =view.findViewById(R.id.etName)
        val btnEnter: Button =view.findViewById(R.id.btnRegister)
        val btnExist: Button =view.findViewById(R.id.btnExistingAcc)
        val url:String = getString(R.string.ServerUrl)
        btnExist.setOnClickListener{
            val logFrag=fragLogin()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl,logFrag)
                .addToBackStack(null)
                .commit()
        }

        btnEnter.setOnClickListener{
            if(username.text.isNotEmpty()&&email.text.isNotEmpty()&&Name.text.isNotEmpty()){
               var indicator2=0
                if(!isValidEmail(email.text.toString())){
                   Toast.makeText(context,"Please Enter a valid email",Toast.LENGTH_SHORT).show()
                    email.setText("")
               }
                else{indicator2+=1}
                if(!isValidName(Name.text.toString())){
                    Toast.makeText(context,"Please Enter a valid name",Toast.LENGTH_SHORT).show()
                    Name.setText("")
                }
                else{indicator2+=2}

                if(indicator2==3){
                    val uname=username.text.toString()
                    val uemail=email.text.toString()
                    val na=Name.text.toString()
                    makeuser(url,uname,uemail,na)
                }
                username.setText("")
                email.setText("")
                Name.setText("")

            }
            else if(username.text.isEmpty()){
                Toast.makeText(context,"Please Enter a username",Toast.LENGTH_SHORT).show()
            }
            else if(email.text.isEmpty()){
                Toast.makeText(context,"Please Enter a email",Toast.LENGTH_SHORT).show()
            }
            else if(Name.text.isEmpty()){
                Toast.makeText(context,"Please Enter a name",Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }


    private fun makeuser(url: String,user:String,email:String,name:String) {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
        val request= Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(Register::class.java)

        val req =request.enterUser("register",user,email,name)
        req.enqueue(object: Callback<data?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<data?>, response: Response<data?>){
                val rb=response.body()!!
                if(rb.status){
                    Toast.makeText(context,rb.message,Toast.LENGTH_SHORT).show()
                    nextScreen(user)
                }
                else{
                    Toast.makeText(context,rb.message,Toast.LENGTH_SHORT).show()
                }

                Log.d("ANSWER", "$rb")

            }
            override fun onFailure(call: Call<data?>, t: Throwable) {
                Log.d("HEllO", "$t")
            }
        }
        )

    }

    private fun isValidName(name: String): Boolean {
        val namePattern = Regex("^[A-Za-z ]+$")
        return namePattern.matches(name)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3,}$")
        return emailPattern.matches(email)
    }

    private fun nextScreen(user: String) {
        val bundle = Bundle()
        bundle.putString("key", user)
        val passFrag=fragPassword()
        passFrag.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fl,passFrag)
            .addToBackStack(null)
            .commit()
    }
}



