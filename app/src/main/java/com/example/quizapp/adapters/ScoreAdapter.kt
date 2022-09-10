package com.example.quizapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizapp.R
import com.example.quizapp.models.User

class ScoreAdapter(private val userList: ArrayList<User>): RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder>() {

    inner class ScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val email: TextView = itemView.findViewById(R.id.email)
        val category: TextView = itemView.findViewById(R.id.category)
        val score: TextView = itemView.findViewById(R.id.score)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScoreAdapter.ScoreViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.individual_data, parent, false)
        return ScoreViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ScoreAdapter.ScoreViewHolder, position: Int) {
        val user: User = userList[position]
        holder.email.text = user.Email
        holder.category.text = user.Category
        holder.score.text = user.Score
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}