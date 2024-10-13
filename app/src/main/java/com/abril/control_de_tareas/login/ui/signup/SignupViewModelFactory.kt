package com.abril.control_de_tareas.login.ui.signup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModelFactory(
    private val firebaseAuth: FirebaseAuth
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            SignupViewModel(firebaseAuth) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
