package com.noatnoat.todoapp.view.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import com.noatnoat.musicapp.databinding.ItemTaskBinding
import com.noatnoat.musicapp.databinding.ItemTextBinding
import com.noatnoat.todoapp.constant.TaskStatus
import com.noatnoat.todoapp.model.room.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(var tasks: List<Any>, private val listener: OnTaskClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_TASK = 0
        const val VIEW_TYPE_TEXT = 1
    }

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemContainer.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = tasks[position]
                    if (item is Task) {
                        listener.onTaskClick(item)
                    }
                }
            }

            binding.checkBox.setOnClickListener {
                val position = adapterPosition
                val task = tasks[position] as Task
                if (binding.checkBox.isChecked) {
                    task.status = TaskStatus.COMPLETED
                    task.progress = 100
                } else {
                    task.status = TaskStatus.START
                    task.progress = 0
                }
                notifyItemChanged(position)
            }
            binding.deleteIcon.setOnClickListener {
                listener.onDeleteClick(tasks[adapterPosition] as Task)
            }
        }
    }
    inner class TextViewHolder(val binding: ItemTextBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return when (tasks[position]) {
            is Task -> VIEW_TYPE_TASK
            is String -> VIEW_TYPE_TEXT
            else -> throw IllegalArgumentException("Invalid type of data at position $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_TASK -> {
                val binding = ItemTaskBinding.inflate(inflater, parent, false)
                TaskViewHolder(binding)
            }
            VIEW_TYPE_TEXT -> {
                val binding = ItemTextBinding.inflate(inflater, parent, false)
                TextViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TaskViewHolder -> {
                val task = tasks[position] as Task
                holder.binding.task = task
                holder.binding.checkBox.isChecked = task.status == TaskStatus.COMPLETED

                if (task.dueDate != null) {
                    val formattedDueDate = formatDate(task.dueDate!!)
                    holder.binding.taskDueDate.text = formattedDueDate
                } else {
                    holder.binding.taskDueDate.text = ""
                }

                if (task.status == TaskStatus.COMPLETED) {
                    holder.binding.taskTitle.paintFlags = holder.binding.taskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    holder.binding.taskTitle.paintFlags = holder.binding.taskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
            is TextViewHolder -> {
                val text = tasks[position] as String
                holder.binding.textView.text = text
            }
        }
    }

    @OptIn(UnstableApi::class) private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }


    override fun getItemCount() = tasks.size

    interface OnTaskClickListener {
        fun onTaskClick(task: Task)
        fun onDeleteClick(task: Task)
    }

}