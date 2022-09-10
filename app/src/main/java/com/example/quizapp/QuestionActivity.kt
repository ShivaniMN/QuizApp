package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.adapters.OptionAdapter
import com.example.quizapp.models.Question
import com.example.quizapp.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

class QuestionActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer
    var quizzes: MutableList<Quiz>? = null
    var questions: MutableMap<String, Question>? = null
    var index = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        setUpFirestore()
        countdown()
        setUpEventListener()
    }

    private fun countdown() {
        var countDown: TextView = findViewById(R.id.countDown)
//        var duration: Long = TimeUnit.SECONDS.toMillis(15)


         countDownTimer = object : CountDownTimer(15000, 1000){
            override fun onTick(millisUnitFinished: Long) {
                countDown.text = (millisUnitFinished / 1000).toString()

            }

            override fun onFinish() {
                    index++
                    if(index <= questions!!.size){
                        bindViews()
                        countDownTimer.start()
                    }else {
                        gameResult()
                        finish()
                    }
            }
        }.start()
    }

    private fun restTimer() {
        countDownTimer.cancel()
    }

    private fun setUpEventListener() {
        var btnPrevious: Button = findViewById(R.id.btnPrevious)
        var btnSubmit : Button = findViewById(R.id.btnSubmit)
        var btnNext :Button = findViewById(R.id.btnNext)
        var countDown: TextView = findViewById(R.id.countDown)
        btnPrevious.setOnClickListener{
            index--
            countDownTimer.start()
            bindViews()
        }
        btnNext.setOnClickListener{
            index++
            bindViews()
            restTimer()
            countdown()
        }
        btnSubmit.setOnClickListener{
            Log.d("FINALQUIZ", questions.toString())
            countDownTimer.cancel()
            gameResult()
            finish()
        }
    }

    private fun setUpFirestore(){
        val fireStore = FirebaseFirestore.getInstance()
        val title = intent.getStringExtra("TITLE")
        if(title != null){
            fireStore.collection("quizzes").whereEqualTo("title", title)
                .get()
                .addOnSuccessListener {
                    if(it != null && !it.isEmpty){
                        quizzes = it.toObjects(Quiz::class.java)
                        questions = quizzes!![0].questions
                        bindViews()
                    }
                }
        }
    }
    private fun gameResult(){
        val intent = Intent(this, ResultActivity::class.java)
        val json = Gson().toJson(quizzes!![0])
        intent.putExtra("QUIZ", json)
        startActivity(intent)
    }
    private fun bindViews() {
        var btnPrevious: Button = findViewById(R.id.btnPrevious)
        var btnSubmit : Button = findViewById(R.id.btnSubmit)
        var btnNext :Button = findViewById(R.id.btnNext)
        var optionList: RecyclerView = findViewById(R.id.optionList)
        var description: TextView = findViewById(R.id.description)

        btnPrevious.visibility = View.GONE
        btnSubmit.visibility = View.GONE
        btnNext.visibility = View.GONE

        if(index == 1){
            btnNext.visibility = View.VISIBLE
        }
        else if(index == questions!!.size){
            btnSubmit.visibility = View.VISIBLE
            btnPrevious.visibility = View.VISIBLE
        }
        else {
            btnPrevious.visibility = View.VISIBLE
            btnNext.visibility = View.VISIBLE
        }

        val question = questions!!["question$index"]
        question?.let {
            description.text = it.description
            val optionAdapter = OptionAdapter(this, it)
            optionList.layoutManager = LinearLayoutManager(this)
            optionList.adapter = optionAdapter
            optionList.setHasFixedSize(true)
        }

    }
}