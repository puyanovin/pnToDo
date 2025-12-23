
package com.example.pn_todo

data class Task(
    val id: Int,
    val title: String,
    val isCompleted: Boolean = false
)
