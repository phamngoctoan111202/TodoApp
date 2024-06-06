package com.noatnoat.todoapp

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import com.noatnoat.musicapp.databinding.DialogNewTaskBinding
import com.noatnoat.todoapp.model.room.Task
import com.noatnoat.todoapp.viewmodel.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class NewTaskDialog(context: Context, private val viewModel: ViewModel) {
    private val binding: DialogNewTaskBinding = DialogNewTaskBinding.inflate(LayoutInflater.from(context))
    private var selectedDueDate: Long? = null

    init {
        binding.dueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDueDate = calendar.timeInMillis
                    binding.dueDate.text = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    fun show() {
        val dialog = AlertDialog.Builder(binding.root.context)
            .setTitle("New Task")
            .setView(binding.root)
            .setPositiveButton("Add", null) // null listener
            .setNegativeButton("Cancel", null) // null listener
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val newTask = Task(
                    title = binding.taskTitle.text.toString(),
                    description = binding.taskDescription.text.toString(),
                    createdDate = System.currentTimeMillis(),
                    dueDate = selectedDueDate
                )
                viewModel.insert(newTask)
                dialog.dismiss()
            }
        }

        dialog.show()
    }
}