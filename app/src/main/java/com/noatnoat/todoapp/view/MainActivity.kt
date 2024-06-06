package com.noatnoat.todoapp.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.noatnoat.musicapp.R
import com.noatnoat.musicapp.databinding.ActivityMainBinding
import com.noatnoat.musicapp.databinding.DialogEditTaskBinding
import com.noatnoat.todoapp.EditTaskDialog
import com.noatnoat.todoapp.NewTaskDialog
import com.noatnoat.todoapp.constant.Priority
import com.noatnoat.todoapp.constant.TaskStatus
import com.noatnoat.todoapp.model.room.Task
import com.noatnoat.todoapp.view.adapter.TaskAdapter
import com.noatnoat.todoapp.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private val viewModel: ViewModel by viewModels()
    private lateinit var newTaskDialog: NewTaskDialog
    private lateinit var editTaskDialog: EditTaskDialog

    @SuppressLint("NotifyDataSetChanged")
    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        taskAdapter = TaskAdapter(listOf(), this) // Initialize with empty list
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = taskAdapter

        viewModel.tasks.observe(this) { tasks ->
            taskAdapter.tasks = tasks
            taskAdapter.notifyDataSetChanged()

            if (tasks.isEmpty()) {
                binding.emptyTasksImageView.visibility = View.VISIBLE
                binding.emptyTasksTextView.visibility = View.VISIBLE
            } else {
                binding.emptyTasksImageView.visibility = View.GONE
                binding.emptyTasksTextView.visibility = View.GONE
            }
        }


        binding.fabAddTask.setOnClickListener {
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_new_task, null)

            val taskTitle = dialogView.findViewById<EditText>(R.id.taskTitle)
            val taskDescription = dialogView.findViewById<EditText>(R.id.taskDescription)
            val taskDueDate = dialogView.findViewById<TextView>(R.id.dueDate)
            val taskPriority = dialogView.findViewById<TextView>(R.id.priority)
            val taskIsRepeating = dialogView.findViewById<Switch>(R.id.taskIsRepeating)

            var selectedDueDate: Long? = null
            var selectedPriority = Priority.MEDIUM
            taskPriority.text = selectedPriority.name

            taskDueDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val datePickerDialog = DatePickerDialog(
                    this,
                    R.style.DatePickerDialogTheme,
                    { _, year, month, dayOfMonth ->
                        calendar.set(year, month, dayOfMonth)
                        selectedDueDate = calendar.timeInMillis
                        taskDueDate.text = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.show()
            }

            taskPriority.setOnClickListener {
                selectedPriority = when (selectedPriority) {
                    Priority.MEDIUM -> Priority.HIGH
                    Priority.HIGH -> Priority.LOW
                    Priority.LOW -> Priority.MEDIUM
                }
                taskPriority.text = selectedPriority.name
            }

            val dialog = AlertDialog.Builder(this)
                .setTitle("Tạo nhiệm vụ mới")
                .setView(dialogView)
                .setPositiveButton("Thêm", null) // null listener
                .setNegativeButton("Hủy", null) // null listener
                .create()

            dialog.setOnShowListener {
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    if (taskTitle.text.toString().isBlank() || taskDescription.text.toString().isBlank() || selectedDueDate == null) {
                        Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    } else {
                        val newTask = Task(
                            title = taskTitle.text.toString(),
                            description = taskDescription.text.toString(),
                            priority = selectedPriority,
                            isRepeating = taskIsRepeating.isChecked,
                            createdDate = System.currentTimeMillis(),
                            dueDate = selectedDueDate
                        )
                        viewModel.insert(newTask)
                        dialog.dismiss()
                    }
                }
                positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))

                val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                negativeButton.setOnClickListener {
                    dialog.dismiss()
                }
                negativeButton.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }

            dialog.show()
        }
    }

    override fun onTaskClick(task: Task) {
        val dialog = Dialog(this, R.style.FullWidth_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        val inflater = LayoutInflater.from(this)
        val binding: DialogEditTaskBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_task, null, false)
        dialog.setContentView(binding.root)

        binding.task = task
        dialog.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT

        val layoutParams = dialog.window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams

        val dueDate = task.dueDate
        if (dueDate != null) {
            val sdf = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            val dueDateString = sdf.format(Date(dueDate))
            binding.taskDueDate.text = dueDateString
        }

        val taskStatus = binding.root.findViewById<TextView>(R.id.taskStatus)
        taskStatus.setOnClickListener {
            task.status = when (task.status) {
                TaskStatus.START -> TaskStatus.PROGRESS
                TaskStatus.PROGRESS -> TaskStatus.START
                TaskStatus.COMPLETED -> TaskStatus.START
            }
            taskStatus.text = task.status.toString()
        }

        val taskPriority = binding.root.findViewById<TextView>(R.id.taskPriority)
        taskPriority.setOnClickListener {
            task.priority = when (task.priority) {
                Priority.HIGH -> Priority.MEDIUM
                Priority.MEDIUM -> Priority.LOW
                Priority.LOW -> Priority.HIGH
            }
            taskPriority.text = task.priority.toString()
        }


        binding.btnUpdateTask.setOnClickListener {
            val updatedTask = task.copy(
                title = binding.taskTitle.text.toString(),
                description = binding.taskDescription.text.toString(),
                status = task.status,
                dueDate = task.dueDate,
                priority = task.priority
            )
            viewModel.update(updatedTask)
            dialog.dismiss()
        }

        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.BOTTOM)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()

        val taskDueDate = binding.root.findViewById<TextView>(R.id.taskDueDate)
        taskDueDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                this,
                R.style.DatePickerDialogTheme,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    task.dueDate = calendar.timeInMillis
                    taskDueDate.text = SimpleDateFormat("dd/MM/yy").format(calendar.time)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }



    override fun onDeleteClick(task: Task) {
        viewModel.delete(task)
    }

}