package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SignUp : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()
        val signup = findViewById<Button>(R.id.signUp)
        val btnLog = findViewById<TextView>(R.id.btnLog)
        signup.setOnClickListener {
            signUpUser()
        }

        btnLog.setOnClickListener{
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun signUpUser() {
        val email: String = findViewById<EditText>(R.id.etEmailAddress).text.toString()
        val password: String = findViewById<EditText>(R.id.etPassword).text.toString()
        val confirmPassword: String = findViewById<EditText>(R.id.etConfirmPassword).text.toString()
        var intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("email", email)

        if(email.isBlank() || password.isBlank() || confirmPassword.isBlank()){
            Toast.makeText(this, "Email and Password cant be Blank", Toast.LENGTH_SHORT).show()
            return
        }

        if(password != confirmPassword){
            Toast.makeText(this,"Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
            return
        }
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this,"Error Creating User", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


