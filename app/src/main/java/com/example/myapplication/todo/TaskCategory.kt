package com.example.myapplication.todo

sealed class TaskCategory(var isSelected : Boolean = false) {
    object Personal : TaskCategory()
    object Business : TaskCategory()
    object Physical : TaskCategory()
    object Other : TaskCategory()

}