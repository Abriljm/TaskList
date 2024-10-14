package com.abril.control_de_tareas.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.abril.control_de_tareas.login.ui.login.LoginScreen
import com.abril.control_de_tareas.login.ui.login.LoginViewModel
import com.abril.control_de_tareas.login.ui.login.LoginViewModelFactory
import com.abril.control_de_tareas.login.ui.signup.SignUpScreen
import com.abril.control_de_tareas.login.ui.signup.SignUpViewModelFactory
import com.abril.control_de_tareas.login.ui.signup.SignupViewModel
import com.abril.control_de_tareas.login.ui.task.AddTaskScreen
import com.abril.control_de_tareas.login.ui.task.EditTaskScreen
import com.abril.control_de_tareas.login.ui.task.TaskRepository
import com.abril.control_de_tareas.login.ui.task.TaskScreen
import com.abril.control_de_tareas.login.ui.task.TaskViewModel
import com.abril.control_de_tareas.login.ui.task.TaskViewModelFactory
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationWrapper(
    navHostController: NavHostController,
    auth: FirebaseAuth,
    taskRepository: TaskRepository
) {

    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(auth))
    val signupViewModel: SignupViewModel = viewModel(factory = SignUpViewModelFactory(auth))
    val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(taskRepository))

    NavHost(navController = navHostController, startDestination = "login") {
        composable("login") {
            LoginScreen(loginViewModel, navHostController)
        }
        composable("signup") {
            SignUpScreen(signupViewModel, navHostController)
        }
        composable("tasks") {
            TaskScreen(taskViewModel, navHostController)
        }
        composable("addTaskScreen") {
            AddTaskScreen(taskViewModel, navHostController)
        }
        composable(
            route = "editTaskScreen/{taskId}",
            // Define "taskId" como argumento de tipo String en la navegación
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Recupera el valor de "taskId" de los argumentos de la entrada de backStack
            // Si es nulo, asigna una cadena vacía por defecto
            val taskId = backStackEntry.arguments?.getString("taskId")

            if (taskId.isNullOrEmpty()) {
                // Manejar el caso en el que el taskId no es válido, navegar de regreso
                navHostController.popBackStack()
            } else {
                EditTaskScreen(taskId = taskId, taskViewModel, navHostController)
            }

            EditTaskScreen(taskId = taskId, taskViewModel, navHostController)
        }
    }
}
