package com.abril.control_de_tareas.login.ui.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.abril.control_de_tareas.ui.theme.BabyBlue
import com.abril.control_de_tareas.ui.theme.PinkLabel
import com.abril.control_de_tareas.ui.theme.PurpleButton
import com.abril.control_de_tareas.ui.theme.Violet
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(viewModel: SignupViewModel, navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, BabyBlue, Violet), startY = 900f, endY = 1800f))
    ){
        Box(Modifier.fillMaxSize().padding(16.dp)) {
            SignUp(Modifier.align(Alignment.Center), viewModel, navController)
        }
    }
}

@Composable
fun SignUp(
    modifier: Modifier,
    viewModel: SignupViewModel,
    navController: NavController
) {
    val email : String by viewModel.email.observeAsState(initial = "")
    val password : String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val coroutineLogin = rememberCoroutineScope()
    val errorMessage: String? by viewModel.errorMessage.observeAsState(null)
    val context = LocalContext.current
    val toastMessage: String? by viewModel.toastMessage.observeAsState()

    // Recibir evento de navegación
    val navigationFlow = viewModel.navigationFlow.collectAsState(initial = null)

    LaunchedEffect(navigationFlow.value) {
        navigationFlow.value?.let { screen ->
            navController.navigate(screen) // Navegar a la pantalla correspondiente
        }
    }

    // Mostrar el Toast si hay el registro fue exitoso
    toastMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        // Reseteamos el valor del toast para que no se vuelva a mostrar
        viewModel._toastMessage.value = null
    }

    Column (modifier = modifier){
        TitleSignUp(Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField(email) { viewModel.onLoginChanged(it, password) }
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(password) {viewModel.onLoginChanged(email, it)}
        Spacer(modifier = Modifier.padding(16.dp))
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.padding(16.dp))
        }

        RegisterButton(loginEnable) {
            coroutineLogin.launch {
                viewModel.onRegisterSelected() // Llamada para registrar al usuario
            }
        }
    }

}

@Composable
fun RegisterButton(loginEnable: Boolean, onRegisterSelected: () -> Unit) {
    Button(
        onClick = { onRegisterSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = loginEnable, // Se habilita o deshabilita según las mismas reglas que el login
        colors = ButtonDefaults.buttonColors(containerColor = PurpleButton )
    ) {
        Text(text = "Register")
    }
}

@Composable
fun PasswordField(password: String, onTextFieldChanged:(String) -> Unit) {
    TextField(
        value = password, onValueChange = {onTextFieldChanged(it)},
        placeholder = { Text(text = "Password") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1
    )
}


@Composable
fun EmailField(email:String, onTextFieldChanged:(String) -> Unit) {
    TextField(value = email, onValueChange = { onTextFieldChanged(it) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        maxLines = 1,
        //colors =
    )
}

@Composable
fun TitleSignUp(modifier: Modifier) {
    Text(
        text = "Sign Up",
        fontSize = 35.sp,
        fontWeight = FontWeight.Bold,
        color = PinkLabel,
        modifier = modifier
            .padding(8.dp)
    )
}