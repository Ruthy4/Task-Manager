package com.example.todoapp.data.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.TodoDao
import com.example.todoapp.data.models.ToDoData

class TodoRepository(private val todoDao: TodoDao) {

    val getAllData: LiveData<List<ToDoData>> = todoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        todoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        todoDao.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData) {
        todoDao.deleteData(toDoData)
    }

    suspend fun deleteAll() {
        todoDao.deleteAll()
    }
}