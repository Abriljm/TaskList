package com.abril.control_de_tareas.login.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.abril.control_de_tareas.R
import com.abril.control_de_tareas.ui.theme.BabyBlue
import com.abril.control_de_tareas.ui.theme.BlueButton
import com.abril.control_de_tareas.ui.theme.PinkLabel
import com.abril.control_de_tareas.ui.theme.PurpleButton
import com.abril.control_de_tareas.ui.theme.Violet
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(viewModel: LoginViewModel, navController: NavController){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, BabyBlue, Violet), startY = 900f, endY = 1800f))
    ){
        Box(Modifier.fillMaxSize().padding(16.dp)) {
            Login(Modifier.align(Alignment.Center), viewModel, navController)
        }
    }
}

@Composable
fun Login(
    modifier: Modifier,
    viewModel: LoginViewModel,
    navController: NavController
) {
    val email : String by viewModel.email.observeAsState(initial = "")
    val password : String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val coroutineLogin = rememberCoroutineScope()
    val errorMessage: String? by viewModel.errorMessage.observeAsState(null)

    // Recibir evento de navegación
    val navigationFlow = viewModel.navigationFlow.collectAsState(initial = null)

    LaunchedEffect(navigationFlow.value) {
        navigationFlow.value?.let { screen ->
            navController.navigate(screen) // Navegar a la pantalla correspondiente
        }
    }

    Column (modifier = modifier){
        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.padding(16.dp))
        EmailField(email) { viewModel.onLoginChanged(it, password) }
        Spacer(modifier = Modifier.padding(4.dp))
        PasswordField(password) {viewModel.onLoginChanged(email, it)}
        Spacer(modifier = Modifier.padding(8.dp))
        ForgotPassword(Modifier.align(Alignment.End))
        Spacer(modifier = Modifier.padding(16.dp))
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
        LoginButton(loginEnable) {
            coroutineLogin.launch {
                viewModel.onLoginSelected()
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))

        SignUpButton {
            coroutineLogin.launch {
                viewModel.onSignUpSelected() // Llamada para navegar a sign up
            }
        }
    }

}

@Composable
fun SignUpButton(onRegisterSelected: () -> Unit) {
    Button(
        onClick = { onRegisterSelected() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = BlueButton )
    ) {
        Text(text = "Sign Up")
    }
}


@Composable
fun LoginButton(loginEnable: Boolean, onLoginSelected: () -> Unit) {
    Button(onClick = { onLoginSelected() },
        modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
        enabled = loginEnable, // se habilita siguiendo las reglas del View Model para cada campo
        colors = ButtonDefaults.buttonColors(containerColor = PurpleButton )
    ){
        Text(text = "Login")
    }
}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Forgot your password?",
        modifier = modifier.clickable {  },
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = PinkLabel
    )
}

@Composable
fun PasswordField(password: String, onTextFieldChanged:(String) -> Unit) {
    TextField(
        value = password, onValueChange = {onTextFieldChanged(it)},
        placeholder = { Text(text = "Password")},
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
fun HeaderImage(modifier: Modifier) {
    Image(painter = painterResource(id = R.drawable.login_image),
        contentDescription = "LoginImage",
        modifier = modifier
    )
}
