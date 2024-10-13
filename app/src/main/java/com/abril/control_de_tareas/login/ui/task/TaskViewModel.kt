package com.abril.control_de_tareas.login.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abril.control_de_tareas.login.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel (
    private val taskRepository: TaskRepository
) : ViewModel(){

    //Flow: funciona como el LiveData
    private val _taskList = MutableStateFlow<List<Task>>(emptyList())
    val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _filteredTaskList = MutableStateFlow<List<Task>>(emptyList())
    val filteredTaskList: StateFlow<List<Task>> = _filteredTaskList.asStateFlow()

    private var currentFilter: Boolean = false

    init {
        //Cargar las tareas cuando se crea el view model
        loadTasks()
    }
    //Cargar tareas desde el repositorio
    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            try{
                val tasks = taskRepository.getTasks()
                if (tasks.isNotEmpty()) {
                    _taskList.value = tasks
                    applyFilter(currentFilter)
                } else {
                    _taskList.value = emptyList()
                }
            } catch (e: Exception) {
                _taskList.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    //Filttro para las tareas completadas
    fun applyFilter(showCompleted: Boolean) {
        currentFilter = showCompleted
        val currentTasks = _taskList.value
        _filteredTaskList.value = if (showCompleted) {
            currentTasks.filter { it.isCompleted }
        } else {
            currentTasks
        }
    }

    //Actualizar el estatus de las tareas
    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            try{
                //Actualizar el estado del repositorio y volver a cargar tareas actualizadas
                task.id?.let { taskRepository.updateTaskStatus(it, isCompleted) }
                loadTasks()
            } catch (e: Exception){
                //Manejo del error
            }
        }
    }

    fun addTask(newTask: Task) {
        viewModelScope.launch {
            try{
                taskRepository.addTask(newTask)
                val updatedTasks = taskRepository.getTasks() // Obtener la lista actualizada
                _taskList.value = updatedTasks
            }catch (e: Exception){
                //Manejo del error
            }
        }
    }

    fun editTask(updatedTask: Task) {
        viewModelScope.launch {
            try {
                taskRepository.editTask(updatedTask)
                loadTasks()
            } catch (e: Exception) {
                //manejar error
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(taskId)
                loadTasks()
            } catch (e: Exception){
                //Exception
            }
        }
    }

    fun getTaskById(taskId: String): Task? {
        return taskList.value.find { it.id == taskId }
    }

}