package com.example.myapplication.todo

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityTodoBinding
import com.google.android.material.snackbar.Snackbar

class TodoActivity : AppCompatActivity() {
    private val categoriesList = mutableListOf<TaskCategory>(
        TaskCategory.Personal,
        TaskCategory.Business,
        TaskCategory.Physical,
        TaskCategory.Other
    )

    private val tasksList = mutableListOf<Task>(
        Task(0, "PruebaBusiness", TaskCategory.Business),
        Task(1, "PruebaPersonal", TaskCategory.Personal),
        Task(2, "PruebaPhysical", TaskCategory.Physical),
        Task(3, "PruebaOther", TaskCategory.Other),
    )

    private lateinit var mBinding : ActivityTodoBinding
    private lateinit var categoriesAdapter : CategoryAdapter
    private lateinit var tasksAdapter : TasksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTodoBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initComponents()
    }
    private fun initComponents() {
        initListeners()
        initRecyclerViews()
    }



    private fun initRecyclerViews() {
        //Categories adapter:
        categoriesAdapter = CategoryAdapter(categoriesList) { position -> onItemCategorySelected(position)}
        mBinding.rvCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvCategory.adapter = categoriesAdapter

        //Tasks adapter:
        tasksAdapter = TasksAdapter(tasksList) { task -> onItemTaskSelected(task) }
        mBinding.rvTasks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvTasks.adapter = tasksAdapter

    }

    private fun initListeners() {
        mBinding.fabAddTask.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_task)

        val buttonCreate : Button = dialog.findViewById(R.id.buttonCreateTask)
        val etTask : EditText = dialog.findViewById(R.id.etTask)
        val radioGroupTasks : RadioGroup = dialog.findViewById(R.id.radioGroupCategories)

        buttonCreate.setOnClickListener {

            val taskName = etTask.text.toString().trim()
            if(taskName.isEmpty()){
                Snackbar.make(mBinding.root, "El nombre de la tarea no puede estar vacío", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val taskCategory = when(radioGroupTasks.checkedRadioButtonId) {
                R.id.rbBusiness -> TaskCategory.Business
                R.id.rbPersonal -> TaskCategory.Personal
                R.id.rbPhysical -> TaskCategory.Physical
                else -> TaskCategory.Other
            }

            val newTask = Task(tasksList.size, taskName, taskCategory)

            insertTask(newTask)
            dialog.hide()

        }

        dialog.show()

    }


    private fun onItemTaskSelected(task : Task){
        task.isSelected = !task.isSelected
        updateTasks()
    }

    private fun updateTasks() {
        //Si no hay ninguna filtrada, mostrar todas por defecto
        if(categoriesList.filter{!it.isSelected}.size == categoriesList.size){
            tasksAdapter.tasks = tasksList
            tasksAdapter.notifyDataSetChanged()
        }else {

            val categoriesSelected = categoriesList.filter { category -> category.isSelected }
            val filteredTasks = tasksList.filter { task ->
                categoriesSelected.contains(task.category)
            }

            tasksAdapter.tasks = filteredTasks
            tasksAdapter.notifyDataSetChanged()
        }

    }

    private fun insertTask(newTask: Task) {
        tasksList.add(newTask)
        updateTasks()
        //tasksAdapter.notifyItemInserted(tasksList.size - 1)
    }


    private fun onItemCategorySelected(position : Int){
        categoriesList[position].isSelected = !categoriesList[position].isSelected
        updateCategories()
        updateTasks()
    }

    private fun updateCategories(){ categoriesAdapter.notifyDataSetChanged() }


    private fun insertCategory(newCategory: TaskCategory) {
        categoriesList.add(newCategory)
        categoriesAdapter.notifyDataSetChanged()
        //tasksAdapter.notifyItemInserted(tasksList.size - 1)
    }





}