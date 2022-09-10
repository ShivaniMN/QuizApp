package com.example.quizapp.utils

object ColorPicker {

    private val colors = arrayOf("#D36280", "#8188CA", "#7D659F", "#E3AD17")
    var currentColor = 0

    fun getColor(): String {
        currentColor = (currentColor + 1) % colors.size
        return colors[currentColor]
    }
}