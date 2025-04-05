package com.example.myapplication.todo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemTaskCategoryBinding

class CategoryAdapter(private val categories : List<TaskCategory>, private val onItemSelected : (Int) -> Unit)
    : RecyclerView.Adapter<CategoriesViewHolder>(){

    /*
    Crear el ViewHolder, este metodo 'override' debe retornar el viewHolder con su respectiva View
    El View es el objeto que nos permite pintar en pantalla. En este caso es el item_task_category.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task_category, parent, false)
        return CategoriesViewHolder(view)
    }


    override fun getItemCount() = categories.size

    // Aquí se ejecuta en bucle por cada uno de los elementos del RecyclerView
    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.render(categories[position], onItemSelected)

    }
}

class CategoriesViewHolder(view : View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemTaskCategoryBinding.bind(view)
    private val context = binding.root.context
    fun render(taskCategory: TaskCategory, onItemSelected : (Int) -> Unit) {

        itemView.setOnClickListener { onItemSelected(layoutPosition) }

        when(taskCategory){
            TaskCategory.Business -> {
                binding.tvCategoryName.text = "Negocios"
                binding.categoryDivider.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.todo_business_category))
            }

            TaskCategory.Personal -> {
                binding.tvCategoryName.text = "Personal"
                binding.categoryDivider.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.todo_personal_category)
                )
            }
            TaskCategory.Physical -> {
                binding.tvCategoryName.text = "Fisico"
                binding.categoryDivider.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.todo_physical_category)
                )
            }
            TaskCategory.Other -> {
                binding.tvCategoryName.text = "Otros"
                binding.categoryDivider.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.todo_other_category)
                )
            }
        }
        val color = if(taskCategory.isSelected){
            R.color.todo_background_enabled
        }else{
            R.color.todo_background_card
        }
        binding.cardViewCategory.setCardBackgroundColor(ContextCompat.getColor(context, color))

    }
}

