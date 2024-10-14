package com.abril.control_de_tareas.login.ui.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.tasks.await

class SignupViewModel(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

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

    private fun isValidPassword(password: String): Boolean = password.length > 6

    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

    /**
     * ************** CAPTURAR REGISTRO ****************
     * Se le pasa la información del usuario a la DB.
     */
    suspend fun onRegisterSelected() {
        try {
            firebaseAuth.createUserWithEmailAndPassword(_email.value ?: "", _password.value ?: "")
                .await()
            _toastMessage.value = "Sign Up correctly"
            _navigationChannel.send("tasks")
        } catch (e: Exception) {
            _errorMessage.value = "Registration failed: ${e.message}"
        }
    }
}
