package com.abril.control_de_tareas.login.ui.task

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.abril.control_de_tareas.login.data.Task
import com.abril.control_de_tareas.ui.theme.BlueButton
import com.abril.control_de_tareas.ui.theme.PurpleButton
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EditTaskScreen(
    taskId: String?,
    viewModel: TaskViewModel,
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    val errorMessage: String? by viewModel.errorMessage.observeAsState(null)

    //Obtiene la información registrada según el Id
    LaunchedEffect(taskId) {
        Log.e("Abril", "$taskId")
        taskId?.let {
            val task = viewModel.getTaskById(it)
            task?.let {
                title = it.title
                description = it.description ?: ""
                isCompleted = it.isCompleted
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Task") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Cada campo se llena con la info existente de la tarea.
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isCompleted, onCheckedChange = { isCompleted = it })
                Text(text = "Completed")
            }
            Spacer(modifier = Modifier.height(16.dp))

            //Update Button
            Button(
                onClick = {
                    val updatedTask = Task(
                        id = taskId,
                        title = title,
                        description = description,
                        isCompleted = isCompleted
                    )
                    viewModel.editTask(updatedTask) // Llamamos a la función para editar la tarea
                    navController.popBackStack() // Regresamos a la pantalla anterior
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = BlueButton )
            ) {
                Text("Save")
            }
            Spacer(modifier = Modifier.height(8.dp))
            //Delete Button
            Button(
                onClick = {
                    taskId?.let { viewModel.deleteTask(it) }
                    navController.popBackStack() // Regresamos a la pantalla anterior
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete Task", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}
