package com.noatnoat.todoapp

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import com.noatnoat.musicapp.R
import com.noatnoat.musicapp.databinding.DialogEditTaskBinding
import com.noatnoat.todoapp.model.room.Task
import com.noatnoat.todoapp.viewmodel.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class EditTaskDialog(context: Context, private val viewModel: ViewModel) : Dialog(context) {
    private lateinit var binding: DialogEditTaskBinding

    fun show(task: Task) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogEditTaskBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        binding.task = task
        window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT

        val layoutParams = window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = layoutParams

        binding.btnUpdateTask.setOnClickListener {
            val updatedTask = task.copy(
                title = binding.taskTitle.text.toString(),
                description = binding.taskDescription.text.toString()
            )
            viewModel.update(updatedTask)
            dismiss()
        }

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val taskDueDate = binding.root.findViewById<TextView>(R.id.taskDueDate)
        taskDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    taskDueDate.setText(SimpleDateFormat("dd/MM/yyyy").format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        show()
    }
}