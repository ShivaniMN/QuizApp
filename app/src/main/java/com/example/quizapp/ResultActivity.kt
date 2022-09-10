package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.quizapp.models.Question
import com.example.quizapp.models.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class ResultActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var dbRef: DatabaseReference
    lateinit var user: FirebaseUser


    private lateinit var quiz: Quiz
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        setUpViews()

    }

    private fun setUpViews() {
        val quizData: String? = intent.getStringExtra("QUIZ")
        quiz = Gson().fromJson<Quiz>(quizData, Quiz::class.java)
        calculateScore()
    }


    private fun calculateScore() {
        var txtScore: TextView = findViewById(R.id.txtScore)
        var score = 0
        firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        var uId = user!!.uid
        var email = user.email.toString()
        var category = quiz.title

        for (entry: MutableMap.MutableEntry<String, Question> in quiz.questions.entries) {
            val question: Question = entry.value
            if (question.answer == question.userAnswer) {
                score += 1
            }
        }
        txtScore.text = "Your Score: $score"
        val finalScore = score.toString()
        saveFireStore(uId, email, category, finalScore)
    }

    private fun saveFireStore(uId: String, email: String, category: String, finalScore: String) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["UserID"] = uId
        user["Email"] = email
        user["Category"] = category
        user["Score"] = finalScore

        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Record added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this, "record failed to add", Toast.LENGTH_SHORT).show()
            }

    }
}