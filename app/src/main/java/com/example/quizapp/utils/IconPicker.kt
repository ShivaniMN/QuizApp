package com.example.quizapp.utils

import com.example.quizapp.R

object IconPicker {
    private val icons = arrayOf( R.drawable.ic_baseline_scoreboard_24, R.drawable.ic_baseline_science_24)
    var currentIcon = 0

    fun getIcon(): Int {
        currentIcon = (currentIcon + 1) % icons.size
        return icons[currentIcon]
    }
}