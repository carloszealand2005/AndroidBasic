package com.example.myapplication.todo

data class Task(
    val id: Int = 0,
    val name: String,
    val category: TaskCategory,
    var isSelected: Boolean = false)