package com.example.myapplication.todo

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemTaskBinding

class TasksAdapter(var tasks : List<Task>, private val onItemSelected : (Task) -> Unit)
    : RecyclerView.Adapter<TasksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TasksViewHolder(view)
    }

    override fun getItemCount() : Int = tasks.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.render(tasks[position], onItemSelected)
    }

}

class TasksViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    val binding = ItemTaskBinding.bind(view)
    private val context = binding.root.context

    fun render(task: Task, onItemSelected : (Task) -> Unit) {

        itemView.setOnClickListener { onItemSelected(task) }
        binding.checkBoxTask.setOnClickListener { onItemSelected(task) }

        binding.tvTask.text = task.name
        binding.checkBoxTask.isChecked = task.isSelected
        if(task.isSelected) {
            binding.tvTask.paintFlags = binding.tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }else{
            binding.tvTask.paintFlags = binding.tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        val checkBoxColor = when(task.category){
            TaskCategory.Business -> R.color.todo_business_category
            TaskCategory.Personal -> R.color.todo_personal_category
            TaskCategory.Physical -> R.color.todo_physical_category
            TaskCategory.Other -> R.color.todo_other_category
        }

        binding.checkBoxTask.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(context, checkBoxColor)
        )

    }

}
