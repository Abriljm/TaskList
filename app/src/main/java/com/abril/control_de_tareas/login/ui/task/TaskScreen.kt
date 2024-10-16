package com.abril.control_de_tareas.login.ui.task

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.abril.control_de_tareas.R
import com.abril.control_de_tareas.login.data.Task
import com.abril.control_de_tareas.ui.theme.BabyBlue
import com.abril.control_de_tareas.ui.theme.Violet
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskScreen(viewModel: TaskViewModel, navController: NavController) {
    val taskList: List<Task> by viewModel.taskList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val filteredTaskList: List<Task> by viewModel.filteredTaskList.collectAsState()
    var filterCompleted by remember { mutableStateOf(false) }

    //Muestra la lista de tareas actualizada cuando se renderiza la pantalla.
    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }

    /**
     * ************** ESTRUCTURA DE LA PANTALLA ****************
     * Se estructura cada elemento de la pantalla, como la lista de tareas con lazyColumn y los floatingActionButton.
     */
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Tasks",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ) },
                actions = {
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo("tasks") { inclusive = true }
                        }
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BabyBlue
                )
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                FloatingActionButton(
                    modifier = Modifier.padding(start = 20.dp),
                    containerColor = BabyBlue,
                    onClick = {
                    filterCompleted = !filterCompleted
                    viewModel.applyFilter(showCompleted = filterCompleted)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = "Filter"
                    )
                }

                FloatingActionButton(
                    containerColor = Violet,
                    onClick = {
                    navController.navigate("addTaskScreen")
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "AddTask")
                }
            }
        }

    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            val displayList = if (filterCompleted) filteredTaskList else taskList

            if (displayList.isEmpty()) {
                Text("No tasks available")
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(displayList) { task ->
                        Column {
                            TaskItem(
                                task = task,
                                onTaskClicked = {
                                    navController.navigate("editTaskScreen/${task.id}")
                                },
                                onCheckedChanged = { checked ->
                                    viewModel.updateTaskStatus(task, checked)
                                }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                thickness = 1.dp,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * ************** ELEMENTOS QUE CONTIENE CADA TAREA  ****************
 * Aquí se organizan los elementos de cada tareas, como título, descripción y checkbox.
 */
@Composable
fun TaskItem(
    task: Task,
    onTaskClicked: () -> Unit,
    onCheckedChanged: (Boolean) -> Unit)
{

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClicked() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(modifier = Modifier.weight(1f)) {
            Text(text = task.title, fontWeight = FontWeight.Bold)
            task.description?.let {
                Text(text = it, fontSize = 12.sp )
            }
        }
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onCheckedChanged(it)}
        )
    }
}

