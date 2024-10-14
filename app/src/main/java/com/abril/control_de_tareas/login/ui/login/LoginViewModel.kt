package com.abril.control_de_tareas.login.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _navigationChannel = Channel<String>()
    val navigationFlow = _navigationChannel.receiveAsFlow()

    /**
     * ************** VALIDAR EMAIL Y PASSWORD  ****************
     * Se recopila email y password para evaluar si cumplen las características específicas.
     *
     * @param email Recibe el email que el usuario ingresa.
     * @param password Recibe el password que el usuario ingresa.
     */
    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
    }

    /**
     * ************** VALIDAR PASSWORD  ****************
     * Se valida que el password tenga más de 6 caracteres.
     */
    private fun isValidPassword(password: String): Boolean = password.length > 6

    /**
     * ************** VALIDAR EMAIL  ****************
     * Se valida que el email contenga una estructura común para un email.
     */
    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * ************** LOGIN ****************
     * Se le manda a FirebaseAuth el usuario y contraseña para validar autenticación.
     */
    suspend fun onLoginSelected() {
        _isLoading.value = true
        try {
            firebaseAuth.signInWithEmailAndPassword(_email.value ?: "", _password.value ?: "")
                .await()
            // Navegar a pantalla tareas.
            _navigationChannel.send("tasks")
        } catch (e: Exception) {
            _errorMessage.value = "Login failed: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * ************** SIGN UP ****************
     * Navega a la pantalla SignUp
     */
    suspend fun onSignUpSelected() {
        _isLoading.value = true
        try {
            _navigationChannel.send("signup")
        } catch (e: Exception) {
            _errorMessage.value = "Signed failed: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
}
