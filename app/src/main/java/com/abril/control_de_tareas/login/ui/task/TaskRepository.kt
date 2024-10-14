package com.abril.control_de_tareas.login.ui.task

import com.abril.control_de_tareas.login.data.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

/**
 * ************** GESTOR QUE COMUNICA A LA DB FIRESTORE ****************
 * Se comunica con Firestore para las funciones CRUD.
 */
class TaskRepository {
    private val db = FirebaseFirestore.getInstance()
    private val taskCollection = db.collection("tasks")

    //Obtener todas las tareas

    suspend fun getTasks(): List<Task> {
        return try {
            val query = taskCollection.get().await()
            query.toObjects(Task::class.java)
        } catch (e: Exception) {
            emptyList() // Devuelve una lista vacía si ocurre un error
        }
    }

    // Agregar una nueva tarea
    suspend fun addTask(task: Task) {
        try {
            if (task.id == null) {
                val newTaskId = taskCollection.document().id
                task.id = newTaskId
                taskCollection.document(newTaskId).set(task).await()
            } else {
                taskCollection.document(task.id!!).set(task).await()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    // Actualizar el estado de la tarea
    suspend fun updateTaskStatus(taskId: String, isCompleted: Boolean) {
        try {
            taskCollection.document(taskId).update("isCompleted", isCompleted).await()
        } catch (e: Exception) {
            throw e
        }
    }

    // Editar una tarea existente
    suspend fun editTask(task: Task) {
        try {
            task.id?.let { taskId ->
                taskCollection.document(taskId).set(task).await()
            } ?: throw Exception("Task ID is null")
        } catch (e: Exception) {
            throw e
        }
    }

    //Borrar tarea
    suspend fun deleteTask(taskId: String) {
        try {
            taskCollection.document(taskId).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }
}
