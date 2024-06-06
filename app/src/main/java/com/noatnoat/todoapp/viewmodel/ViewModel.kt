package com.noatnoat.todoapp.viewmodel

import androidx.annotation.OptIn
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.noatnoat.todoapp.model.room.Task
import com.noatnoat.todoapp.model.room.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(private val taskDao: TaskDao) : ViewModel() {

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> get() = _tasks

    init {
        refreshTasks()
    }

    @OptIn(UnstableApi::class)
    fun refreshTasks() = viewModelScope.launch(Dispatchers.IO) {
        val allTasks = taskDao.getAll()
        Log.d("refreshTasks", "All tasks from database: $allTasks")
        _tasks.postValue(allTasks)
        delay(1000) // wait for 1 second
        Log.d("refreshTasks", "refreshTasks ${_tasks.value}")
    }

    fun insert(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.insert(task)
        refreshTasks()
    }

    fun isTasksEmpty(): Boolean {
        val tasksValue = _tasks.value
        return tasksValue == null || tasksValue.isEmpty()
    }

    fun update(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.update(task)
        refreshTasks()
    }

    fun delete(task: Task) = viewModelScope.launch(Dispatchers.IO) {
        taskDao.delete(task)
        refreshTasks()
    }
}