package com.noatnoat.todoapp.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noatnoat.todoapp.constant.Priority
import com.noatnoat.todoapp.constant.RepeatInterval
import com.noatnoat.todoapp.constant.TaskStatus

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    var priority: Priority = Priority.MEDIUM,
    var progress: Int = 0,
    val isRepeating: Boolean = false,
    val repeatInterval: RepeatInterval? = null,
    val createdDate: Long? = null,
    var dueDate: Long? = null,
    var status: TaskStatus = TaskStatus.START,
    val projectId: Int? = null
)