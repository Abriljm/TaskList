package com.abril.control_de_tareas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abril.control_de_tareas.login.ui.task.TaskRepository
import com.abril.control_de_tareas.navigation.NavigationWrapper
import com.abril.control_de_tareas.ui.theme.Control_de_tareasTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()

        setContent {
            navHostController = rememberNavController()
            val taskRepository = TaskRepository()
            Control_de_tareasTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavigationWrapper(navHostController, auth, taskRepository)
                    LaunchedEffect(Unit){
                        val currentUser = auth.currentUser
                        if (currentUser!= null){
                            navHostController.navigate("tasks"){
                                popUpTo("login") { inclusive = true } //evitar error con el botón back
                            }
                            Log.e("Abril","Sesión ya iniciada, Navegar a Task")
                        } else {
                            navHostController.navigate("login"){
                                popUpTo("tasks") { inclusive = true }
                            }
                            Log.e("Abril","Sesión NO iniciada aún, Navegar a login")
                        }
                    }
                }
            }
        }
    }

}
