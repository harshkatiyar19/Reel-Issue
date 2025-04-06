package com.example.login_project

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val btnLogScr: Button =findViewById(R.id.btnLogin)
        val btnRegScr: Button =findViewById(R.id.btnRegister)

        frameChange(fragLogin())
        btnLogScr.setOnClickListener {
            frameChange(fragLogin())
        }

        btnRegScr.setOnClickListener {
            frameChange(fragRegister())
        }
    }

    private fun frameChange(frag: Fragment) {
        val fragMan= supportFragmentManager
        val fragTran=fragMan.beginTransaction()
        fragTran.replace(R.id.fl,frag)
        fragTran.commit()
    }
}

