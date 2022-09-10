package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class LoginIntro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_intro)
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        if(auth.currentUser != null){
            Toast.makeText(this,"User is already loggedIn", Toast.LENGTH_SHORT).show()
            redirect("MAIN")
        }

        val loginIntro = findViewById<Button>(R.id.login_intro)
        loginIntro.setOnClickListener{
            redirect("LOGIN")
        }

    }

    private fun redirect(name: String){
        val intent: Intent = when(name){
            "LOGIN" -> Intent(this, Login::class.java)
            "MAIN" -> Intent(this, MainActivity::class.java)
            else -> throw Exception("no path exists")
        }
        startActivity(intent)
        finish()
    }
}