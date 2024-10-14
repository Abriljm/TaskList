package com.abril.control_de_tareas.login.ui.task

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var currentFilter: Boolean = false

    init {
        //Carga las tareas cuando se crea el view model.
        loadTasks()
    }

    /**
     * ************** LISTA DE TAREAS ****************
     * Carga lista de tareas completa, evaluando posibles errores.
     */
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

    /**
     * ************** FILTRO ****************
     * Se aplica un filtro a la lista de tareas, mostranndo solo las tareas completadas si así se requiere.
     *
     * @param showCompleted indica si se deben mostrar únicamente las tareas completadas si su estado es verdadero.
     */
    fun applyFilter(showCompleted: Boolean) {
        //Actualiza la variable que indica el estado actual de filtro.
        currentFilter = showCompleted
        //Obtener lista actual de tareas.
        val currentTasks = _taskList.value
        //Filtrar tareas según el parámetro, mostrando solo las completas.
        _filteredTaskList.value = if (showCompleted) {
            currentTasks.filter { it.isCompleted }
        } else {
            currentTasks //Mostrar todas las tareas
        }
    }

    /**
     * ************** ESTATUS DE TAREA ****************
     * Actualiza el estatus de la tarea seleccionada según el Id de la misma.
     *
     * @param Task Recibe la data class
     * @param isCompleted Actualiza el estado del repositorio.
     */
    fun updateTaskStatus(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            try{
                //Actualiza el estado del repositorio y vuelve a cargar tareas actualizadas
                task.id?.let { taskRepository.updateTaskStatus(it, isCompleted) }
                loadTasks()
            } catch (e: Exception){
                _errorMessage.value = "Updated Task Status failed: ${e.message}"            }
        }
    }

    /**
     * ************** AGREGAR TAREA NUEVA ****************
     * Agrega una tarea nueva a la lista
     *
     * @param newTask recibe los datos nuevos para pasarlos al repositorio.
     */
    fun addTask(newTask: Task) {
        viewModelScope.launch {
            try{
                taskRepository.addTask(newTask)
                Log.d("TaskViewModel", "Tareas después de agregar: ${_taskList.value}")
                loadTasks()
            }catch (e: Exception){
                _errorMessage.value = "Add Task failed: ${e.message}"
            }
        }
    }

    /**
     * ************** EDITAR TAREA  ****************
     * Recibe la nueva información y la manda a la DB, después actualiza la lista de tareas.
     *
     * @param newTask recibe los datos nuevos para pasarlos al repositorio.
     */
    fun editTask(updatedTask: Task) {
        viewModelScope.launch {
            try {
                taskRepository.editTask(updatedTask)
                loadTasks()
            } catch (e: Exception) {
                _errorMessage.value = "Edit Task failed: ${e.message}"
            }
        }
    }

    /**
     * ************** ELIMINAR TAREA  ****************
     * Recibe el Id de la tarea a eliminar, realiza el proceso y actualiza la lista de tareas.
     *
     * @param taskId Recibe el Id de la tarea que se eliminará.
     */
    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(taskId)
                loadTasks()
            } catch (e: Exception){
                _errorMessage.value = "Delete Task failed: ${e.message}"
            }
        }
    }

    /**
     * ************** OBTENER ID DE TAREA QUE SE EDITARÁ  ****************
     * Se le pasa el Id de la tarea que se editará para rellenar los campos con la información existente.
     *
     * @param taskId Recibe el Id de la tarea que se editará.
     */
    fun getTaskById(taskId: String): Task? {
        return taskList.value.find { it.id == taskId }
    }

}